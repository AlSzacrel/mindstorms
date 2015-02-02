package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;

public class BluetoothConnection {

    private static final int CLOSE_CONNECTION = 2;
    private final BTConnection connection;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    public BluetoothConnection(BTConnection connection) {
        super();
        this.connection = connection;
        // LCD.clear();
        LCD.drawString("Connected", 0, 0);
        dis = connection.openDataInputStream();
        dos = connection.openDataOutputStream();
    }

    /**
     * method for sending an integer to the lift
     *
     * @param value
     *            integer to send
     */
    protected void writeInt(int value) {
        try {
            dos.writeInt(value);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to read a boolean variable
     *
     * @return answer of the lift
     */
    protected boolean readBool() {
        boolean value = false;
        try {
            value = dis.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    protected Integer readInt() {
        Integer value = Integer.MAX_VALUE;
        try {
            value = dis.readInt();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    /**
     * this method's name should be self explaining
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                writeInt(CLOSE_CONNECTION);
                connection.close();
                dis.close();
                dos.close();
            } catch (IOException e) {
                // ignore
            }
        }
        LCD.drawString("Disconnected", 0, 0);
    }

}