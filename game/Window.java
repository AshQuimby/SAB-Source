package game;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.event.*;
import javax.swing.*;
import game.screen.*;
import modloader.*;

public class Window extends JPanel implements ActionListener, KeyListener {
    // The delay between ticks, in milliseconds.
    private static final int TICK_LENGTH_MILLISECONDS = 25;
    public static Screen preferredSelectScreen;
    // The window's current screen.
    private Screen screen;

    // Timer to control game ticks.
    private Timer timer;

    public Window(int width, int height, Screen screen) {
        try {
            Images.load();
            SoundEngine.load();
            ModReader.load();
        } catch (Exception e) {
        }
        StageSelectScreen.load();
        Stages.load();
        CharacterSelectScreen.loadModCharacters();
        Settings.load("../options.sabo");
        setPreferredSize(new Dimension(width, height));
        preferredSelectScreen = screen;
        this.screen = screen;

        timer = new Timer(TICK_LENGTH_MILLISECONDS, this);
        timer.start();
    }

    public Window(int width, int height) {
        this(width, height, null);

        // Initialize a screen that does nothing.
        Screen defaultScreen = new Screen() {
            @Override
            public Screen keyTyped(KeyEvent event) {
                return this;
            }

            @Override
            public Screen keyPressed(KeyEvent event) {
                return this;
            }

            @Override
            public Screen keyReleased(KeyEvent event) {
                return this;
            }

            @Override
            public void render(Graphics g, ImageObserver target) {

            }

            @Override
            public Screen update() {
                return this;
            }
        };

        setScreen(defaultScreen);

        timer = new Timer(TICK_LENGTH_MILLISECONDS, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // This method is called every tick by the timer before rendering.
        screen = screen.update();

        // Redraw the screen.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        screen.render(g, this);

        // This smooths out animations on some systems.
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        screen = screen.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        screen = screen.keyReleased(e);
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}