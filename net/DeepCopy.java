package net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopy {
    /**
     * Creates a deep copy of an object.
     * 
     * @param o the object to copy
     * @return a deep copy of the object
     */
    public static Object deepCopy(Object o) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(o);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            Object deepCopy = objectIn.readObject();

            objectOut.close();
            objectIn.close();
            byteOut.close();
            byteIn.close();

            return deepCopy;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

}