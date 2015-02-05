package marvin;

import lejos.nxt.NXTRegulatedMotor;

public class MovementPrimitives {
    private static final float CORRECTION_FACTOR = 0.75f;
    private static final float TURN_FACTOR = 0.5f;
    private static final float SPIN_FACTOR = 1f;
    private static final float SPEED_FACTOR_FULL = 0.5f;
    private static final float BACKUP_FACTOR = SPEED_FACTOR_FULL / 2f;
    private static final float SPEED_FACTOR_SLOW = SPEED_FACTOR_FULL / 2f;
    private static final float SPEED_FACTOR_CRAWL = SPEED_FACTOR_SLOW / 2f;
	private static final float SPEED_FACTOR_STALK = SPEED_FACTOR_CRAWL / 4f;
    private float speed = 0;
    public final Configuration conf;
    private NXTRegulatedMotor leftWheel;
    private NXTRegulatedMotor rightWheel;

    public MovementPrimitives(Configuration conf) {
        this.conf = conf;
        leftWheel = conf.getLeftWheel();
        rightWheel = conf.getRightWheel();
        slow();       
    }

    public void fullSpeed() {
        speed = Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void reelSpeed() {
        speed = SPEED_FACTOR_REEL * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void normalSpeed() {
        speed = SPEED_FACTOR_NORMAL * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void slow() {
        speed = SPEED_FACTOR_SLOW * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void crawl() {
        speed = SPEED_FACTOR_CRAWL * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void stalk() {
        speed = SPEED_FACTOR_STALK * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void drive() {
        leftWheel.setSpeed(speed);
        rightWheel.setSpeed(speed);
        leftWheel.forward();
        rightWheel.forward();
    }

    public void backup() {
        leftWheel.setSpeed(BACKUP_FACTOR * speed);
        rightWheel.setSpeed(BACKUP_FACTOR * speed);
        leftWheel.backward();
        rightWheel.backward();
    }

    public void stop() {
        leftWheel.stop();
        rightWheel.stop();
    }

    public void correctionLeft() {
        leftWheel.setSpeed(CORRECTION_FACTOR * speed);
        rightWheel.setSpeed(speed);
        leftWheel.forward();
        rightWheel.forward();
    }

    public void correctionRight() {
        leftWheel.setSpeed(speed);
        rightWheel.setSpeed(CORRECTION_FACTOR * speed);
        leftWheel.forward();
        rightWheel.forward();
    }

    public void turnLeft() {
        leftWheel.setSpeed(TURN_FACTOR * speed);
        rightWheel.setSpeed(speed);
        leftWheel.forward();
        rightWheel.forward();
    }

    public void turnRight() {
        leftWheel.setSpeed(speed);
        rightWheel.setSpeed(TURN_FACTOR * speed);
        leftWheel.forward();
        rightWheel.forward();
    }

    public void spinLeft() {
        leftWheel.setSpeed(SPIN_FACTOR * speed);
        rightWheel.setSpeed(SPIN_FACTOR * speed);
        leftWheel.rotate(-90, true);
        rightWheel.rotate(90, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    public void spinRight() {
        leftWheel.setSpeed(SPIN_FACTOR * speed);
        rightWheel.setSpeed(SPIN_FACTOR * speed);
        leftWheel.rotate(90, true);
        rightWheel.rotate(-90, true);
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }
    
    public void turnAround(){
    	 leftWheel.rotate(-400, true);
         rightWheel.rotate(400, true);
         leftWheel.waitComplete();
         rightWheel.waitComplete();
    } 

    public void correct(int correctionFactor) {
        leftWheel.setSpeed(speed - correctionFactor);
        rightWheel.setSpeed(speed + correctionFactor);
        leftWheel.forward();
        rightWheel.forward();
    }

    public void resetSpeed() {
        leftWheel.setSpeed(speed);
        rightWheel.setSpeed(speed);
    }

	public void searchBarcode() {
		// TODO Auto-generated method stub
		
	}
}
