import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Configuration {

    private final LightSensor light;
    private final NXTRegulatedMotor leftWheel;
    private final NXTRegulatedMotor rightWheel;
    private final NXTRegulatedMotor sensorMotor;
    private final ArrayList<DataSet> sensorData;

    public Configuration() {
        super();
        light = new LightSensor(SensorPort.S4);
        leftWheel = Motor.A;
        rightWheel = Motor.B;
        sensorMotor = Motor.C;
        sensorData = new ArrayList<>();
    }

    public LightSensor getLight() {
        return light;
    }

    public NXTRegulatedMotor getSensorMotor() {
        return sensorMotor;
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

    public void updateSensorData(DataSet dataset) {
        sensorData.add(dataset);

    }

    public NXTRegulatedMotor getLeftWheel() {
        return leftWheel;
    }

    public NXTRegulatedMotor getRightWheel() {
        return rightWheel;
    }

}
