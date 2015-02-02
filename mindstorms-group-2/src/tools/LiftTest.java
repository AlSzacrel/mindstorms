package tools;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;
import marvin.CancelUpdater;

import communication.BluetoothCommunication;
import communication.LiftConnection;

/**
 * Example stuff to communicate with a server, in this example with the lift
 *
 * @author The Coding-Team
 *
 */
public class LiftTest {

    /**
     * just a main function...
     *
     * @param args
     *            you know what this is for (at least i hope so)
     */
    public static void main(String args[]) {
        LCD.drawString("Started", 0, 0);
        try (LiftConnection lift = BluetoothCommunication.connectToLift(new CancelUpdater() {

            @Override
            public boolean isCancel() {
                return Button.ESCAPE.isDown();
            }
        })) {
            lift.goDown();

            LCD.drawString("Going down", 0, 1);

            while (!lift.canExit()) {
                LCD.drawString("Can exit: No", 0, 2);
                Delay.msDelay(100);

            }
            LCD.drawString("Can exit: Yes", 0, 2);
        }
    }
}
