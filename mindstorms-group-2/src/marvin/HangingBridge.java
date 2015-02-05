package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class HangingBridge implements Step {

	// follow wall
	private final static int NO_WALL = 30;
	private FollowWall followWall = new FollowWall();

	// follow line
	private static final int HIGH_THRESHOLD = Configuration.MAX_ANGLE - 5;
	private static final int LOW_THRESHOLD = 5;
	private static final float GAIN = 0.5f;
	private FollowLine followLine = new FollowLine();

	// drive the bridge
	private final static int SIDE_EDGE_THRESHOLD = 25;
	private final static int LEFT_CORRECTION_FACTOR = 10;
	private final static int RIGHT_CORRECTION_FACTOR = -10;
	private final static int N_GAPS_TOTAL = 14;
	private final static int BRIGHTNESS_THRESHOLD = 280;
	private int nGaps = 0;
	private int nDarkRounds = 0;
	private boolean lastCorrectionWasLeft = false;
	private boolean floorWasBright = false;

	// control states
	private boolean followWallPart = true;
	private boolean followLinePart = false;
	private boolean reachedEndOfBridge = false;
	private boolean beginningOfPart = true;
	private boolean beenOnBridge = false;

	@Override
	public void run(Configuration configuration) {

		UltrasonicSensor ultraSonic = configuration.getUltraSonic();
		LightSensor light = configuration.getLight();
		MovementPrimitives movement = configuration.getMovementPrimitives();
		SensorDataCollector sensorDataCollector = configuration
				.getSensorDataCollector();

		// follow the right wall until we either find the line or loose the wall
		if (followWallPart) {
			if (beginningOfPart) {
				movement.crawl();
				movement.drive();
				sensorDataCollector.turnToLeftMaximum();
				beginningOfPart = false;
			}

			if (sensorDataCollector.isBright(light.getNormalizedLightValue())
					|| ultraSonic.getDistance() > NO_WALL) {
				followWallPart = false;
				followLinePart = true;
				beginningOfPart = true;
				return;
			}

			followWall.followWall(movement, ultraSonic);

		} else if (followLinePart) {

			if (beginningOfPart) {
				if (!sensorDataCollector.isBright(light
						.getNormalizedLightValue())) {
					followLine.lost(configuration);
				}
				beginningOfPart = false;
			}

			if (!FollowLine.followShortAndStraightLine(movement,
					sensorDataCollector)) {
				followLinePart = false;
				beginningOfPart = true;
			}

		} else {

			if (beginningOfPart) {
				sensorDataCollector.turnToRightMaximum();
				beginningOfPart = false;
			}

			followHangingBridge(sensorDataCollector, movement, light,
					ultraSonic);

			if (reachedEndOfBridge) {
				// TODO remove this delay if stop() is removed
				movement.crawl();
				movement.drive();
				Delay.msDelay(250);
				configuration.nextStep();
				movement.stop();
			}
		}
	}

	private void followHangingBridge(SensorDataCollector sensorDataCollector,
			MovementPrimitives movement, LightSensor light,
			UltrasonicSensor ultraSonic) {
		followEdge(movement, ultraSonic);

		if (sensorDataCollector.isDark(light.getNormalizedLightValue())) {

			if (floorWasBright) {
				nDarkRounds = 0;
			}

			nDarkRounds += 1;
			Sound.beep();

			floorWasBright = false;

			if (nDarkRounds > 6 && beenOnBridge) {
				reachedEndOfBridge = true;
			}

		} else if (light.getNormalizedLightValue() > BRIGHTNESS_THRESHOLD) {
			floorWasBright = true;
			beenOnBridge = true;
		}

		// if (light.getNormalizedLightValue() <= BRIGHTNESS_THRESHOLD
		// && floorWasBright) {
		// floorWasBright = false;
		// nGaps += 1;
		// Sound.beep();
		//
		// if (nGaps > N_GAPS_TOTAL) {
		// reachedEndOfBridge = true;
		// }
		//
		// } else if (light.getNormalizedLightValue() > BRIGHTNESS_THRESHOLD &&
		// !floorWasBright) {
		// floorWasBright = true;
		//
		// Sound.beep();
		// Delay.msDelay(20);
		// Sound.beep();
		// }
	}

	private void followEdge(MovementPrimitives movement,
			UltrasonicSensor ultraSonic) {

		float medDistance = FollowEdge.getAverageDistance(ultraSonic);

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

	@Override
	public String getName() {
		return "HangingBridge";
	}

}
