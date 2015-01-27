import lejos.nxt.Motor;

public class FollowLine implements Step {

    private enum Direction {
        LEFT() {
            @Override
            public void switchDirection() {
                Motor.B.stop();
                Motor.A.forward();
            }
        },
        RIGHT() {
            @Override
            public void switchDirection() {
                Motor.A.stop();
                Motor.B.forward();
            }
        };

        public abstract void switchDirection();
    }

    private static final int SILVER_LINE_THRESHOLD = 375;

    private Direction last;

    @Override
    public void run(Configuration configuration) {
        Direction currentDirection = configuration.getLight().getNormalizedLightValue() < SILVER_LINE_THRESHOLD ? Direction.LEFT
                : Direction.RIGHT;
        if (currentDirection == last) {
            return;
        }
        currentDirection.switchDirection();
        last = currentDirection;
    }
}
