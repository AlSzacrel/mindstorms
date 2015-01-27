import java.util.LinkedList;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class Configuration {

    private LightSensor light;
    private final LinkedList<Step> steps;

    public Configuration() {
        super();
        light = new LightSensor(SensorPort.S4);
	leftWheel = Motor.A;
	rightWheel = Motor.B;
	sensorMotor = Motor.C;
    }

    public LightSensor getLight() {
        return light;
    }

    public boolean cancel() {
        return Button.ESCAPE.isDown();
    }

    public void displayInformation() {
        displayInformation(getLight());
    }

    private void displayInformation(LightSensor light) {
        LCD.drawInt(light.getLightValue(), 4, 0, 0);
        LCD.drawInt(light.getNormalizedLightValue(), 4, 0, 1);
        LCD.drawInt(SensorPort.S1.readRawValue(), 4, 0, 2);
        LCD.drawInt(SensorPort.S1.readValue(), 4, 0, 3);
    }

    public void runCurrentStep() {
        if (steps.isEmpty()) {
            return;
        }
        steps.get(0).run(this);
    }

    public void add(Step step) {
        steps.add(step);
    }

}
