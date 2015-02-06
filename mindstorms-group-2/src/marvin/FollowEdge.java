package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class FollowEdge implements Step {

    private final static int SIDE_EDGE_THRESHOLD = 20;
    private final static int LEFT_CORRECTION_FACTOR = 25;
    private final static int RIGHT_CORRECTION_FACTOR = -15;
    private final static int DISTANCE_ERROR = 255;
    private static final int DETECT_ELEVATOR_LIGHT_THRESHOLD = 480;
    private boolean lastCorrectionWasLeft = false;

    private boolean beginning = true;

    @Override
    public void run(Configuration configuration) {
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        LightSensor light = configuration.getLight();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();

        sensorDataCollector.turnToRightMaximum();

        if (beginning) {
            takeTheRamp(configuration, movement, ultraSonic);
            beginning = false;
        }

        followEdge(movement, ultraSonic);

        if (light.getNormalizedLightValue() > DETECT_ELEVATOR_LIGHT_THRESHOLD) {
            configuration.nextStep();
            movement.stop();
        }

    }

    private void takeTheRamp(Configuration configuration, MovementPrimitives movement, UltrasonicSensor ultraSonic) {
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
        leftWheel.rotate(80);
        // movement.drive();
        // movement.slow();
        movement.drive();
        movement.crawl();
        Delay.msDelay(6500);
        // movement.turnRight();
        // Delay.msDelay(520);
        // movement.drive();

        while (getAverageDistance(ultraSonic) < SIDE_EDGE_THRESHOLD) {
            Delay.msDelay(20);
        }
    }

    private void followEdge(MovementPrimitives movement, UltrasonicSensor ultraSonic) {

        boolean hasStopped = false;
        float medDistance = getAverageDistance(ultraSonic);

        // If there is an edge
        if (medDistance > SIDE_EDGE_THRESHOLD) {

            if (!lastCorrectionWasLeft || hasStopped) {
                movement.crawl();
                hasStopped = false;
            }

            movement.correct(LEFT_CORRECTION_FACTOR);
            lastCorrectionWasLeft = true;

            // If there is no edge
        } else {

            if (lastCorrectionWasLeft || hasStopped) {
                movement.crawl();
                hasStopped = false;
            }
            movement.correct(RIGHT_CORRECTION_FACTOR);
            lastCorrectionWasLeft = false;
        }
    }

    public static float getAverageDistance(UltrasonicSensor ultraSonic) {

        // get a mean distance value
        float medDistance = ultraSonic.getDistance();
        int currentDistance;

        while (medDistance == DISTANCE_ERROR) {
            medDistance = ultraSonic.getDistance();
        }

        for (int i = 0; i < 6; i++) {
            Delay.msDelay(5);
            currentDistance = ultraSonic.getDistance();

            if (currentDistance != DISTANCE_ERROR) {
                medDistance = Filter.avgEWMA(medDistance, currentDistance);
            }
        }
        return medDistance;
    }

    @Override
    public String getName() {
        return "FollowEdge";
    }
}
