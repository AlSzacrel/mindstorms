package marvin;

import java.io.IOException;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

public class SensorDataCollector {

    private static final float SENSOR_HEAD_SPEED_FACTOR = 1f;
    private static final int MEASURE_INTERVAL = 5;
    private static final int BRIGHT_THRESHOLD = 350;
    // private static final int MAX_ANGLE_STEPS = 5;
    // private static final int STEP_SIZE = 175 / 5;
    private final Configuration configuration;

    private boolean leftToRight = true;

    public SensorDataCollector(Configuration configuration) {
        this.configuration = configuration;
    }

    public void collectData() throws IOException {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        sensorMotor.setSpeed(SENSOR_HEAD_SPEED_FACTOR * sensorMotor.getMaxSpeed());
        configuration.addNewLine(scan(sensorMotor));
        leftToRight = !leftToRight;
    }

    private LineBorders scan(NXTRegulatedMotor sensorMotor) {
        if (leftToRight) {
            sensorMotor.rotateTo(Configuration.MAX_ANGLE, true);
        } else {
            sensorMotor.rotateTo(0, true);
        }
        int lastLightValue = configuration.getLight().getNormalizedLightValue();
        int lastAngle = sensorMotor.getTachoCount();
        int darkToBrightAngle = Integer.MIN_VALUE;
        int brightToDarkAngle = Integer.MIN_VALUE;
        int minAngle = lastAngle;
        int maxAngle = lastAngle;

        while (sensorMotor.isMoving() && !configuration.isCancel()) {
            Delay.msDelay(MEASURE_INTERVAL);
            int lightValue = configuration.getLight().getNormalizedLightValue();
            Integer angle = sensorMotor.getTachoCount();
            if (isDark(lastLightValue) && isBright(lightValue)) {
                if (leftToRight) {
                    darkToBrightAngle = (angle + lastAngle) / 2;
                    brightToDarkAngle = Integer.MIN_VALUE;
                } else {
                    brightToDarkAngle = (angle + lastAngle) / 2;
                    darkToBrightAngle = Integer.MIN_VALUE;
                }
            }
            if (isBright(lastLightValue) && isDark(lightValue)) {
                if (leftToRight) {
                    brightToDarkAngle = (angle + lastAngle) / 2;
                } else {
                    darkToBrightAngle = (angle + lastAngle) / 2;
                }
            }
            lastLightValue = lightValue;
            lastAngle = angle;
            minAngle = Math.min(minAngle, angle);
            maxAngle = Math.max(maxAngle, angle);
        }
        configuration.write("darkToBright: " + darkToBrightAngle + " brightToDark: " + brightToDarkAngle
                + " lineWidth: " + (brightToDarkAngle - darkToBrightAngle) + ";\r\n\r\n");
        return new LineBorders(darkToBrightAngle, brightToDarkAngle, minAngle, maxAngle);
    }

    private boolean isBright(int lightValue) {
        return lightValue > BRIGHT_THRESHOLD;
    }

    private boolean isDark(int lightValue) {
        return !isBright(lightValue);
    }
}
