import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class Configuration {

    private LightSensor light;

    public Configuration() {
        super();
        light = new LightSensor(SensorPort.S4);
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
        // TODO Auto-generated method stub
    }

    public void add(Step step) {
        // TODO Auto-generated method stub

    }

}
