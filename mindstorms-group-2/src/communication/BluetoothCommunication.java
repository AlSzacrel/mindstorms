package communication;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;
import marvin.CancelUpdater;

/**
 * Example stuff to communicate with a server, in this example with the lift
 *
 * @author The Coding-Team
 *
 */
public class BluetoothCommunication {

    private static final String LIFT_NAME = "Lift";
    private static final String LIFT_ADDRESS = "00165309448C";
    private static final String TURN_TABLE_NAME = "TurnTable";
    private static final String TURN_TABLE_ADDRESS = "00165306B259";
    private static final String GATE_NAME = "TestName";
    private static final String GATE_ADDRESS = "00165304779A";

    private static BTConnection connection;

    public static LiftConnection connectToLift(CancelUpdater configuration) {
        while (!openConnection(LIFT_NAME, LIFT_ADDRESS) && !configuration.isCancel()) {
            Delay.msDelay(1000); // waiting for free connection
            System.out.println("Connect to lift");
        }

        return new LiftConnection(connection);
    }

    public static TurnTableConnection connectToTurnTable(CancelUpdater configuration) {
        while (!openConnection(TURN_TABLE_NAME, TURN_TABLE_ADDRESS) && !configuration.isCancel()) {
            Delay.msDelay(1000); // waiting for free connection
            System.out.println("Connect to turn table");
        }

        return new TurnTableConnection(connection);
    }

    public static GateConnection connectToGate(CancelUpdater configuration) {
        while (!openConnection(GATE_NAME, GATE_ADDRESS) && !configuration.isCancel()) {
            Delay.msDelay(1000); // waiting for free connection
            System.out.println("Connect to gate");
        }

        return new GateConnection(connection);
    }

    /**
     * opens a connection to a server
     *
     * @param server
     *            name of the server (hope you already paired your device with
     *            the server)
     * @param name
     * @param address
     * @return if the connection could be established or not
     */
    private static boolean openConnection(String name, String address) {
        RemoteDevice remoteDevice = new RemoteDevice(name, address, 0);

        connection = Bluetooth.connect(remoteDevice);

        if (connection == null) {
            // connection failed, try again...
            return false;
        }

        return true;
    }
}
