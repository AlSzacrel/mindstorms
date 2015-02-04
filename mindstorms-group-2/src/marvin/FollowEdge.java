package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class FollowEdge implements Step {

	private final static int SIDE_EDGE_THRESHOLD = 15;
	private final static int LEFT_CORRECTION_FACTOR = 35;
	private final static int RIGHT_CORRECTION_FACTOR = -23;
	private final static int DISTANCE_ERROR = 255;
	private static final int DETECT_ELEVATOR_LIGHT_THRESHOLD = 480;
	private boolean lastCorrectionWasLeft = false;
	private int iterativeLeftCorrectionFactor = LEFT_CORRECTION_FACTOR; 
	private boolean beginning = true;

	@Override
	public void run(Configuration configuration) {
		UltrasonicSensor ultraSonic = configuration.getUltraSonic();
		LightSensor light = configuration.getLight();
		MovementPrimitives movement = configuration.getMovementPrimitives();
		SensorDataCollector sensorDataCollector = configuration
				.getSensorDataCollector();
		
		sensorDataCollector.turnToRightMaximum();
		//light.setFloodlight(false);

		if (beginning) {
			takeTheRamp(movement, ultraSonic);
			beginning = false;
		}

		followEdge(movement, ultraSonic);

		if (light.getNormalizedLightValue() > DETECT_ELEVATOR_LIGHT_THRESHOLD) {
			configuration.nextStep();
			movement.stop();
		}

	}

	private void takeTheRamp(MovementPrimitives movement,
			UltrasonicSensor ultraSonic) {
		movement.slow();
		movement.drive();
		Delay.msDelay(2000);
		movement.turnRight();
		Delay.msDelay(200);
		movement.fullSpeed();
		movement.drive();

		while (getAverageDistance(ultraSonic) < SIDE_EDGE_THRESHOLD) {
			Delay.msDelay(20);
		}
	}

	private void followEdge(MovementPrimitives movement,
			UltrasonicSensor ultraSonic) {

		boolean hasStopped = false;
		float medDistance = getAverageDistance(ultraSonic);

		// If there is an edge
		if (medDistance > SIDE_EDGE_THRESHOLD) {

			if (!lastCorrectionWasLeft || hasStopped) {
				movement.fullSpeed();
				iterativeLeftCorrectionFactor = LEFT_CORRECTION_FACTOR;
				hasStopped = false;
			}

			movement.correct(iterativeLeftCorrectionFactor);
			iterativeLeftCorrectionFactor = (int) (iterativeLeftCorrectionFactor * 0.75);
			lastCorrectionWasLeft = true;

			// If there is no edge
		} else {

			if (lastCorrectionWasLeft || hasStopped) {
				movement.fullSpeed();
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

}
