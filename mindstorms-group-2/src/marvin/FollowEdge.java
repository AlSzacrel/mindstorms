package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class FollowEdge implements Step {

    private final static int SIDE_EDGE_THRESHOLD = 15;
    private final static int LEFT_CORRECTION_FACTOR = 15;
    private final static int RIGHT_CORRECTION_FACTOR = -10;
    private final static int DISTANCE_ERROR = 255;
    private boolean lastCorrectionWasLeft = false;

    // private boolean beginning = true;

    @Override
    public void run(Configuration configuration) {
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        LightSensor light = configuration.getLight();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();

        // if (beginning) {
        // takeTheRamp(movement, ultraSonic);
        // beginning = false;
        // }

        followEdge(movement, ultraSonic);

        if (sensorDataCollector.isBright(light.getLightValue())) {
            configuration.nextStep();
        }

    }

    // private void takeTheRamp(MovementPrimitives movement,
    // UltrasonicSensor ultraSonic) {
    // movement.stalk();
    // movement.drive();
    // Delay.msDelay(7000); // TODO magic numbers
    //
    // // movement.backup();
    // // Delay.msDelay(200);
    //
    // movement.slow();
    // movement.drive();
    //
    // while (getAverageDistance(ultraSonic) < 255) {
    // Delay.msDelay(20);
    // }
    // }

    private void followEdge(MovementPrimitives movement, UltrasonicSensor ultraSonic) {

        boolean hasStopped = false;
        float medDistance = getAverageDistance(ultraSonic);

        // If there is an edge
        if (medDistance > SIDE_EDGE_THRESHOLD) {

            if (!lastCorrectionWasLeft || hasStopped) {
                movement.crawl();
                hasStopped = false;
            }

            movement.correct(LEFT_CORRECTION_FACTOR); // TODO change magic
            // number
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

    private float getAverageDistance(UltrasonicSensor ultraSonic) {

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
