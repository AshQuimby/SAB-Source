package local;

import game.Window;
import game.screen.TitleScreen;

import javax.swing.*;

class LocalGame {
    private static void createWindow() {
        // Create a JFrame with a title and make it stop running when closed.
        JFrame frame = new JFrame("Super Ass Brothers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("assets/images/icon.png");
        frame.setIconImage(icon.getImage());

        // Create the window and attach it to the frame.
        // Board board = new Board();
        Window window = new Window(1152, 704, new TitleScreen());
        frame.add(window);

        // Pass keyboard input to the window.
        frame.addKeyListener(window);

        // Fit the frame to the window.
        frame.pack();

        // don't allow the user to resize the frame.
        frame.setResizable(false);

        // Open the window in the center of the screen and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // invokeLater() is used here to prevent our graphics processing from
        // blocking the GUI. https://stackoverflow.com/a/22534931/4655368
        // this is a lot of boilerplate code that you shouldn't be too concerned about.
        // just know that when main runs it will call initWindow() once.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createWindow();
            }
        });
    }
}