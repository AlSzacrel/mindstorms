package marvin;

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
    private boolean foundLeft = false;

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

    private void resetLost(boolean foundLeft) {
        lostNumber = 0;
        this.foundLeft = foundLeft;
    }

    public void lost(Configuration configuration) {
        lostNumber++;
        configuration.getMovementPrimitives().stop();
        configuration.getMovementPrimitives().resetSpeed();
        if (foundLeft) {
            searchLeft(configuration);
            searchRight(configuration);
        } else {
            searchRight(configuration);
            searchLeft(configuration);
        }
        if(lostNumber > 2) {
        	configuration.getMovementPrimitives().resetSpeed();
        	configuration.getMovementPrimitives().drive();
        	boolean barcodeFound = false;
        	while(!barcodeFound && !configuration.isCancel()) {
        		barcodeFound = configuration.getSensorDataCollector().detectBarcode(configuration.getLight());
        	}
        	if(barcodeFound) {
        		configuration.nextStep();
        	}
        }
    }

    private void searchRight(Configuration configuration) {
        SensorDataCollector collector = configuration.getSensorDataCollector();
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();

        // TODO we should search for line while switching head from left to
        // right
        leftWheel.stop();
        rightWheel.stop();
        collector.turnToRightMaximum();
        rightWheel.rotate(-LOST_ANGLE * lostNumber, true);

        while (rightWheel.isMoving() && !configuration.isCancel()) {
            int lightValue = configuration.getLight().getNormalizedLightValue();
            if (collector.isBright(lightValue)) {
                leftWheel.stop();
                rightWheel.stop();
                rightWheel.rotate(-FOUND_ANGLE);
                resetLost(false);
                return;
            }
        }
        rightWheel.rotate(LOST_ANGLE * lostNumber);
        rightWheel.stop();
        leftWheel.stop();
    }

    private void searchLeft(Configuration configuration) {
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
                resetLost(true);
                return;
            }
        }
        leftWheel.rotate(LOST_ANGLE * lostNumber);
        rightWheel.stop();
        leftWheel.stop();
    }
}
