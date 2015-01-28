package marvin;
import lejos.nxt.NXTRegulatedMotor;

public class SensorDataCollector {

    private static final int MAX_ANGLE_STEPS = 5;
    private static final int STEP_SIZE = 175 / 5;
    private final Configuration configuration;
    private boolean leftToRight = true;

    public SensorDataCollector(Configuration configuration) {
        this.configuration = configuration;
    }

    public void collectData() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        DataSet dataset = new DataSet(MAX_ANGLE_STEPS);
        if (true == leftToRight) {
            for (int step = 0; step < MAX_ANGLE_STEPS; step++) {
                int currentAngle = STEP_SIZE * step;
                sensorMotor.rotateTo(currentAngle);
                int lightValue = configuration.getLight().getNormalizedLightValue();
                int distance = configuration.getUltraSonic().getDistance();
                leftToRight = !leftToRight;
                dataset.append(new Value(step, lightValue, distance));
            }
        } else {
            for (int step = 4; step >= 0; step--) {
                int currentAngle = STEP_SIZE * step;
                sensorMotor.rotateTo(currentAngle);
                int lightValue = configuration.getLight().getNormalizedLightValue();
                int distance = configuration.getUltraSonic().getDistance();
                leftToRight = !leftToRight;
                dataset.prepend(new Value(step, lightValue, distance));
            }
        }
        configuration.updateSensorData(dataset);
    }
}
