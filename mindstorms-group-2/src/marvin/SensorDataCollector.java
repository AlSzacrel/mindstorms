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
        sensorMotor.setSpeed(0.1f * sensorMotor.getMaxSpeed());
        sensorMotor.rotateTo(Configuration.MAX_ANGLE, true);
        int distance = configuration.getUltraSonic().getDistance();
        DataSet dataSet = new DataSet(1);
        dataSet.append(new Value(0, 0, distance));
        configuration.updateSensorData(dataSet);
        Delay.msDelay(100);
        int lastLightValue = configuration.getLight().getNormalizedLightValue();
        int lastAngle = sensorMotor.getTachoCount();
        int darkToBrightAngle = Integer.MIN_VALUE;
        int brightToDarkAngle = Integer.MIN_VALUE;

        while (sensorMotor.isMoving() && !configuration.isCancel()) {
            int lightValue = configuration.getLight().getNormalizedLightValue();
            Integer angle = sensorMotor.getTachoCount();
            configuration.write(angle + " - " + lightValue + ";");

            if (isDark(lastLightValue) && isBright(lightValue)) {
                darkToBrightAngle = (angle + lastAngle) / 2;
                brightToDarkAngle = Integer.MIN_VALUE;
            }
            if (isBright(lastLightValue) && isDark(lightValue)) {
                brightToDarkAngle = (angle + lastAngle) / 2;
            }
            lastLightValue = lightValue;
            lastAngle = angle;
            Delay.msDelay(100);
        }
        configuration.write("darkToBright: " + darkToBrightAngle + " brightToDark: " + brightToDarkAngle
                + " lineWidth: " + (brightToDarkAngle - darkToBrightAngle) + ";\r\n\r\n");
        sensorMotor.rotateTo(0, true);
        sensorMotor.setSpeed(0.1f * sensorMotor.getMaxSpeed());
        sensorMotor.waitComplete();
        configuration.addNewLine(new LineBorders(darkToBrightAngle, brightToDarkAngle));
    }

    private boolean isBright(int lightValue) {
        return lightValue > BRIGHT_THRESHOLD;
    }

    private boolean isDark(int lightValue) {
        return !isBright(lightValue);
    }

    public DataSet collectDataRow() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        sensorMotor.setSpeed(SENSOR_HEAD_SPEED_FACTOR * sensorMotor.getMaxSpeed());
        if (leftToRight) {
            sensorMotor.rotateTo(Configuration.MAX_ANGLE, true);
        } else {
            sensorMotor.rotateTo(0, true);
        }

        DataSet dataSet = new DataSet(50);
        while (sensorMotor.isMoving() && !configuration.isCancel()) {
            Delay.msDelay(MEASURE_INTERVAL);
            Integer angle = sensorMotor.getPosition();
            int lightValue = configuration.getLight().getNormalizedLightValue();
            int distance = configuration.getUltraSonic().getDistance();
            if (leftToRight) {
                dataSet.append(new Value(angle, lightValue, distance));
            } else {
                dataSet.prepend(new Value(angle, lightValue, distance));
            }
        }
        leftToRight = !leftToRight;
        return dataSet;
    }

    public void turnToRightMaximum() {
        configuration.getSensorMotor().rotateTo(Configuration.MAX_ANGLE);
        leftToRight = false;
    }

    public void turnToLeftMaximum() {
        configuration.getSensorMotor().rotateTo(0);
        leftToRight = true;
    }
}
