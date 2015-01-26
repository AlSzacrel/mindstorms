import lejos.nxt.Button;
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

}
