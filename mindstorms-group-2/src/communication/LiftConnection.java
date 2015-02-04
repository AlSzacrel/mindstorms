package communication;

import lejos.nxt.comm.BTConnection;

public class LiftConnection extends BluetoothConnection {

    private static final int GO_DOWN = 0;
    private static final int IS_DOWN = 1;
    private static final int CYA = 2;
    private static final int DRIVE_IN = 3;

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
        return readBoolean();
    }

    /**
     * returns if you can exit the lift
     *
     * @return if the lift is on the bottom
     */
    public boolean canExit() {
        writeInt(IS_DOWN);
        return readBoolean();
    }

    public boolean canDriveIn() {
        writeInt(DRIVE_IN);
        return readBoolean();
    }

    @Override
    public void close() {
        writeInt(CYA);
        super.close();
    }

}
