package marvin;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;

import communication.BluetoothConnection;

public class TurnTableConnection extends BluetoothConnection {

    public TurnTableConnection(BTConnection connection) {
        super(connection);
    }

    private enum TurnTableCommand {
        HELLO, TURN, DONE, CYA, UNKNOWN;

        public static TurnTableCommand getByOrdinal(int commandOrdinal) {
            if (commandOrdinal >= values().length) {
                return UNKNOWN;
            }
            return values()[commandOrdinal];
        }
    }

    public boolean hello() {
        TurnTableCommand command = TurnTableCommand.getByOrdinal(readInt());
        if (command == TurnTableCommand.HELLO) {
            LCD.drawString("HELLO", 0, 1);
            return true;
        }
        LCD.drawString("NO HELLO", 0, 1);
        return false;
    }

    public void turn() {
        writeInt(TurnTableCommand.TURN.ordinal());
    }

    public boolean done() {
        TurnTableCommand command = TurnTableCommand.getByOrdinal(readInt());
        if (command == TurnTableCommand.DONE) {
            LCD.drawString("TURN", 0, 1);
            return true;
        }
        LCD.drawString("NO TURN", 0, 1);
        return false;
    }

    public void cya() {
        writeInt(TurnTableCommand.CYA.ordinal());
    }
}