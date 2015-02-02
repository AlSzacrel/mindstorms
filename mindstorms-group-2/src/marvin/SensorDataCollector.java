package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
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

    public void collectData() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        sensorMotor.setSpeed(SENSOR_HEAD_SPEED_FACTOR * sensorMotor.getMaxSpeed());
        configuration.addNewLine(scan(sensorMotor));
        RConsole.println(configuration.getLines().toString());
        leftToRight = !leftToRight;
    }

    private LineBorders scan(NXTRegulatedMotor sensorMotor) {
        if (leftToRight) {
            sensorMotor.forward();
        } else {
            sensorMotor.backward();
        }
        int lastLightValue = configuration.getLight().getNormalizedLightValue();
        int lastAngle = sensorMotor.getTachoCount();
        int darkToBrightAngle = Integer.MIN_VALUE;
        int brightToDarkAngle = Integer.MIN_VALUE;
        int minAngle = lastAngle;
        int maxAngle = lastAngle;
        boolean scanComplete = false;

        while (scanComplete == false && sensorMotor.getTachoCount() <= Configuration.MAX_ANGLE
                && sensorMotor.getTachoCount() >= 0 && !configuration.isCancel()) {
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
                scanComplete = true;
            }
            lastLightValue = lightValue;
            lastAngle = angle;
            minAngle = Math.min(minAngle, angle);
            maxAngle = Math.max(maxAngle, angle);
        }
        sensorMotor.stop();
        configuration.write("darkToBright: " + darkToBrightAngle + " brightToDark: " + brightToDarkAngle
                + " lineWidth: " + (brightToDarkAngle - darkToBrightAngle) + ";\r\n\r\n");
        return new LineBorders(darkToBrightAngle, brightToDarkAngle, minAngle, maxAngle);
    }

    public boolean isBright(int lightValue) {
        return lightValue > BRIGHT_THRESHOLD;
    }

    public boolean isDark(int lightValue) {
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

    public void turnToCenter() {
        configuration.getSensorMotor().rotateTo(Configuration.MAX_ANGLE / 2);
    }

    public LineBorders collectLineData() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        sensorMotor.setSpeed(SENSOR_HEAD_SPEED_FACTOR * sensorMotor.getMaxSpeed());
        if (leftToRight) {
            sensorMotor.rotateTo(Configuration.MAX_ANGLE, true);
        } else {
            sensorMotor.rotateTo(0, true);
        }

        LightSensor light = configuration.getLight();
        int lastLightValue = light.getNormalizedLightValue();
        Integer darkToBright = 0;
        Integer brightToDark = Configuration.MAX_ANGLE;
        while (sensorMotor.isMoving() && !configuration.isCancel()) {
            Integer angle = sensorMotor.getPosition();
            int lightValue = light.getNormalizedLightValue();
            if (leftToRight) {
                if (isDark(lastLightValue) && isBright(lightValue)) {
                    darkToBright = angle;
                }
                if (isBright(lastLightValue) && isDark(lightValue)) {
                    brightToDark = angle;
                    sensorMotor.stop();
                    break;
                }
            } else {
                if (isDark(lastLightValue) && isBright(lightValue)) {
                    brightToDark = angle;
                }
                if (isBright(lastLightValue) && isDark(lightValue)) {
                    darkToBright = angle;
                    sensorMotor.stop();
                    break;
                }
            }
            lastLightValue = lightValue;
        }
        sensorMotor.stop();
        leftToRight = !leftToRight;
        return new LineBorders(darkToBright, brightToDark, 0, lastLightValue);
    }
}
