import lejos.nxt.Motor;

public class FollowLine implements Step {
    private static final int SILVER_LINE_THRESHOLD = 450;

    @Override
    public void run(Configuration configuration) {
        Motor.B.stop();
        Motor.A.forward();

        if (configuration.getLight().getNormalizedLightValue() < SILVER_LINE_THRESHOLD) {
            Motor.A.stop();
            Motor.B.forward();
        }

    }

}
