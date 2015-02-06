package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.TurnTableConnection;

public class TurnTable extends FollowLine {

    @Override
    public void run(Configuration configuration) {
        MovementPrimitives movement = configuration.getMovementPrimitives();
        movement.stop();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
        LightSensor light = configuration.getLight();
        // TODO align at barcode
        leftWheel.rotate(80, true);
        rightWheel.rotate(80, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        sensorDataCollector.turnToRightMaximum();
        rightWheel.backward();
        while (!configuration.isCancel() && rightWheel.isMoving()) {
            if (sensorDataCollector.isBright(light.getNormalizedLightValue())) {
                rightWheel.stop();
                break;
            }
        }
        sensorDataCollector.turnToLeftMaximum();
        leftWheel.backward();
        while (!configuration.isCancel() && leftWheel.isMoving()) {
            if (sensorDataCollector.isBright(light.getNormalizedLightValue())) {
                leftWheel.stop();
                break;
            }
        }
        try (TurnTableConnection turnTable = BluetoothCommunication.connectToTurnTable(configuration)) {
            while (!turnTable.hello()) {
                Delay.msDelay(1000);
            }

            leftWheel.rotate(40);
            movement.drive();

            while (!configuration.isCancel()) {
                if (sensorDataCollector.isBright(light.getNormalizedLightValue())) {
                    movement.stop();
                    break;
                }
            }

            movement.drive();

            // follow a line to drive into
            while (!configuration.isCancel() && !detectEnd(configuration)) {
                super.run(configuration);
            }

            turnTable.turn();
            while (!turnTable.done()) {
                Delay.msDelay(1000);
            }

            // drive out
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
