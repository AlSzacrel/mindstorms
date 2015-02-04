package marvin;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.LiftConnection;

public class Elevator implements Step {

    // rot [435,447]
    // grüne [400,423]
    // Colors changed. Red is now darker.
    private static final int THRESHOLD = 450;

    @Override
    public void run(Configuration configuration) {
        MovementPrimitives movement = configuration.getMovementPrimitives();
        configuration.getLight().setFloodlight(false);
        movement.slow();
        movement.stop();
        RConsole.println("Elevator step started");

        // TODO align in drive direction

        // Connect to bluetooth
        try (LiftConnection lift = BluetoothCommunication.connectToLift(configuration)) {
            RConsole.println("Connection established");
            while (!configuration.isCancel()) {
                RConsole.println("Collecting data");
                DataSet sensorData = configuration.getSensorDataCollector().collectDataRow();
                if (sensorData.size() == 0) {
                    return;
                }
                float color = color(sensorData);
                if (isWhite(color)) {
                    RConsole.println("Found white");
                    break;
                }
                Delay.msDelay(200);
            }

            // TODO drive into elevator
            movement.drive();
            TouchSensor leftTouchSensor = configuration.getLeftTouchSensor();
            TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
            NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
            NXTRegulatedMotor rightWheel = configuration.getRightWheel();
            while (!configuration.isCancel()) {
                // TODO detect stop, use distance sensor
                if (leftTouchSensor.isPressed() && rightTouchSensor.isPressed()) {
                    movement.stop();
                    Delay.msDelay(100);
                    break;
                }
                if (leftTouchSensor.isPressed()) {
                    movement.stop();
                    rightWheel.rotate(-20);
                }
                if (rightTouchSensor.isPressed()) {
                    movement.stop();
                    leftWheel.rotate(-20);
                }
            }
            lift.goDown();
            Delay.msDelay(100);
            while (!lift.canExit()) {
                Delay.msDelay(1000);
            }

            // TODO make sure Marvin gets out of the elevator
            leftWheel.rotate(600, true);
            rightWheel.rotate(600, true);
            leftWheel.waitComplete();
            rightWheel.waitComplete();
            movement.stop();
        } finally {
            configuration.getLight().setFloodlight(true);
        }
    }

    private float color(DataSet sensorData) {
        float colorAVG = sensorData.get(sensorData.size() - 1).getLightValue();
        // TODO adjust once also scanning from right to left
        for (int i = 0; i < sensorData.size(); i++) {
            colorAVG = Filter.avgEWMA(colorAVG, sensorData.get(i).getLightValue());
        }
        return colorAVG;
    }

    private boolean isWhite(float color) {
        return color > THRESHOLD;
    }

}