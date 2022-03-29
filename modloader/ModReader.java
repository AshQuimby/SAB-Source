package modloader;

import java.util.ArrayList;
import game.character.Character;
import java.io.File;
import game.Player;

public class ModReader {

   private static ArrayList<ModCharacter> modCharacters = new ArrayList<>();
   private static ArrayList<ModStage> modStages = new ArrayList<>();
   private static ArrayList<ModPlayerOverride> modPlayers = new ArrayList<>();

   public static void load() {
      readFolder(new File("mods"));
   }

   public static int addModCharacter(ModCharacter character) {
      System.out.println("Request Recieved; Character being added");
      if (modCharacters.contains(character)) {
         System.out.println(
               "Duplicate Character Error: Two modded characters of the same type were found while loading mods");
         System.exit(1);
      }
      modCharacters.add(character);
      return modCharacters.size();
   }

   public static int addModStage(ModStage stage) {
      System.out.println("Request Recieved; Stage being added");
      if (modStages.contains(stage)) {
         System.out.println(
               "Duplicate Stage Error: Two modded stages of the same type were found while loading mods");
         System.exit(1);
      }
      modStages.add(stage);
      return modStages.size();
   }

   public static int addModPlayer(ModPlayerOverride modPlayer) {
      System.out.println("Request Recieved; ModPlayer being added");
      modPlayers.add(modPlayer);
      return modPlayers.size();
   }

   public static ArrayList<ModCharacter> getModCharacters() {
      return modCharacters;
   }

   public static ArrayList<ModStage> getmodStages() {
      return modStages;
   }

   public static Character getModCharacter(int index) {
      return modCharacters.get(index);
   }

   public static void injectModPlayers(Player player) {
      for (ModPlayerOverride modPlayer : modPlayers) {
         player.modPlayers.add(modPlayer.copy());
      }
   }

   public static void readFolder(File folder) {
      File[] listOfFiles = folder.listFiles();

      for (File file : listOfFiles) {
         if (file.isFile()) {
            // Look for ModLoader files on windows
            if (System.getProperty("os.name").startsWith("W")) {
               try {
                  // This does NOT get saved for use, this exists to load the ModLoader class and
                  // execute its "static" so that mods load properly
                  Class.forName(file.getPath().replace('\\', '.').substring(0, file.getPath().length() - 5))
                        .newInstance();
               } catch (ClassNotFoundException e) {
               } catch (InstantiationException e) {
               } catch (IllegalAccessException e) {
               }
            } else {
               // Look for ModLoader files on mac/linux/AmogOS
               try {
                  // This does NOT get saved for use, this exists to load the ModLoader class and
                  // execute its "static" so that mods load properly
                  Class.forName(file.getPath().replace('/', '.').substring(0, file.getPath().length() - 5))
                        .newInstance();
               } catch (ClassNotFoundException e) {
               } catch (InstantiationException e) {
               } catch (IllegalAccessException e) {
               }
            }
         } else if (file.isDirectory()) {
            readFolder(file);
         }
      }
   }
}