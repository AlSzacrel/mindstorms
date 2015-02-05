package marvin;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.TurnTableConnection;

public class TurnTable extends FollowLine {
	
	private boolean beginning = true;
	private boolean followFirstLine = false;
	private FollowLine followLine = new FollowLine();

    @Override
    public void run(Configuration configuration) {
    	MovementPrimitives movement = configuration.getMovementPrimitives();
    	
    	if (beginning) {
    		movement.slow();
    		movement.drive();
    		Delay.msDelay(500);
    		followLine.lost(configuration);
    		beginning = false;
    		followFirstLine = true;
    		
    	} else if (followFirstLine) {
    		
    		followFirstLine = false;    		
    	}
    	
//        try (TurnTableConnection turnTable = BluetoothCommunication.connectToTurnTable(configuration)) {
//            while (!turnTable.hello()) {
//                Delay.msDelay(1000);
//            }
//
//            // follow a line to drive into
//            while (!configuration.isCancel() && !detectEnd(configuration)) {
//                super.run(configuration);
//            }
//
//            turnTable.turn();
//            while (!turnTable.done()) {
//                Delay.msDelay(1000);
//            }
//
//            // drive out
//            NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
//            NXTRegulatedMotor rightWheel = configuration.getRightWheel();
//            leftWheel.rotate(-800, true);
//            rightWheel.rotate(-800, true);
//            leftWheel.waitComplete();
//            rightWheel.waitComplete();
//            movement.turnAround();
           
//        }
    }

    @Override
    protected boolean detectEnd(Configuration configuration) {
        TouchSensor leftTouchSensor = configuration.getLeftTouchSensor();
        TouchSensor rightTouchSensor = configuration.getRightTouchSensor();
        return leftTouchSensor.isPressed() && rightTouchSensor.isPressed();
    }

    @Override
    public String getName() {
        return "TurnTable";
    }

}
