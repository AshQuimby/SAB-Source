package game.screen;

import game.stage.*;
import game.Images;
import game.physics.Vector;
import game.Fonts;
import game.character.Character;

import java.awt.Graphics;
import java.awt.*;
import java.util.ArrayList;
import modloader.ModReader;

import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

public class StageSelectScreen implements Screen {
    public static ArrayList<Stage> stages = new ArrayList<>();
    private int selectedStageIndex;

    private Character character1;
    private Character character2;
    private int costume1;
    private int costume2;

    private static Stage hell;
    private static Stage god;

    public static void load() {
        stages.add(new LastLocation());
        stages.add(new Warzone());
        stages.add(new ThumbabaLair());
        stages.add(new Desert());
        stages.add(new OurResort());
        stages.add(new COBS());
        stages.addAll(ModReader.getmodStages());
        hell = new Hell();
        god = new God();

    }

    public StageSelectScreen(Character character1, Character character2, int costume1, int costume2) {
        selectedStageIndex = 0;

        this.character1 = character1;
        this.character2 = character2;
        this.costume1 = costume1;
        this.costume2 = costume2;
    }

    private void drawText(Vector pos, float size, String text, Color color, Graphics g, boolean lockedCenter) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(color);
        g2d.setFont(Fonts.getSABFont());
        g2d.setFont(g2d.getFont().deriveFont(size));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle((int) pos.x, (int) pos.y, metrics.stringWidth(text), (int) metrics.getHeight());
        int x;
        int y;
        if (lockedCenter) {
            x = rect.x - rect.width / 2;
            y = rect.y + rect.height / 2;
        } else {
            x = rect.x - (text.length() - 2) * 24;
            y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        }
        g2d.drawString(text, x, y);
    }

    @Override
    public Screen keyTyped(KeyEvent event) {
        return this;
    }

    @Override
    public Screen keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            selectedStageIndex--;
            selectedStageIndex = selectedStageIndex < 0 ? stages.size() - 1 : selectedStageIndex;
        }

        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            selectedStageIndex++;
            selectedStageIndex = selectedStageIndex > stages.size() - 1 ? 0 : selectedStageIndex;
        }

        if (keyCode == KeyEvent.VK_H) {
            if (!stages.contains(hell)) {
                stages.add(hell);
            }
        }

        if (keyCode == KeyEvent.VK_G) {
            if (!stages.contains(god)) {
                stages.add(god);
            }
        }

        if (keyCode == KeyEvent.VK_ENTER) {
            Stage stage = stages.get(selectedStageIndex).copy();
            BattleScreen battleScreen = new BattleScreen(character1, character2, stage,
                    costume1, costume2);
            return battleScreen;
        }

        return this;
    }

    @Override
    public Screen keyReleased(KeyEvent event) {
        return this;
    }

    @Override
    public Screen update() {
        return this;
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        g.drawImage(Images.getImage(stages.get(selectedStageIndex).getBackground()), 0, 0, target);
        stages.get(selectedStageIndex).selectScreenRender(g, target);
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(0, 0, 1152, 704);
        drawText(new Vector(1152 / 2, 128), 20, stages.get(selectedStageIndex).getName(), Color.WHITE, g, true);
        drawText(new Vector(1152 / 2, 640), 12, stages.get(selectedStageIndex).getMusicCredit(), Color.WHITE, g, true);
    }
}