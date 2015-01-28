import lejos.nxt.NXTRegulatedMotor;

public class SensorDataCollector {

    private static final int MAX_ANGLE_STEPS = 5;
    private static final int STEP_SIZE = 175 / 5;
    private final Configuration configuration;

    public SensorDataCollector(Configuration configuration) {
        this.configuration = configuration;
    }

    public void collectData() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        DataSet dataset = new DataSet(MAX_ANGLE_STEPS);
        for (int step = 0; step < MAX_ANGLE_STEPS; step++) {
            int currentAngle = STEP_SIZE * step;
            sensorMotor.rotateTo(currentAngle);
            int lightValue = configuration.getLight().getNormalizedLightValue();
            int distance = configuration.getUltraSonic().getDistance();
            dataset.add(new Value(step, lightValue, distance));
        }
        configuration.updateSensorData(dataset);
    }
}
