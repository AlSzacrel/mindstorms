public class MovementPrimitives {
    private static final float CORRECTION_FACTOR = 0.75f;
    private float speed = 0;
    public final Configuration conf;

    public MovementPrimitives(Configuration conf) {
        this.conf = conf;
    }

    public void fullSpeed() {
        speed = 0.5f * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void slow() {
        speed = 0.25f * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void crawl() {
        speed = 0.125f * Math.min(conf.getLeftWheel().getMaxSpeed(), conf.getRightWheel().getMaxSpeed());
    }

    public void drive() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().forward();
    }

    public void backup() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().backward();
        conf.getRightWheel().backward();
    }

    public void stop() {
        speed = 0;
        drive();
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
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().stop();
        conf.getRightWheel().forward();
    }

    public void turnRight() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().stop();
    }

    public void spinLeft() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().backward();
        conf.getRightWheel().forward();
    }

    public void spinRight() {
        conf.getLeftWheel().setSpeed(speed);
        conf.getRightWheel().setSpeed(speed);
        conf.getLeftWheel().forward();
        conf.getRightWheel().backward();
    }
}
