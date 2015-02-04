package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class HangingBridge implements Step {

	private final static int SIDE_EDGE_THRESHOLD = 15;
	private final static int LEFT_CORRECTION_FACTOR = 10;
	private final static int RIGHT_CORRECTION_FACTOR = -10;
	private final static int DISTANCE_ERROR = 255;
	private final static int N_GAPS_TOTAL = 14;
	private boolean lastCorrectionWasLeft = false;
	private boolean beginning = true;
	private boolean floorWasBright = false;
	private int nGaps = 0;

	@Override
	public void run(Configuration configuration) {

		UltrasonicSensor ultraSonic = configuration.getUltraSonic();
		LightSensor light = configuration.getLight();
		MovementPrimitives movement = configuration.getMovementPrimitives();
		SensorDataCollector sensorDataCollector = configuration
				.getSensorDataCollector();

		sensorDataCollector.turnToRightMaximum();

		if (beginning) {
			movement.slow();
			movement.drive();
			Delay.msDelay(500);
			movement.turnRight();
			Delay.msDelay(150);
			movement.crawl();
			movement.drive();

			while (getAverageDistance(ultraSonic) < SIDE_EDGE_THRESHOLD) {
				Delay.msDelay(20);
			}
			beginning = false;
		}
		followEdge(movement, ultraSonic);

		if (sensorDataCollector.isDark(light.getNormalizedLightValue())
				&& floorWasBright) {
			floorWasBright = false;
			nGaps += 1;

			if (nGaps > N_GAPS_TOTAL) {
				configuration.nextStep();
				movement.stop();
			}

		} else if (sensorDataCollector
				.isBright(light.getNormalizedLightValue())) {
			floorWasBright = true;
		}

		// TODO: find end
	}

	private void followEdge(MovementPrimitives movement,
			UltrasonicSensor ultraSonic) {

		float medDistance = getAverageDistance(ultraSonic);

		// If there is an edge
		if (medDistance > SIDE_EDGE_THRESHOLD) {

			if (!lastCorrectionWasLeft) {
				movement.crawl();
			}

			movement.correct(LEFT_CORRECTION_FACTOR);
			lastCorrectionWasLeft = true;

			// If there is no edge
		} else {

			if (lastCorrectionWasLeft) {
				movement.crawl();
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
