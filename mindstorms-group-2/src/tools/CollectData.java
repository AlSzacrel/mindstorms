package tools;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;
import marvin.Configuration;
import marvin.SensorDataCollector;

public class CollectData {

	public static void main(String[] args) throws IOException {
		Configuration configuration = new Configuration();
		SensorDataCollector sensorData = new SensorDataCollector(configuration);

		boolean scanComplete = false;
		NXTRegulatedMotor sensorMotor = configuration.getLeftWheel();

		sensorMotor.forward();
		while (scanComplete == false
				&& sensorMotor.getTachoCount() < Configuration.MAX_ANGLE
				&& sensorMotor.getTachoCount() >= 0 && !configuration.isCancel()) {
			
//			int lightValue = configuration.getLight().getNormalizedLightValue();
//			Integer angle = sensorMotor.getTachoCount();
//			if (isDark(lastLightValue) && isBright(lightValue)) {
//			}
//			lastLightValue = lightValue;
//			lastAngle = angle;
//			minAngle = Math.min(minAngle, angle);
//			maxAngle = Math.max(maxAngle, angle);
//		
			Delay.msDelay(50);
		}
		sensorMotor.stop();

		configuration.save();
	}
}
