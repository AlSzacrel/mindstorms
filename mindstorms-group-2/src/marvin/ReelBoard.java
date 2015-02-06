package marvin;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.GateConnection;

public class ReelBoard implements Step {

    @Override
    public void run(Configuration configuration) {
        configuration.getMovementPrimitives().stop();
        configuration.getMovementPrimitives().reelSpeed();
        configuration.getMovementPrimitives().resetSpeed();
        configuration.getSensorDataCollector().turnToWallPosition();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        try (GateConnection gate = BluetoothCommunication.connectToGate(configuration)) {
            leftWheel.rotate(-3200, true);
            rightWheel.rotate(-3200, true);

            // TODO watch distance
            leftWheel.waitComplete();
            rightWheel.waitComplete();
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
