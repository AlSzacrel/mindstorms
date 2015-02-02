package tools;

import communication.BluetoothCommunication;
import communication.LiftConnection;

import lejos.nxt.LCD;
import lejos.util.Delay;

/**
 * Example stuff to communicate with a server, in this example with the lift
 *
 * @author The Coding-Team
 *
 */
public class BluetoothTest {

    /**
     * just a main function...
     *
     * @param args
     *            you know what this is for (at least i hope so)
     */
    public static void main(String args[]) {
        LCD.drawString("Started", 0, 0);
        LiftConnection lift = BluetoothCommunication.connectToLift();
        lift.goDown();

        LCD.drawString("Going down", 0, 1);

        while (!lift.canExit()) {
            LCD.drawString("Can exit: No", 0, 2);
            Delay.msDelay(100);

        }
        LCD.drawString("Can exit: Yes", 0, 2);

        lift.closeConnection();
    }
}
