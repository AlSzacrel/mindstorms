package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

public class FollowLine implements Step {

    private static final int FOUND_ANGLE = 30;
    private static final int LOST_ANGLE = 50;
    private static final int SPEED = 150;
    private static final float GAIN = 0.5f;
    private static final int HIGH_THRESHOLD = Configuration.MAX_ANGLE - 5;
    private static final int LOW_THRESHOLD = 5;
    private int lostNumber = 0;

    @Override
    public void run(Configuration configuration) {
        MovementPrimitives movement = configuration.getMovementPrimitives();
        // stupidVersion(configuration);
        LineBorders lineData = configuration.getSensorDataCollector().collectLineData();

        int leftBorder = lineData.getDarkToBright();
        int rightBorder = lineData.getBrightToDark();

        if (leftBorder < LOW_THRESHOLD && rightBorder > HIGH_THRESHOLD) {
            // help we are lost
            lost(configuration);
            return;
        }

        int center = (leftBorder + rightBorder) / 2;

        int correctionFactor = ((Configuration.MAX_ANGLE / 2) - center);
        int gainedFactor = (int) (correctionFactor * GAIN);

        RConsole.println("" + gainedFactor);
        movement.correct(gainedFactor);
    }

    private void resetLost() {
        lostNumber = 0;
    }

    private void lost(Configuration configuration) {
        lostNumber++;
        SensorDataCollector collector = configuration.getSensorDataCollector();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();

        leftWheel.stop();
        rightWheel.stop();
        collector.turnToLeftMaximum();
        leftWheel.rotate(-LOST_ANGLE * lostNumber, true);

        while (leftWheel.isMoving() && !configuration.isCancel()) {
            int lightValue = configuration.getLight().getNormalizedLightValue();
            if (collector.isBright(lightValue)) {
                leftWheel.stop();
                rightWheel.stop();
                leftWheel.rotate(-FOUND_ANGLE);
                resetLost();
                return;
            }
        }
        leftWheel.rotate(LOST_ANGLE * lostNumber);
        rightWheel.stop();
        leftWheel.stop();

        // TODO we should search for line while switching head from left to
        // right
        collector.turnToRightMaximum();
        rightWheel.rotate(-LOST_ANGLE * lostNumber, true);

        while (rightWheel.isMoving() && !configuration.isCancel()) {
            int lightValue = configuration.getLight().getNormalizedLightValue();
            if (collector.isBright(lightValue)) {
                leftWheel.stop();
                rightWheel.stop();
                rightWheel.rotate(-FOUND_ANGLE);
                resetLost();
                return;
            }
        }
        rightWheel.rotate(LOST_ANGLE * lostNumber);
        rightWheel.stop();
        leftWheel.stop();
    }

    private void stupidVersion(Configuration configuration) {
        SensorDataCollector collector = configuration.getSensorDataCollector();
        configuration.getSensorDataCollector().turnToCenter();
        LightSensor light = configuration.getLight();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        leftWheel.setSpeed(SPEED);
        rightWheel.setSpeed(SPEED);

        leftWheel.forward();
        rightWheel.forward();
        while (!configuration.isCancel()) {
            int value = light.getNormalizedLightValue();
            if (collector.isBright(value)) {
                leftWheel.setSpeed(SPEED);
                rightWheel.setSpeed(SPEED / 2);
                leftWheel.forward();
                rightWheel.backward();
            } else {
                leftWheel.setSpeed(SPEED / 2);
                rightWheel.setSpeed(SPEED);
                leftWheel.backward();
                rightWheel.forward();
            }
        }
    }
}
