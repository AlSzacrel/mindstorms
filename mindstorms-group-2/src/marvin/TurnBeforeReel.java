package marvin;

import lejos.nxt.NXTRegulatedMotor;

public class TurnBeforeReel implements Step {

    @Override
    public void run(Configuration configuration) {
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        configuration.getMovementPrimitives().crawl();
        leftWheel.rotate(-400, true);
        rightWheel.rotate(-400, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        turnAround(leftWheel, rightWheel);
        configuration.nextStep();
    }

    private void turnAround(NXTRegulatedMotor leftWheel, NXTRegulatedMotor rightWheel) {
        leftWheel.rotate(515, true);
        rightWheel.rotate(-515, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    @Override
    public String getName() {
        return "TurnBeforeReel";
    }

}
