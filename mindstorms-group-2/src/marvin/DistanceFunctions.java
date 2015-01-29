package marvin;

public class DistanceFunctions implements Step {

	/*
	 * Perceptable Data Range: [3;31] 3 is hend in front of sensor meassured
	 * values up to 60, but not on the stupid plane!!! Set head back on 28.01.
	 * ~17h Before that: distance to wall when touching: 9~10
	 */
	private static final int SIDE_WALL_THRESH = 30;
	private static final int FRONT_WALL_THRESH = 14; // average 24.2
	private static final int FOLLOW_WALL_LOW_THRESH = 18;
	private static final int FOLLOW_WALL_HIGH_THRESH = 25;
	private static final int SIDE_EDGE_THRESH = 35; // Können wir das überhaupt
													// noch wahrnehmen?
	private static final int FRONT_EDGE_THRESH = 35;

	private MovementPrimitives movPrim;
	private DataSet sensData;
	private int leftDistance;
	private int rightDistance;
	private int centerDistance;

	@Override
	public void run(Configuration configuration) {
		movPrim = configuration.getMovementPrimitives();
		sensData = configuration.getLastSensorData();
		leftDistance = sensData.get(0).getDistance();
		rightDistance = sensData.get(sensData.size() - 1).getDistance();
		centerDistance = sensData.get(sensData.size() / 2).getDistance();
		followLeftWall();

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
		if (rightDistance < SIDE_EDGE_THRESH
				&& rightDistance > SIDE_WALL_THRESH) {
			return true;
		}
		return false;

	}

	private boolean isEdgeUpFront() {
		if (centerDistance < FRONT_EDGE_THRESH
				&& centerDistance > FRONT_WALL_THRESH) {
			return true;
		}
		return false;
	}

	// needs to be called from the outside
	public void followLeftWall() {
		if (!isWallLeft()) {
			movPrim.turnLeft();
		} else if (leftDistance > FOLLOW_WALL_HIGH_THRESH) {
			movPrim.turnLeft();

		} else if (leftDistance < FOLLOW_WALL_LOW_THRESH) {
			movPrim.turnRight();
		} else {
			movPrim.slow();
			movPrim.drive();
		}
	}
}
