package net;

import java.io.*;
import java.net.*;

public class SABConnection {
    private Socket socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SABConnection(Socket socket) {
        this.socket = socket;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void send(Object o) {
        try {
            out.writeObject(o);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Object receive() {
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}