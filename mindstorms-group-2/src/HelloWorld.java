import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class HelloWorld {
    private static final int SILVER_LINE_THRESHOLD = 450;
    private static boolean running = true;

    public static void main(String[] args) {
        LightSensor light = new LightSensor(SensorPort.S4);

        while (running) {
            cancelRun();
            displayInformation(light);
            followLine(light);

        }
    }

    private static void followLine(LightSensor light) {
        Motor.B.stop();
        Motor.A.forward();

        if (light.getNormalizedLightValue() < SILVER_LINE_THRESHOLD) {
            Motor.A.stop();
            Motor.B.forward();
        }
    }

    private static void displayInformation(LightSensor light) {
        LCD.drawInt(light.getLightValue(), 4, 0, 0);
        LCD.drawInt(light.getNormalizedLightValue(), 4, 0, 1);
        LCD.drawInt(SensorPort.S1.readRawValue(), 4, 0, 2);
        LCD.drawInt(SensorPort.S1.readValue(), 4, 0, 3);
    }

    private static void cancelRun() {
        if (Button.ESCAPE.isDown()) {
            running = false;
        }
    }
}