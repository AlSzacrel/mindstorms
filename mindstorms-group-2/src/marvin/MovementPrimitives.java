package marvin;

public class MovementPrimitives {
    private static final float CORRECTION_FACTOR = 0.75f;
    private static final float TURN_FACTOR = 0.5f;
    private static final float SPIN_FACTOR = 1f;
    private static final float SPEED_FACTOR_REEL = 0.75f;
    private static final float SPEED_FACTOR_NORMAL = 0.5f;
    private static final float BACKUP_FACTOR = SPEED_FACTOR_NORMAL / 2f;
    private static final float SPEED_FACTOR_SLOW = SPEED_FACTOR_NORMAL / 2f;
    private static final float SPEED_FACTOR_CRAWL = SPEED_FACTOR_SLOW / 2f;
    private static final float SPEED_FACTOR_STALK = SPEED_FACTOR_CRAWL / 4f;
    private float speed = 0;
    public final Configuration conf;

    public MovementPrimitives(Configuration conf) {
        this.conf = conf;
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
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void backup() {
        conf.getLeftWheel().setSpeed(BACKUP_FACTOR * speed);
        conf.getRightWheel().setSpeed(BACKUP_FACTOR * speed);
        conf.getLeftWheel().backward();
        conf.getRightWheel().backward();
    }

    public void stop() {
        conf.getLeftWheel().stop();
        conf.getRightWheel().stop();
    }

    public void correctionLeft() {
        conf.getLeftWheel().setSpeed(CORRECTION_FACTOR * speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void correctionRight() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(CORRECTION_FACTOR * speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void turnLeft() {
        conf.getLeftWheel().setSpeed(TURN_FACTOR * speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void turnRight() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(TURN_FACTOR * speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void spinLeft() {
        conf.getLeftWheel().setSpeed(SPIN_FACTOR * speed);
        conf.getRightWheel().setSpeed(SPIN_FACTOR * speed);
        conf.getLeftWheel().rotate(-90, true);
        conf.getRightWheel().rotate(90, true);
        conf.getLeftWheel().waitComplete();
        conf.getRightWheel().waitComplete();
    }

    public void spinRight() {
        conf.getLeftWheel().setSpeed(SPIN_FACTOR * speed);
        conf.getRightWheel().setSpeed(SPIN_FACTOR * speed);
        conf.getLeftWheel().rotate(90, true);
        conf.getRightWheel().rotate(-90, true);
        conf.getLeftWheel().waitComplete();
        conf.getRightWheel().waitComplete();
    }

    public void correct(int correctionFactor) {
        conf.getLeftWheel().setSpeed(speed - correctionFactor);
        conf.getRightWheel().setSpeed(speed + correctionFactor);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void resetSpeed() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(speed);
    }

    public void searchBarcode() {
        // TODO Auto-generated method stub

    }
}
