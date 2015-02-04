package communication;

import lejos.nxt.comm.BTConnection;

public class GateConnection extends BluetoothConnection {

    public GateConnection(BTConnection connection) {
        super(connection);
    }

    public void passed() {
        System.out.println("Sended passing signal");
        writeBoolean(true);
    }

    public boolean waitForSuccess() {
        return readBoolean();
    }

}
