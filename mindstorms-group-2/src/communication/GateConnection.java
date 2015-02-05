package communication;

import lejos.nxt.comm.BTConnection;

public class GateConnection extends BluetoothConnection {

    public GateConnection(BTConnection connection) {
        super(connection);
    }

    public void passed() {
        writeBoolean(true);
    }

    public boolean waitForSuccess() {
        return readBoolean();
    }

}
