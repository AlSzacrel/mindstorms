package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class HangingBridge implements Step {

	private final static int SIDE_EDGE_THRESHOLD = 15;
	private final static int LEFT_CORRECTION_FACTOR = 10;
	private final static int RIGHT_CORRECTION_FACTOR = -10;
	private final static int DISTANCE_ERROR = 255;
	private final static int N_GAPS_TOTAL = 14;
	// follow line
	private static final int HIGH_THRESHOLD = Configuration.MAX_ANGLE - 5;
	private static final int LOW_THRESHOLD = 5;
	private static final float GAIN = 0.5f;

	private boolean lastCorrectionWasLeft = false;
	private boolean followWallPart = true;
	private boolean followLinePart = false;
	private boolean floorWasBright = false;

	private int nGaps = 0;

	private FollowWall followWall = new FollowWall();
	private FollowLine followLine = new FollowLine();

	@Override
	public void run(Configuration configuration) {

		UltrasonicSensor ultraSonic = configuration.getUltraSonic();
		LightSensor light = configuration.getLight();
		MovementPrimitives movement = configuration.getMovementPrimitives();
		SensorDataCollector sensorDataCollector = configuration
				.getSensorDataCollector();

		movement.crawl();
		movement.drive();

		// follow the right wall until we either find the line or loose it
		if (followWallPart) {
			followWall(sensorDataCollector, movement, light, ultraSonic);

		} else if (followLinePart) {
			followLine(sensorDataCollector, movement);

		} else {
			sensorDataCollector.turnToRightMaximum();
			followEdge(movement, ultraSonic);

			if (sensorDataCollector.isDark(light.getNormalizedLightValue())
					&& floorWasBright) {
				floorWasBright = false;
				nGaps += 1;

				if (nGaps > N_GAPS_TOTAL) {
					configuration.nextStep();
					movement.stop();
				}

			} else if (sensorDataCollector.isBright(light
					.getNormalizedLightValue())) {
				floorWasBright = true;
			}

		}

		// TODO: find end
	}

	private void followLine(SensorDataCollector sensorDataCollector, MovementPrimitives movement) {
		
		LineBorders lineData = sensorDataCollector.collectLineData();

		int leftBorder = lineData.getDarkToBright();
		int rightBorder = lineData.getBrightToDark();

		if (leftBorder < LOW_THRESHOLD && rightBorder > HIGH_THRESHOLD) {
			followLinePart = false;
			return;
		}

		int center = (leftBorder + rightBorder) / 2;

		int correctionFactor = ((Configuration.MAX_ANGLE / 2) - center);
		int gainedFactor = (int) (correctionFactor * GAIN);

		RConsole.println("" + gainedFactor);
		movement.correct(gainedFactor);
		
	}

	private void followWall(SensorDataCollector sensorDataCollector,
			MovementPrimitives movement, LightSensor light,
			UltrasonicSensor ultraSonic) {
		sensorDataCollector.turnToLeftMaximum();
		followWall.followWall(movement, ultraSonic);

		if (sensorDataCollector.isBright(light.getNormalizedLightValue())
				|| ultraSonic.getDistance() == DISTANCE_ERROR) {

			followWallPart = false;
			// if
			// (!sensorDataCollector.isBright(light.getNormalizedLightValue()))
			// {
			// followLine.lost(configuration);
			// }
			followLinePart = true;
		}
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
