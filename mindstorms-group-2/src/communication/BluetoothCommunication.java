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

    private static final String LIFT = "Lift";
    private static final String TURN_TABLE = "TurnTable";
    private static final String GATE = "TestName";

    private static BTConnection connection;

    public static LiftConnection connectToLift(CancelUpdater configuration) {
        while (!openConnection(LIFT) && !configuration.isCancel()) {
            Delay.msDelay(1000); // waiting for free connection
        }

        return new LiftConnection(connection);
    }

    public static TurnTableConnection connectToTurnTable(CancelUpdater configuration) {
        while (!openConnection(TURN_TABLE) && !configuration.isCancel()) {
            Delay.msDelay(1000); // waiting for free connection
        }

        return new TurnTableConnection(connection);
    }

    public static GateConnection connectToGate(CancelUpdater configuration) {
        while (!openConnection(GATE) && !configuration.isCancel()) {
            Delay.msDelay(1000); // waiting for free connection
        }

        return new GateConnection(connection);
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
