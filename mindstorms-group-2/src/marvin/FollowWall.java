package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class FollowWall implements Step {

    /*
     * Perceptable Data Range: [3;31] 3 is hend in front of sensor meassured
     * values up to 60, but not on the stupid plane!!! Set head back on 28.01.
     * ~17h Before that: distance to wall when touching: 9~10
     */

    // first labyrinth works with 30
    private static final int SIDE_WALL_THRESHOLD = 9;
    private static final int SEARCH_THRESHOLD = 50;
    private static final int GAIN = 10;
    private static final int MAX_CORRECTION = 45;
    private static final int MIN_CORRECTION = -MAX_CORRECTION;

    @Override
    public void run(Configuration configuration) {
        configuration.getMovementPrimitives().slow();
        // TODO initially search wall
        // search on the left and right side. If there is no wall, we are in the
        // middle
        // if we start in the middle, we should drive straight on until we find
        // the wall on the left or right side or until we hit the next wall.

        // TODO turn sensor head to side where we search the wall.
        configuration.getSensorDataCollector().turnToLeftMaximum();
        // TODO change direction when distance to wall decreases
        TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
        TouchSensor leftTouchSensor = configuration.getLeftTouchSensor();
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        LightSensor light = configuration.getLight();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
        followWall(movement, ultraSonic, configuration);
        detectWall(configuration, movement, rightTouchSensor, leftTouchSensor);
        if (sensorDataCollector.detectBarcode(light)) {
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

    private void detectWall(Configuration configuration, MovementPrimitives movement, TouchSensor rightTouchSensor,
            TouchSensor leftTouchSensor) {
        if (!rightTouchSensor.isPressed() && !leftTouchSensor.isPressed()) {
            return;
        }
        movement.stop();
        movement.resetSpeed();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        leftWheel.rotate(-200, true);
        rightWheel.rotate(-200, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        rightWheel.rotate(400, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        movement.stop();
        movement.drive();
    }

    public void followWall(MovementPrimitives movement, UltrasonicSensor ultraSonic, Configuration configuration) {
        int distance = ultraSonic.getDistance();
        if (distance > SEARCH_THRESHOLD) {
            searchWall(configuration);
        }
        int correctionFactor = SIDE_WALL_THRESHOLD - distance;
        int gainedCorrection = correctionFactor * GAIN;
        int limittedCorrection = Math.max(gainedCorrection, MIN_CORRECTION);
        limittedCorrection = Math.min(limittedCorrection, MAX_CORRECTION);
        RConsole.println("d: " + distance + " c: " + correctionFactor + " g: " + gainedCorrection + " l: "
                + limittedCorrection);
        movement.correct(limittedCorrection);
    }

    // TODO do we really need this method
    private void searchWall(Configuration configuration) {
        Sound.beep();
        Delay.msDelay(100);
        Sound.beep();
        Delay.msDelay(100);
        Sound.beep();
        Delay.msDelay(100);
        Sound.beep();

        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        MovementPrimitives movement = configuration.getMovementPrimitives();

        movement.stop();
        leftWheel.rotate(200, true);
        rightWheel.rotate(200, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        leftWheel.rotate(380, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        movement.stop();
        movement.drive();
    }

    @Override
    public String getName() {
        return "FollowWall";
    }
}
