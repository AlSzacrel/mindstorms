package marvin;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.TurnTableConnection;

public class TurnTable extends FollowLine {

    @Override
    public void run(Configuration configuration) {
        try (TurnTableConnection turnTable = BluetoothCommunication.connectToTurnTable(configuration)) {
            while (!turnTable.hello()) {
                Delay.msDelay(1000);
            }

            // follow a line to drive into
            while (!configuration.isCancel() && !detectEnd(configuration)) {
                super.run(configuration);
            }

            turnTable.turn();
            while (!turnTable.done()) {
                Delay.msDelay(1000);
            }

            // drive out
            NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
            NXTRegulatedMotor rightWheel = configuration.getRightWheel();
            leftWheel.rotate(-800, true);
            rightWheel.rotate(-800, true);
            leftWheel.waitComplete();
            rightWheel.waitComplete();
            leftWheel.rotate(-400, true);
            rightWheel.rotate(400, true);
            leftWheel.waitComplete();
            rightWheel.waitComplete();
        }
    }

    @Override
    protected boolean detectEnd(Configuration configuration) {
        TouchSensor leftTouchSensor = configuration.getLeftTouchSensor();
        TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
        return leftTouchSensor.isPressed() && rightTouchSensor.isPressed();
    }

    @Override
    public String getName() {
        return "TurnTable";
    }

}
