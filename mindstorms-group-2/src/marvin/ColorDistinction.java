package marvin;

import java.util.ArrayList;

public class ColorDistinction implements Step {

	// rot [435,447]
	// gr¨¹ne [400,423]
	private static final int CHANGE_THRESH = 428;
	private static final int RED_THRESH = 450;
	private static final int GREEN_THRESH = 400;
	private boolean bluetoothAccepted = false;

	@Override
	public void run(Configuration configuration) {
		DataSet sensorData = configuration.getLastSensorData();
		
		//TODO request bluetooth value
		float colorAVG = sensorData.get(sensorData.size() - 1).getLightValue();
		
		//TODO adjust once also scanning from right to left
		for(int i = 0; i <= sensorData.size() - 1; i++){
			colorAVG = Filter.avgEWMA(colorAVG, sensorData.get(i).getLightValue());		
		}
		
		
		if ((isGreen(colorAVG) && !bluetoothAccepted) || isRed(colorAVG)){
				configuration.getMovementPrimitives().stop();

		} else {
			//TODO call enter elevator function
		}
	}	
	 
	private boolean isGreen(float color){		
		return color > GREEN_THRESH && color <= CHANGE_THRESH;		
	}

	
	private boolean isRed(float color){
		return color > CHANGE_THRESH && color < RED_THRESH;
	}

}