package marvin;

public class DistanceFunctions implements Step {	

	/*
	 * Perceptable Data Range: [3;31]
	 * 3 is hend in front of sensor
	 * meassured values up to 60, but not on the stupid plane!!!
	 * Set head back on 28.01. ~17h
	 * Before that: distance to wall when touching: 9~10
	 */
	private static final int SIDE_WALL_THRESH = 35;
	private static final int FRONT_WALL_THRESH = 18; //average 24.2
	private static final int FOLLOW_WALL_THRESH = 20;
	private static final int SIDE_EDGE_THRESH = 100; //Können wir das überhaupt noch wahrnehmen?
	private static final int FRONT_EDGE_THRESH = 18; 
	private static final int WALL_TOO_CLOSE_THRESH = 15;
	
    private DataSet sensData;

	@Override
	public void run(Configuration configuration) {
        sensData = configuration.getLastSensorData();
		
	}

	private boolean isWallLeft() {
		if (sensData.get(0).getDistance() < SIDE_WALL_THRESH) {
			return true;
		}
		return false;
	}

	private boolean isWallRight() {
		if (sensData.get(sensData.size() - 1).getDistance() < SIDE_WALL_THRESH) {
			return true;
		}
		return false;
		
	}

	private boolean isWallUpFront() {
		if (sensData.get(sensData.size() / 2).getDistance() < FRONT_WALL_THRESH) {
			return true;
		}
		return false;
		
	}

	private boolean isEdgeLeft() {
		if (sensData.get(0).getDistance() < SIDE_EDGE_THRESH) {
			return true;
		}
		return false;
	}

	private boolean isEdgeRight() {
		if (sensData.get(sensData.size() - 1).getDistance() < SIDE_EDGE_THRESH) {
			return true;
		}
		return false;
		
	}

	private boolean isEdgeUpFront() {
		if (sensData.get(sensData.size() / 2).getDistance() < FRONT_EDGE_THRESH) {
			return true;
		}
		return false;		
	}
	
	// needs to be called from the outside
	private void followLeftWall() {
		if (isWallLeft()  && sensData.get(0).getDistance() < FOLLOW_WALL_THRESH) {
			
		}
	}
}
