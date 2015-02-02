package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class FollowWall implements Step {

    /*
     * Perceptable Data Range: [3;31] 3 is hend in front of sensor meassured
     * values up to 60, but not on the stupid plane!!! Set head back on 28.01.
     * ~17h Before that: distance to wall when touching: 9~10
     */

    // first labyrinth works with 30
    private static final int SIDE_WALL_THRESHOLD = 27;
    private static final int SEARCH_THRESHOLD = 50;
    private static final int GAIN = 15;
    private static final int MAX_CORRECTION = 100;
    private static final int MIN_CORRECTION = -MAX_CORRECTION;
    private int lastLightValue;
    private int lineBeginning = 0;
    private int lineEnding = 0;

    @Override
    public void run(Configuration configuration) {
        // TODO initially search wall
        // search on the left and right side. If there is no wall, we are in the
        // middle
        // if we start in the middle, we should drive straight on until we find
        // the wall on the left or right side or until we hit the next wall.

        // TODO turn sensor head to side where we search the wall.
        configuration.getSensorDataCollector().turnToRightMaximum();
        // TODO change direction when distance to wall decreases
        TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        LightSensor light = configuration.getLight();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
        followWall(movement, ultraSonic);
        detectWall(movement, rightTouchSensor);
        if (detectBarcode(light, sensorDataCollector)) {
            Sound.beep();
            Sound.beep();
            configuration.nextStep();
            sensorDataCollector.turnToCenter();
            while (sensorDataCollector.isDark(light.getNormalizedLightValue()) && !configuration.isCancel()) {
                movement.drive();
            }
            sensorDataCollector.turnToLeftMaximum();
        }
    }

    private boolean detectBarcode(LightSensor light, SensorDataCollector sensor) {
        int lightValue = light.getNormalizedLightValue();
        // TODO detect line borders and count them. There must be 3 from dark to
        // bright and 3 from bright to dark
        if (sensor.isDark(lastLightValue) && sensor.isBright(lightValue)) {
            // switched from dark to bright --> line starts
            lineBeginning++;
            Sound.beep();
            Sound.beep();
        }
        if (sensor.isBright(lastLightValue) && sensor.isDark(lightValue)) {
            // switched from dark to bright --> line ends
            lineEnding++;
            Sound.beep();
            Sound.beep();
            Sound.beep();
        }
        // TODO use similar mechanism as in FollowLine
        lastLightValue = lightValue;
        return lineBeginning >= 3 && lineEnding >= 3;
    }

    private void detectWall(MovementPrimitives movement, TouchSensor rightTouchSensor) {
        if (!rightTouchSensor.isPressed()) {
            return;
        }
        movement.stop();
        movement.backup();
        Delay.msDelay(4000);
        movement.spinLeft();
        Delay.msDelay(1200);
        movement.stop();
        movement.drive();
    }

    private void followWall(MovementPrimitives movement, UltrasonicSensor ultraSonic) {
        int distance = ultraSonic.getDistance();
        if (distance > SEARCH_THRESHOLD) {
            searchWall(movement);
        }
        int correctionFactor = SIDE_WALL_THRESHOLD - distance;
        int gainedCorrection = correctionFactor * GAIN;
        int limittedCorrection = Math.max(gainedCorrection, MIN_CORRECTION);
        limittedCorrection = Math.min(limittedCorrection, MAX_CORRECTION);
        movement.correct(limittedCorrection);
    }

    // TODO do we really need this method
    private void searchWall(MovementPrimitives movement) {
        movement.backup();
    }
}
