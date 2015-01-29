package marvin;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class SensorDataCollector {

    private static final int BRIGHT_THRESHOLD = 350;
    // private static final int MAX_ANGLE_STEPS = 5;
    // private static final int STEP_SIZE = 175 / 5;
    private final Configuration configuration;

    private final boolean leftToRight = true;

    public SensorDataCollector(Configuration configuration) {
        this.configuration = configuration;
    }

    public void collectData() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        RConsole.println("set threshold");
        sensorMotor.setStallThreshold(1, 25);
        RConsole.println("forward");
        sensorMotor.setSpeed(0.1f * sensorMotor.getMaxSpeed());
        sensorMotor.forward();
        sensorMotor.waitComplete();
        RConsole.println("get values before while");
        int lastLightValue = configuration.getLight().getNormalizedLightValue();
        int lastAngle = sensorMotor.getTachoCount();
        int darkToBrightAngle = Integer.MIN_VALUE;
        int brightToDarkAngle = Integer.MIN_VALUE;
        RConsole.println("isStalled: " + sensorMotor.isStalled());
        while (!sensorMotor.isStalled() && !configuration.isCancel()) {
            RConsole.println("start loop");
            int lightValue = configuration.getLight().getNormalizedLightValue();
            Integer angle = sensorMotor.getTachoCount();
            if (isDark(lastLightValue) && isBright(lastLightValue)) {
                darkToBrightAngle = (angle + lastAngle) / 2;
            }
            if (isBright(lastLightValue) && isDark(lastLightValue)) {
                brightToDarkAngle = (angle + lastAngle) / 2;
            }
            lastLightValue = lightValue;
            lastAngle = angle;
            Delay.msDelay(50);
            RConsole.println("isStalled: " + sensorMotor.isStalled());
        }
        RConsole.println("finished loop");
        sensorMotor.backward();
        sensorMotor.setSpeed(0.01f * sensorMotor.getMaxSpeed());
        sensorMotor.waitComplete();
        configuration.addNewLine(new LineBorders(darkToBrightAngle, brightToDarkAngle));
        RConsole.println("finished collectData");
    }

    private boolean isBright(int lightValue) {
        return lightValue > BRIGHT_THRESHOLD;
    }

    private boolean isDark(int lightValue) {
        return !isBright(lightValue);
    }
}
