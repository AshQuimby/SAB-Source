package game;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;

import javafx.scene.media.AudioClip;

import java.util.ArrayList;

public class SoundEngine {
    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static final ArrayList<AudioClip> currentMusicTrack = new ArrayList<>();
    public static float playbackSpeed = 1;

    public static void load() {
        File assets = new File("assets/sounds");
        String[] fileNames = assets.list();

        for (String fileName : fileNames) {
            if (fileName.endsWith(".wav") || fileName.endsWith(".mp3")) {
                sounds.put(fileName.substring(0, fileName.length() - 4), new AudioClip(Paths.get("assets/sounds/" + fileName).toUri().toString()));
            }
        }
        readModSounds(new File("mods"));
    }
    
    public static void stopMusic() {
        try {
            currentMusicTrack.get(0).stop();
            currentMusicTrack.remove(0);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public static synchronized void playMusic(String fileName) {
        if (Settings.music()) {
            stopMusic();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        currentMusicTrack.add(sounds.get(fileName));
                        currentMusicTrack.get(0).setCycleCount(-1);
                        sounds.get(fileName).play(Settings.volume(), 1, playbackSpeed, 0, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static synchronized void playMusicOnce(String fileName) {
        if (Settings.music()) {
            stopMusic();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        sounds.get(fileName).play(Settings.volume(), 1, playbackSpeed, 0, 1);
                        currentMusicTrack.add(sounds.get(fileName));
                    } catch (Exception e) {
                        System.out.println("WARNING: Error loading sound \"" + fileName + "\"");
                    }
                }
            }).start();
        }
    }

    public static synchronized void playSound(String fileName) {
        if (Settings.soundEffects()) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        sounds.get(fileName).play(Settings.volume(), 1, playbackSpeed, 0, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static void readModSounds(File folder) {
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".wav")) {
                new AudioClip(Paths.get(file.getPath()).toUri().toString());
            } else if (file.isDirectory()) {
                readModSounds(file);
            }
        }
    }
}
