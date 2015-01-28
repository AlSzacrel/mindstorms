import java.util.ArrayList;

import lejos.nxt.Motor;

public class FollowLine implements Step {
	
	private DataSet sensData;

	@Override
	public void run(Configuration configuration) {
		
		sensData = configuration.getLastSensorData();
		
	}
	
	public void StraightCase(){
		//case line to the left or 
		if(sensData.get(0).getValue() > 350){
			for (int i = 1; i < sensData.size(); i++){
				if(sensData.get(i).getValue() <= 350){
					//TODO: case = line left
					break;
				} else {
					//TODO: standing orthogonal to line
				}
			}
		} else {
			
		}
		
		//for (int i = 0; i < sensData.size(); i++){
			
		//}
	}
	
	
	// alter Code der mit schiebesensor nicht funktioniert: nur für Referenz

   /** private enum Direction {
        LEFT() {
            @Override
            public void switchDirection() {
                Motor.B.stop();
                Motor.A.forward();
            }
        },
        RIGHT() {
            @Override
            public void switchDirection() {
                Motor.A.stop();
                Motor.B.forward();
            }
        };

        public abstract void switchDirection();
    }

    private static final int SILVER_LINE_THRESHOLD = 375;

    private Direction last;

    @Override
    public void run(Configuration configuration) {
        Direction currentDirection = configuration.getLight().getNormalizedLightValue() < SILVER_LINE_THRESHOLD ? Direction.LEFT
                : Direction.RIGHT;
        if (currentDirection == last) {
            return;
        }
        currentDirection.switchDirection();
        last = currentDirection;
    } */
}
