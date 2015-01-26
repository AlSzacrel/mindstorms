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

}
