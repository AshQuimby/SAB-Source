package game;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.util.ArrayList;

public class SoundEngine {
    private static final Map<String, File> sounds = new HashMap<>();
    private static final ArrayList<Clip> currentMusicTrack = new ArrayList<>();

    public static void load() {
        File assets = new File("assets/sounds");
        String[] fileNames = assets.list();

        for (String fileName : fileNames) {
            if (fileName.endsWith(".wav")) {
                sounds.put(fileName, new File("assets/sounds/" + fileName));
            }
        }
        readModSounds(new File("mods"));
    }

    public static void stopMusic() {
        try {
            currentMusicTrack.get(0).close();
            currentMusicTrack.remove(0);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public static synchronized void playMusic(String fileName) {
        if (Settings.music()) {
            String fileFullName = fileName + ".wav";
            stopMusic();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        File audioFile = sounds.get(fileFullName);
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        clip.open(audioStream);
                        clip.start();
                        ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN))
                                .setValue(Settings.volume() * 80f - 80f);
                        clip.loop(-1);
                        currentMusicTrack.add(clip);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static synchronized void playMusicOnce(String fileName) {
        if (Settings.music()) {
            String fileFullName = fileName + ".wav";
            stopMusic();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        File audioFile = sounds.get(fileFullName);
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        clip.open(audioStream);
                        clip.start();
                        ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN))
                                .setValue(Settings.volume() * 80f - 80f);
                        currentMusicTrack.add(clip);
                    } catch (Exception e) {
                        System.out.println("WARNING: Error loading sound \"" + fileName + "\"");
                    }
                }
            }).start();
        }
    }

    public static synchronized void playSound(String fileName) {
        if (Settings.soundEffects()) {
            String fileFullName = fileName + ".wav";
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        File audioFile = sounds.get(fileFullName);
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        clip.open(audioStream);
                        ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN))
                                .setValue(Settings.volume() * 80f - 80f);
                        clip.start();
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
                sounds.put(file.getName(), file);
            } else if (file.isDirectory()) {
                readModSounds(file);
            }
        }
    }
}
