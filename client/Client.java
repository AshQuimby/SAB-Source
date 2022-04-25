package client;

import game.Window;
import game.screen.LoadingScreen;
import game.screen.Screen;

import net.SABConnection;

import java.io.*;
import java.net.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Client {
    private Window window;
    private SABConnection connection;

    public Client(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            connection = new SABConnection(socket);
            System.out.println("Connected to server");

            window = createWindow();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Window createWindow() {
        // Create a JFrame with a title and make it stop running when closed.
        JFrame frame = new JFrame("Super Ass Brothers Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("assets/images/server_icon.png");
        frame.setIconImage(icon.getImage());
        // Create the window and attach it to the frame.
        Window window = new Window(1152, 704);
        window.setScreen(new LoadingScreen());
        frame.add(window);

        // Fit the frame to the window.
        frame.pack();

        // don't allow the user to resize the frame.
        frame.setResizable(false);

        // Pass keyboard input to the window.
        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent event) {
                connection.send(event);
            }

            @Override
            public void keyPressed(KeyEvent event) {
                connection.send(event);
            }

            @Override
            public void keyReleased(KeyEvent event) {
                connection.send(event);
            }
        });

        // Open the window in the center of the screen and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return window;
    }

    public static void main(String[] args) {
        String address = args[0];
        int port = Integer.parseInt(args[1]);

        Client client = new Client(address, port);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    client.window.getScreen().update();
                }
            }
        }.start();

        while (true) {
            Object o = client.connection.receive();

            if (o instanceof Screen) {
                client.window.setScreen((Screen) o);
            }
        }
    }
}