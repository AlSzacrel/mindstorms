import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Marvin {

    private static final int SILVER_LINE_THRESHOLD = 450;
    private boolean running = true;
    private Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() {

        while (running) {
            cancelRun();
            displayInformation(configuration.getLight());
            followLine(configuration.getLight());
        }
    }

    private void followLine(LightSensor light) {
        Motor.B.stop();
        Motor.A.forward();

        if (light.getNormalizedLightValue() < SILVER_LINE_THRESHOLD) {
            Motor.A.stop();
            Motor.B.forward();
        }
    }

    private void displayInformation(LightSensor light) {
        LCD.drawInt(light.getLightValue(), 4, 0, 0);
        LCD.drawInt(light.getNormalizedLightValue(), 4, 0, 1);
        LCD.drawInt(SensorPort.S1.readRawValue(), 4, 0, 2);
        LCD.drawInt(SensorPort.S1.readValue(), 4, 0, 3);
    }

    private void cancelRun() {
        if (configuration.cancel()) {
            running = false;
        }
    }
}
