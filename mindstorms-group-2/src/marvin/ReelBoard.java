package marvin;

import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.GateConnection;

public class ReelBoard implements Step {

    @Override
    public void run(Configuration configuration) {
        configuration.getMovementPrimitives().stop();
        configuration.getLeftWheel().rotate(-200, true);
        configuration.getRightWheel().rotate(-200, true);
        configuration.getLeftWheel().waitComplete();
        configuration.getRightWheel().waitComplete();
        try (GateConnection gate = BluetoothCommunication.connectToGate(configuration)) {
            configuration.getMovementPrimitives().fullSpeed();
            configuration.getMovementPrimitives().drive();
            Delay.msDelay(5000);
            configuration.getMovementPrimitives().stop();
            gate.passed();
            while (!gate.waitForSuccess()) {
                Delay.msDelay(50);
            }
            System.out.println("Successful passed");
        }

    }

    @Override
    public String getName() {
        return "ReelBoard";
    }

}
