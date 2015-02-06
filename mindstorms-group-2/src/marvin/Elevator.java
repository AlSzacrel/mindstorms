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
        TouchSensor leftTouchSensor = configuration.getLeftTouchSensor();
        TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        RConsole.println("Elevator step started");

        // TODO align in drive direction
        rightWheel.rotate(120);
        rightWheel.waitComplete();

        leftWheel.rotate(400, true);
        rightWheel.rotate(400, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();

        leftWheel.rotate(120);
        leftWheel.waitComplete();

        // Connect to bluetooth
        try (LiftConnection lift = BluetoothCommunication.connectToLift(configuration)) {
            System.out.println("Connection established");
            while (!configuration.isCancel() && !lift.canDriveIn()) {
                System.out.println("Cannot drive in");
                Delay.msDelay(200);
            }
            if (configuration.isCancel()) {
                return;
            }

            // TODO drive into elevator
            System.out.println("Start to drive");
            movement.drive();
            while (!configuration.isCancel()) {
                lift.canExit();
                // TODO detect stop, use distance sensor
                if (leftTouchSensor.isPressed() && rightTouchSensor.isPressed()) {
                    System.out.println("left right touched");
                    movement.stop();
                    Delay.msDelay(100);
                    break;
                }
                if (leftTouchSensor.isPressed()) {
                    System.out.println("left touch");
                    movement.stop();
                    leftWheel.rotate(-80, true);
                    rightWheel.rotate(-80, true);
                    leftWheel.waitComplete();
                    rightWheel.waitComplete();
                    rightWheel.rotate(-40);
                    movement.drive();
                }
                if (rightTouchSensor.isPressed()) {
                    System.out.println("right touch");
                    movement.stop();
                    leftWheel.rotate(-80, true);
                    rightWheel.rotate(-80, true);
                    leftWheel.waitComplete();
                    rightWheel.waitComplete();
                    leftWheel.rotate(-40);
                    movement.drive();
                }
                Delay.msDelay(50);
            }
            System.out.println("go down");
            lift.goDown();
            Delay.msDelay(100);
            while (!lift.canExit()) {
                Delay.msDelay(100);
            }

            // TODO make sure Marvin gets out of the elevator
            movement.drive();
            if (leftTouchSensor.isPressed()) {
                movement.stop();
                leftWheel.rotate(-20, true);
                rightWheel.rotate(-20, true);
                leftWheel.waitComplete();
                rightWheel.waitComplete();
                rightWheel.rotate(-20);
                movement.drive();
            }
            if (rightTouchSensor.isPressed()) {
                movement.stop();
                leftWheel.rotate(-20, true);
                rightWheel.rotate(-20, true);
                leftWheel.waitComplete();
                rightWheel.waitComplete();
                leftWheel.rotate(-20);
                movement.drive();
            }
            Delay.msDelay(4000);
            movement.stop();
            configuration.nextStep();
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

    @Override
    public String getName() {
        return "Elevator";
    }

}