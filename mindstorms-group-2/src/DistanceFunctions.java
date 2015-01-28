
public class DistanceFunctions implements Step {	

	private static final int SIDE_WALL_THRESH = 50;
	private static final int FRONT_WALL_THRESH = 18; //average 24.2
	private static final int SIDE_EDGE_THRESH = 50;
	private static final int FRONT_EDGE_THRESH = 18; 
    private DataSet sensData;

	@Override
	public void run(Configuration configuration) {
        sensData = configuration.getLastSensorData();
		
	}

	public boolean isWallLeft() {
		if (sensData.get(0).getDistance() < SIDE_WALL_THRESH) {
			return true;
		}
		return false;
	}

	public boolean isWallRight() {
		if (sensData.get(sensData.size() - 1).getDistance() < SIDE_WALL_THRESH) {
			return true;
		}
		return false;
		
	}

	public boolean isWallUpFront() {
		if (sensData.get(sensData.size() / 2).getDistance() < FRONT_WALL_THRESH) {
			return true;
		}
		return false;
		
	}

	public boolean isEdgeLeft() {
		if (sensData.get(0).getDistance() < SIDE_EDGE_THRESH) {
			return true;
		}
		return false;
	}

	public boolean isEdgeRight() {
		if (sensData.get(sensData.size() - 1).getDistance() < SIDE_EDGE_THRESH) {
			return true;
		}
		return false;
		
	}

	public boolean isEdgeUpFront() {
		if (sensData.get(sensData.size() / 2).getDistance() < FRONT_EDGE_THRESH) {
			return true;
		}
		return false;
		
	}	
}
