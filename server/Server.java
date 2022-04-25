package server;

import game.Settings;
import game.Window;
import game.screen.Screen;
import game.screen.CharacterSelectScreen;
import net.DeepCopy;
import net.SABConnection;

import java.util.ConcurrentModificationException;

import java.io.*;
import java.net.*;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Server {
    private Window window;
    private ServerSocket serverSocket;
    private SABConnection[] connections;

    private KeyEvent convertKeyCode(int id, KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (id == 0) {
            if (keyCode == KeyEvent.VK_UP) {
                event.setKeyCode(KeyEvent.VK_W);
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                event.setKeyCode(KeyEvent.VK_S);
            }
            if (keyCode == KeyEvent.VK_LEFT) {
                event.setKeyCode(KeyEvent.VK_A);
            }
            if (keyCode == KeyEvent.VK_RIGHT) {
                event.setKeyCode(KeyEvent.VK_D);
            }

            if (keyCode == KeyEvent.VK_M) {
                event.setKeyCode(KeyEvent.VK_F);
            }

            if (keyCode == KeyEvent.VK_B) {
                event.setKeyCode(KeyEvent.VK_Z);
            }
        } else if (id == 1) {
            if (keyCode == KeyEvent.VK_W) {
                event.setKeyCode(KeyEvent.VK_UP);
            }
            if (keyCode == KeyEvent.VK_S) {
                event.setKeyCode(KeyEvent.VK_DOWN);
            }
            if (keyCode == KeyEvent.VK_A) {
                event.setKeyCode(KeyEvent.VK_LEFT);
            }
            if (keyCode == KeyEvent.VK_D) {
                event.setKeyCode(KeyEvent.VK_RIGHT);
            }

            if (keyCode == KeyEvent.VK_F || keyCode == KeyEvent.VK_Q) {
                event.setKeyCode(KeyEvent.VK_M);
            }

            if (keyCode == KeyEvent.VK_Z) {
                event.setKeyCode(KeyEvent.VK_B);
            }
        }

        return event;
    }

    public Server(int port) {
        window = createWindow();
        connections = new SABConnection[2];

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for players...");

            for (int i = 0; i < connections.length; i++) {
                Socket socket = serverSocket.accept();
                connections[i] = new SABConnection(socket);
                SABConnection connection = connections[i];
                final int id = i;

                new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            Object o = connection.receive();

                            if (o instanceof KeyEvent) {
                                KeyEvent event = (KeyEvent) o;

                                if (event.getID() == KeyEvent.KEY_PRESSED) {
                                    event = convertKeyCode(id, event);
                                    window.keyPressed(event);
                                } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                                    event = convertKeyCode(id, event);
                                    window.keyReleased(event);
                                }
                            }
                        }
                    }
                }.start();
            }

            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Screen s = (Screen) DeepCopy.deepCopy(window.getScreen());

                            for (SABConnection connection : connections) {
                                connection.send(s);
                            }
                        } catch (ConcurrentModificationException e) {

                        }

                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }.start();

            System.out.println("Game started");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Window createWindow() {
        // Create a JFrame with a title and make it stop running when closed.
        JFrame frame = new JFrame("Super Ass Brothers Server");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("assets/images/server_icon.png");
        frame.setIconImage(icon.getImage());

        Window window = new Window(1152, 704, new CharacterSelectScreen());
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

        return window;
    }

    public static void main(String[] args) {
        new Server(Settings.port());
    }
}