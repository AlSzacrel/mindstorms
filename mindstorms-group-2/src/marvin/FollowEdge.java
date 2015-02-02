package marvin;

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class FollowEdge  implements Step{
	
	private final static int SIDE_EDGE_THRESHOLD = 40;
	

	@Override
	public void run(Configuration configuration) {
        configuration.getSensorDataCollector().turnToLeftEdgeDetection();
        UltrasonicSensor ultraSonic = configuration.getUltraSonic();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
        
        //TODO: Do we need left edge detection, too?
        movement.crawl();
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
		
		// get a mean distance value
		float medDistance = ultraSonic.getDistance();
		int currentDistance;
		
		for (int i = 0; i < 6; i++) {
			Delay.msDelay(5);
			currentDistance = ultraSonic.getDistance();
			
			if (currentDistance != 255) {
				medDistance = Filter.avgEWMA(medDistance, currentDistance);
			}
		}		
		
		// If there is an edge
		if (medDistance > SIDE_EDGE_THRESHOLD){
	        movement.correct(15); //TODO change magic number
			
		// If there is no edge
		} else {
	        movement.correct(-15);
			Delay.msDelay(20);
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
