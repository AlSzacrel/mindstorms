package tools;

import lejos.nxt.Button;
import lejos.util.Delay;
import marvin.CancelUpdater;

import communication.BluetoothCommunication;
import communication.GateConnection;

public class GateTest {

    public static void main(String[] args) {

        System.out.println("Calling gate");
        try (GateConnection gate = BluetoothCommunication.connectToGate(new CancelUpdater() {

            @Override
            public boolean isCancel() {
                return Button.ESCAPE.isDown();
            }
        })) {
            System.out.println("Connected to the gate.");

            // Now the gate opens & a timer of 20 seconds starts
            // in this time the robot has to drive through & send a "I passed"
            // signal

            Delay.msDelay(2000);

            // Robot drives through the gate
            System.out.println("Driving through.");
            Delay.msDelay(5000);

            gate.passed();
            System.out.println("Sended passing signal");
            while (!gate.waitForSuccess()) {
                Delay.msDelay(50);
            }
            System.out.println("Successful passed");
        }
        Button.waitForAnyPress();
    }
}