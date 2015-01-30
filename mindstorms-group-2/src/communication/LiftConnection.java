package communication;

import lejos.nxt.comm.BTConnection;

public class LiftConnection extends BluetoothConnection {

    private static final int GO_DOWN = 0;
    private static final int IS_DOWN = 1;
    private static final int CYA = 2;

    public LiftConnection(BTConnection connection) {
        super(connection);
    }

    /**
     * moves the lift down
     *
     * @return true
     */
    public boolean goDown() {
        writeInt(GO_DOWN);
        return readBool();
    }

    /**
     * returns if you can exit the lift
     *
     * @return if the lift is on the bottom
     */
    public boolean canExit() {
        writeInt(IS_DOWN);
        return readBool();
    }

    @Override
    public void close() {
        writeInt(CYA);
        super.close();
    }

}
