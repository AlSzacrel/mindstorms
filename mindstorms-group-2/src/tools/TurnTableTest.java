package tools;

import lejos.util.Delay;
import communication.BluetoothCommunication;
import communication.TurnTableConnection;

public class TurnTableTest {

    public static void main(String[] args) {
        TurnTableConnection turnTable = BluetoothCommunication.connectToTurnTable();
        while (!turnTable.hello()) {
            Delay.msDelay(1000);
        }
        turnTable.turn();

        while (!turnTable.done()) {
            Delay.msDelay(1000);
        }

        turnTable.cya();
        turnTable.closeConnection();
    }

}