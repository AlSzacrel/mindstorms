package tools;

import lejos.nxt.Button;
import lejos.util.Delay;
import marvin.CancelUpdater;

import communication.BluetoothCommunication;
import communication.TurnTableConnection;

public class TurnTableTest {

    public static void main(String[] args) {
        try (TurnTableConnection turnTable = BluetoothCommunication.connectToTurnTable(new CancelUpdater() {

            @Override
            public boolean isCancel() {
                return Button.ESCAPE.isDown();
            }
        })) {
            while (!turnTable.hello()) {
                Delay.msDelay(1000);
            }
            turnTable.turn();

            while (!turnTable.done()) {
                Delay.msDelay(1000);
            }
        }
    }
}