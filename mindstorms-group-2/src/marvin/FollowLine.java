package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

public class FollowLine implements Step {

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
        lostNumber = 0;

        int center = (leftBorder + rightBorder) / 2;

        int correctionFactor = ((Configuration.MAX_ANGLE / 2) - center);
        int gainedFactor = (int) (correctionFactor * GAIN);

        RConsole.println("" + gainedFactor);
        movement.correct(gainedFactor);

        // evaluateStraightCase(configuration).adjustCourse(movPrim,
        // caseHistory, lineCenterHistory);
    }

    private void lost(Configuration configuration) {
        lostNumber++;
        searchRightOfLine(configuration);

        // searchLeftOfLine(configuration);
    }

    private void searchRightOfLine(Configuration configuration) {
        SensorDataCollector collector = configuration.getSensorDataCollector();
        collector.turnToLeftMaximum();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();

        leftWheel.rotate(-100 * lostNumber, true);
        rightWheel.stop();

        boolean found = false;
        while (!configuration.isCancel()) {
            int lightValue = configuration.getLight().getNormalizedLightValue();
            if (collector.isBright(lightValue)) {
                leftWheel.stop();
                rightWheel.stop();
                found = true;
                break;
            }
        }
        if (found) {
            return;
        }
        leftWheel.rotate(100 * lostNumber);
        rightWheel.stop();
        leftWheel.stop();
        rightWheel.rotate(-100 * lostNumber, true);

        while (!configuration.isCancel()) {
            int lightValue = configuration.getLight().getNormalizedLightValue();
            if (collector.isBright(lightValue)) {
                leftWheel.stop();
                rightWheel.stop();
                found = true;
                break;
            }
        }
        rightWheel.rotate(100 * lostNumber);
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
