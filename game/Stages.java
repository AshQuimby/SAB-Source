package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

import game.screen.StageSelectScreen;
import game.stage.Stage;
import game.stage.StageLoader;

public final class Stages {
    public static void load() {
        File assets = new File("assets/stages");
        Queue<File> queue = new LinkedList<>();
        queue.offer(assets);

        while (!queue.isEmpty()) {
            File file = queue.poll();

            if (file.isDirectory()) {
                String[] fileNames = file.list();

                for (String fileName : fileNames) {
                    queue.offer(new File(fileName));
                }
            } else if (file.getName().endsWith(".sabs")) {
                try {
                    Stage stage = StageLoader.loadStage(String.format("assets/stages/%s", file));

                    if (stage != null) {
                        StageSelectScreen.stages.add(stage);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}