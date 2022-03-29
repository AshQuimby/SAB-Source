package game.stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import game.physics.*;

public final class StageLoader {
    private static String[] validProperties = new String[] {
            "name",
            "soft_bounds",
            "hard_bounds",
            "platform",
            "ledge",
            "background",
            "music",
            "music_credit",
            "max_zoom",
            "player_one_spawn_offset",
            "player_two_spawn_offset"
    };

    private static final String[] requiredProperties = new String[] {
            "name",
            "soft_bounds",
            "hard_bounds"
    };

    private static final String[] multiProperties = new String[] {
            "platform",
            "ledge"
    };

    private static boolean isValidProperty(String property) {
        for (String validProperty : validProperties) {
            if (property.equals(validProperty)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isMultiProperty(String property) {
        for (String multiProperty : multiProperties) {
            if (property.equals(multiProperty)) {
                return true;
            }
        }

        return false;
    }

    private static AABB parseAABB(String source) {
        String[] parameters = source.split(" ");

        if (parameters.length != 4) {
            return null;
        }

        return new AABB(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]),
                Double.parseDouble(parameters[2]), Double.parseDouble((parameters[3])));
    }

    private static Platform parsePlatform(String source) {
        String[] parameters = source.split(" ");

        if (parameters.length != 6) {
            return null;
        }

        return new Platform(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]),
                Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]), Boolean.parseBoolean(parameters[4]),
                parameters[5]);
    }

    private static int parseDirection(String source) {
        if (source.equals("left")) {
            return -1;
        } else if (source.equals("right")) {
            return 1;
        }

        return 0;
    }

    private static Ledge parseLedge(String source) {
        String[] parameters = source.split(" ");

        if (parameters.length != 5) {
            return null;
        }

        int direction = parseDirection(parameters[4]);
        if (direction == 0) {
            return null;
        }

        return new Ledge(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]),
                Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]), direction);
    }

    private static Vector parseVector(String vectorString) {
        String[] parameters = vectorString.split(" ");

        if (parameters.length != 2) {
            return null;
        }

        return new Vector(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]));
    }

    public static Stage loadStage(String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        Map<String, String> properties = new HashMap<>();
        Map<String, List<String>> listProperties = new HashMap<>();

        for (String multiProperty : multiProperties) {
            listProperties.put(multiProperty, new ArrayList<>());
        }

        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String token = scanner.next();

            if (token.startsWith("@")) {
                String property = token.substring(1);
                String value = scanner.nextLine();
                value = value.trim();

                if (!isValidProperty(property)) {
                    System.out.println(String.format("Failed to load stage \"%s\": Property \"%s\" does not exist",
                            filePath, property));
                    scanner.close();
                    return null;
                }

                if (isMultiProperty(property)) {
                    if (!listProperties.containsKey(property)) {
                        listProperties.put(property, new ArrayList<>());
                    }

                    listProperties.get(property).add(value);
                } else {
                    if (!properties.containsKey(property)) {
                        properties.put(property, value);
                    } else {
                        System.out.println(String.format("Failed to load stage \"%s\": Property \"%s\" already defined",
                                filePath, property));
                        scanner.close();
                        return null;
                    }
                }
            }
        }

        scanner.close();

        for (String requiredProperty : requiredProperties) {
            if (!properties.containsKey(requiredProperty)) {
                System.out.println(String.format("Failed to load stage \"%s\": Missing required property \"%s\"",
                        filePath, requiredProperty));
                return null;
            }
        }

        Stage stage = new Stage();

        stage.name = properties.get("name");
        stage.backgroundImage = properties.get("background");
        stage.musicCredit = "null";
        stage.deadPlatforms = new ArrayList<>();
        stage.maxZoomOut = 2;
        stage.platforms = new ArrayList<>();
        stage.ledges = new ArrayList<>();
        stage.musicFile = "big_seagull_victory";
        stage.p1SpawnOffset = new Vector(0, 0);
        stage.p2SpawnOffset = new Vector(0, 0);

        AABB softBounds = parseAABB(properties.get("soft_bounds"));

        if (softBounds == null) {
            System.out
                    .println(String.format("Failed to load stage \"%s\": Property \"soft_bounds\" requires 4 arguments",
                            filePath));
            return null;
        }

        stage.safeBlastZone = softBounds;

        AABB hardBounds = parseAABB(properties.get("hard_bounds"));

        if (hardBounds == null) {
            System.out
                    .println(String.format("Failed to load stage \"%s\": Property \"hard_bounds\" requires 4 arguments",
                            filePath));
            return null;
        }

        String p1SpawnOffset = properties.get("player_one_spawn_offset");

        if (p1SpawnOffset != null && p1SpawnOffset.length() > 0) {
            stage.p1SpawnOffset = parseVector(p1SpawnOffset);
        }

        String p2SpawnOffset = properties.get("player_two_spawn_offset");

        if (p2SpawnOffset != null && p2SpawnOffset.length() > 0) {
            stage.p2SpawnOffset = parseVector(p2SpawnOffset);
        }

        String maxZoom = properties.get("max_zoom");

        if (maxZoom != null && maxZoom.length() > 0) {
            stage.maxZoomOut = Float.parseFloat(maxZoom);
        }

        String music = properties.get("music");

        if (music != null && music.length() > 0) {
            stage.musicFile = music;
        }

        String musicCredit = properties.get("music_credit");

        if (musicCredit != null && musicCredit.length() > 0) {
            stage.musicCredit = musicCredit;
        }

        stage.unsafeBlastZone = hardBounds;

        for (String platformSource : listProperties.get("platform")) {
            Platform platform = parsePlatform(platformSource);

            if (platform == null) {
                System.out
                        .println(String.format(
                                "Failed to load stage \"%s\": Error parsing platform",
                                filePath));
                return null;
            }
            stage.platforms.add(platform);
        }

        for (String ledgeSource : listProperties.get("ledge")) {
            Ledge ledge = parseLedge(ledgeSource);

            if (ledge == null) {
                System.out
                        .println(String.format(
                                "Failed to load stage \"%s\": Error parsing ledge",
                                filePath));
                return null;
            }
            stage.ledges.add(ledge);
        }

        return stage;
    }
}