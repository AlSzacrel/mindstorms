package marvin;

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class FollowEdge  implements Step{
	
	private final static int SIDE_EDGE_THRESHOLD = 40;
	

	@Override
	public void run(Configuration configuration) {
        configuration.getSensorDataCollector().turnToLeftEdgeDetection();
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
        
        sensorDataCollector.turnToRightEdgeDetection();
        followEdge(movement, ultraSonic);


        
//		TODO anpassen und einbauen
//        if (detectBarcode(light, sensorDataCollector)) {
//            Sound.beep();
//            Sound.beep();
//            configuration.nextStep(); //setzt nur den step für "nächste Runde" code läuft weiter
//            sensorDataCollector.turnToCenter();
//            while (sensorDataCollector.isDark(light.getNormalizedLightValue()) && !configuration.isCancel()) {
//                movement.drive();
//            }
//            sensorDataCollector.turnToLeftMaximum();
//        }
	}
	private void followEdge(MovementPrimitives movement, UltrasonicSensor ultraSonic) {
		
		for (int i = 0; i < 5; i++) {
			if (ultraSonic.getDistance() != 255) {
				
			}
		}
		
		
		if (true){
			
		}
		
		
		//
//      private void followWall(MovementPrimitives movement, UltrasonicSensor ultraSonic) {
//          int distance = ultraSonic.getDistance();
//          if (distance > SEARCH_THRESHOLD) {
//              searchWall(movement);
//          }
//          int correctionFactor = SIDE_WALL_THRESHOLD - distance;
//          int gainedCorrection = correctionFactor * GAIN;
//          int limittedCorrection = Math.max(gainedCorrection, MIN_CORRECTION);
//          limittedCorrection = Math.min(limittedCorrection, MAX_CORRECTION);
//          movement.correct(limittedCorrection);
//      }
	}
	
	

}
