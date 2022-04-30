package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public final class Settings {

  private static boolean musicSetting;
  private static boolean soundEffectsSetting;
  private static boolean fixedCameraSetting;
  private static float volumeSetting;
  private static int livesSetting;
  private static int aiPlayer1Setting;
  private static int aiPlayer2Setting;
  private static boolean testingModeSetting;
  private static int portSetting;
  private static boolean assBallSetting;
  private static boolean stageHazardSetting;
  private static boolean performanceModeSetting;

  public static void setSettings(boolean music, boolean soundEffects, boolean fixedCamera, float volume, int lives,
      int aiPlayer1, int aiPlayer2, boolean testingMode, int port, boolean assBall, boolean stageHazard, boolean performanceMode) {
    musicSetting = music;
    soundEffectsSetting = soundEffects;
    fixedCameraSetting = fixedCamera;
    volumeSetting = volume;
    livesSetting = lives;
    aiPlayer1Setting = aiPlayer1;
    aiPlayer2Setting = aiPlayer2;
    testingModeSetting = testingMode;
    portSetting = port;
    assBallSetting = assBall;
    stageHazardSetting = stageHazard;
    performanceModeSetting = performanceMode;
  }

  private static String[] properties = new String[] {
      "music",
      "sound_effects",
      "fixed_camera",
      "volume",
      "lives",
      "ai_player_1",
      "ai_player_2",
      "ass_balls",
      "stage_hazards",
      "performance_mode",
      "testing_mode",
      "port"
  };

  private static void defaultSettings() {
    setSettings(true, true, false, 0.5f, 3, 0, 1, false, 19128, true, true, false);
  }

  public static void writeToFile() {
    try {
      File oldOptions = new File("../options.sabo");
      oldOptions.delete();
    } catch (Exception e) {
      System.out.println("WARNING: Options file not found. Making options file...");
    }
    File options = new File("../options.sabo");

    try {
      FileWriter optionWriter = new FileWriter(options, false);

      optionWriter.write("@music " + musicSetting + "\n");
      optionWriter.write("@sound_effects " + soundEffectsSetting + "\n");
      optionWriter.write("@fixed_camera " + fixedCameraSetting + "\n");
      optionWriter.write("@volume " + volumeSetting + "\n");
      optionWriter.write("@lives " + livesSetting + "\n");
      optionWriter.write("@ai_player_1 " + aiPlayer1Setting + "\n");
      optionWriter.write("@ai_player_2 " + aiPlayer2Setting + "\n");
      optionWriter.write("@ass_balls " + assBallSetting + "\n");
      optionWriter.write("@stage_hazards " + stageHazardSetting + "\n");
      optionWriter.write("@performance_mode " + performanceModeSetting + "\n");
      optionWriter.write("@testing_mode " + testingModeSetting + "\n");
      optionWriter.write("@port " + portSetting + "\n");

      optionWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void load(String filePath) {
    try {
      File file = new File(filePath);

      Map<String, String> settings = new HashMap<>();

      Scanner scanner = new Scanner(file);

      String property = "";
      String value = "";

      while (scanner.hasNext()) {
        String token = scanner.next();
        if (token.startsWith("@")) {
          token = token.substring(1);
          if (propertyExists(token)) {
            property = token;
          } else {
            System.out
                .println("WARNING: Illegal settings file. Unexpected setting: " + token + ". Resetting to default...");
            defaultSettings();
            scanner.close();
            writeToFile();
            return;
          }
        } else {
          value = token;
          settings.put(property, value);
        }
      }
      try {
        musicSetting = Boolean.parseBoolean(settings.get("music"));
        soundEffectsSetting = Boolean.parseBoolean(settings.get("sound_effects"));
        fixedCameraSetting = Boolean.parseBoolean(settings.get("fixed_camera"));
        volumeSetting = Float.parseFloat(settings.get("volume"));
        livesSetting = Integer.parseInt(settings.get("lives"));
        aiPlayer1Setting = Integer.parseInt(settings.get("ai_player_1"));
        aiPlayer2Setting = Integer.parseInt(settings.get("ai_player_2"));
        assBallSetting = Boolean.parseBoolean(settings.get("ass_balls"));
        stageHazardSetting = Boolean.parseBoolean(settings.get("stage_hazards"));
        performanceModeSetting = Boolean.parseBoolean(settings.get("performance_mode"));
        testingModeSetting = Boolean.parseBoolean(settings.get("testing_mode"));
        portSetting = Integer.parseInt(settings.get("port"));

      } catch (Exception e) {
        System.out.println("WARNING: Illegal settings file. One or more values are null or wrong data type. Resetting to default...");
        defaultSettings();
        writeToFile();
      }

      scanner.close();

    } catch (FileNotFoundException e) {
      System.out.println("WARNING: Options file not found. Creating default options file...");
      defaultSettings();
      writeToFile();
    }
  }

  private static boolean propertyExists(String property) {
    for (String actualProperty : properties) {
      if (property.equals(actualProperty)) {
        return true;
      }
    }
    return false;
  }

  public static boolean music() {
    return musicSetting;
  }

  public static boolean soundEffects() {
    return soundEffectsSetting;
  }

  public static boolean fixedCamera() {
    return fixedCameraSetting;
  }

  public static float volume() {
    return volumeSetting;
  }

  public static int lives() {
    return livesSetting;
  }

  public static int aiPlayer1() {
    return aiPlayer1Setting;
  }

  public static int aiPlayer2() {
    return aiPlayer2Setting;
  }

  public static boolean testingMode() {
    return testingModeSetting;
  }

  public static int port() {
    return portSetting;
  }

  public static boolean assBalls() {
    return assBallSetting;
  }

  public static boolean stageHazards() {
    return stageHazardSetting;
  }
  
  public static boolean performanceMode() {
    return performanceModeSetting;
  }

  public static void setMusic(boolean setTo) {
    musicSetting = setTo;
  }

  public static void setSoundEffects(boolean setTo) {
    soundEffectsSetting = setTo;
  }

  public static void setFixedCamera(boolean setTo) {
    fixedCameraSetting = setTo;
  }

  public static void setVolume(float setTo) {
    volumeSetting = setTo;
  }

  public static void setLives(int setTo) {
    livesSetting = setTo;
  }

  public static void setAIPlayer1(int setTo) {
    aiPlayer1Setting = setTo;
  }

  public static void setAIPlayer2(int setTo) {
    aiPlayer2Setting = setTo;
  }

  public static void setTestingMode(boolean setTo) {
    testingModeSetting = setTo;
  }

  public static void setPort(int setTo) {
    portSetting = setTo;
  }

  public static void setAssBalls(boolean setTo) {
    assBallSetting = setTo;
  }

  public static void setStageHazards(boolean setTo) {
    stageHazardSetting = setTo;
  }
  
  public static void setPerformanceMode(boolean setTo) {
    performanceModeSetting = setTo;
  }
}