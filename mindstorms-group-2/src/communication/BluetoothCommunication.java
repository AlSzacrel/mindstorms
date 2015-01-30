package communication;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;
import marvin.TurnTableConnection;

/**
 * Example stuff to communicate with a server, in this example with the lift
 *
 * @author The Coding-Team
 *
 */
public class BluetoothCommunication {

    private static final String LIFT = "Lift";
    private static final String TURN_TABLE = "TurnTable";

    private static BTConnection connection;

    /**
     * just a main function...
     *
     * @param args
     *            you know what this is for (at least i hope so)
     */
    public static void main(String args[]) {
        LiftConnection lift = connectToLift();
        lift.goDown();

        LCD.drawString("Going down", 0, 1);

        while (!lift.canExit()) {
            LCD.drawString("Can exit: No", 0, 2);
            Delay.msDelay(100);
        }
        LCD.drawString("Can exit: Yes", 0, 2);

        lift.closeConnection();
    }

    public static LiftConnection connectToLift() {
        while (!openConnection(LIFT)) {
            Delay.msDelay(1000); // waiting for free connection
        }

        return new LiftConnection(connection);
    }

    public static TurnTableConnection connectToTurnTable() {
        while (!openConnection(TURN_TABLE)) {
            Delay.msDelay(1000); // waiting for free connection
        }

        return new TurnTableConnection(connection);
    }

    /**
     * opens a connection to a server
     *
     * @param server
     *            name of the server (hope you already paired your device with
     *            the server)
     * @return if the connection could be established or not
     */
    private static boolean openConnection(String server) {
        RemoteDevice btrd = Bluetooth.getKnownDevice(server);

        if (btrd == null) {
            // no such device, you should pair your devices first or check the
            // Devices name
            return false;
        }

        connection = Bluetooth.connect(btrd);

        if (connection == null) {
            // connection failed, try again...
            return false;
        }

        return true;
    }
}
