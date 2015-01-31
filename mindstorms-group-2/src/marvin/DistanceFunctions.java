package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class DistanceFunctions implements Step {

    /*
     * Perceptable Data Range: [3;31] 3 is hend in front of sensor meassured
     * values up to 60, but not on the stupid plane!!! Set head back on 28.01.
     * ~17h Before that: distance to wall when touching: 9~10
     */
    private static final int SIDE_WALL_THRESH = 30;
    private static final int FRONT_WALL_THRESH = 14; // average 24.2
    private static final int FOLLOW_WALL_LOW_THRESH = 18;
    private static final int FOLLOW_WALL_HIGH_THRESH = 22;
    private static final int SIDE_EDGE_THRESH = 35; // Können wir das überhaupt
    // noch wahrnehmen?
    private static final int FRONT_EDGE_THRESH = 35;

    // new constants
    private static final int SIDE_WALL_THRESHOLD = 30;
    private static final int SEARCH_THRESHOLD = 50;
    private static final int GAIN = 15;
    private static final int MAX_CORRECTION = 100;
    private static final int MIN_CORRECTION = -MAX_CORRECTION;
    private static final int WALL_DETECTION_COUNT = 40;
    private static final int MAX_WALL_DETECTION_VALUES = 10;
    private static final int FRONT_DETECTION_THRESHOLD = 32;

    private MovementPrimitives movement;
    private DataSet data;
    private int leftDistance;
    private int rightDistance;
    private int centerDistance;
    private final boolean lastTurnCorrectionLeft = false;
    private final boolean lastTurnCorrectionRight = false;
    private int scanNumber = 0;

    @Override
    public void run(Configuration configuration) {
        movement = configuration.getMovementPrimitives();
        // TODO initially search wall
        SensorDataCollector dataCollector = configuration.getSensorDataCollector();
        data = dataCollector.collectDataRow();
        if (data.isEmpty()) {
            return;
        }
        if (data.size() < 3) {
            return;
        }
        leftDistance = data.get(0).getDistance();
        rightDistance = data.get(data.size() - 1).getDistance();
        centerDistance = data.get(data.size() / 2).getDistance();

        // TODO change direction when distance to wall decreases
        TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        LightSensor light = configuration.getLight();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        while (!configuration.isCancel()) {
            normalCorrection(movement, ultraSonic);
            detectWall(movement, rightTouchSensor);
            if (detectBarcode(light)) {
                // TODO switch to next step in configuration
            }
            scanNumber++;
        }
    }

    private boolean detectBarcode(LightSensor light) {
        // TODO detect line borders and count them. There must be 3 from dark to
        // bright and 3 from bright to dark
        // TODO use similar mechanism as in FollowLine
        return false;
    }

    private void detectWall(MovementPrimitives movement, TouchSensor rightTouchSensor) {
        if (!rightTouchSensor.isPressed()) {
            return;
        }
        movement.stop();
        movement.backup();
        Delay.msDelay(1000);
        movement.spinLeft();
        Delay.msDelay(600);
        movement.stop();
        movement.drive();
    }

    private void normalCorrection(MovementPrimitives movement, UltrasonicSensor ultraSonic) {
        int distance = ultraSonic.getDistance();
        if (distance > SEARCH_THRESHOLD) {
            searchWall(movement);
        }
        int correctionFactor = SIDE_WALL_THRESHOLD - distance;
        int gainedCorrection = correctionFactor * GAIN;
        int limittedCorrection = Math.max(gainedCorrection, MIN_CORRECTION);
        limittedCorrection = Math.min(limittedCorrection, MAX_CORRECTION);
        movement.correct(limittedCorrection);
        RConsole.println("d: " + distance + " f: " + correctionFactor + " g: " + gainedCorrection + " l: "
                + limittedCorrection);
    }

    private void searchWall(MovementPrimitives movement) {
        movement.backup();
    }

    // needs to be called from the outside
    // why?
    private void followLeftWall() {
        if (!isWallLeft()) {
            movement.turnLeft();
            Delay.msDelay(500);
            movement.slow();
            movement.drive();
            // lastTurnCorrectionLeft = true;
            // Sound.beep();
        } else if (leftDistance > FOLLOW_WALL_HIGH_THRESH) {
            movement.turnLeft();
            Delay.msDelay(500);
            movement.turnRight();
            Delay.msDelay(500);
            movement.slow();
            movement.drive();
            // lastTurnCorrectionLeft = true;
            // Sound.beep();
            // Delay.msDelay(100);
            // Sound.beep();
        } else if (leftDistance < FOLLOW_WALL_LOW_THRESH) {
            movement.turnRight();
            Delay.msDelay(500);
            movement.turnLeft();
            Delay.msDelay(500);
            movement.slow();
            movement.drive();
            // lastTurnCorrectionRight = true;
            // Sound.beep();
            // Delay.msDelay(100);
            // Sound.beep();
            // Delay.msDelay(100);
            // Sound.beep();
        } else {
            movement.slow();
            movement.drive();
            // Sound.beep();
            // Delay.msDelay(100);
            // Sound.beep();
            // Delay.msDelay(100);
            // Sound.beep();
            // Delay.msDelay(100);
            // Sound.beep();
        }
    }

    private boolean isWallLeft() {
        if (leftDistance < SIDE_WALL_THRESH) {
            return true;
        }
        return false;
    }

    private boolean isWallRight() {
        if (rightDistance < SIDE_WALL_THRESH) {
            return true;
        }
        return false;

    }

    private boolean isWallUpFront() {
        if (centerDistance < FRONT_WALL_THRESH) {
            return true;
        }
        return false;

    }

    private boolean isEdgeLeft() {
        if (leftDistance < SIDE_EDGE_THRESH && leftDistance > SIDE_WALL_THRESH) {
            return true;
        }
        return false;
    }

    private boolean isEdgeRight() {
        if (rightDistance < SIDE_EDGE_THRESH && rightDistance > SIDE_WALL_THRESH) {
            return true;
        }
        return false;

    }

    private boolean isEdgeUpFront() {
        if (centerDistance < FRONT_EDGE_THRESH && centerDistance > FRONT_WALL_THRESH) {
            return true;
        }
        return false;
    }
}
