import lejos.nxt.NXTRegulatedMotor;

public class SensorData {

    private static final int MAX_ANGLE_STEPS = 5;
    private static final int STEP_SIZE = 130 / 5;
    private Configuration configuration;

    public SensorData(Configuration configuration) {
        this.configuration = configuration;
    }

    public void collectData() {
        NXTRegulatedMotor sensorMotor = configuration.getSensorMotor();
        DataSet dataset = new DataSet(MAX_ANGLE_STEPS);
        for (int step = 0; step < MAX_ANGLE_STEPS; step++) {
            int currentAngle = STEP_SIZE * step;
            sensorMotor.rotateTo(currentAngle);
            int lightValue = configuration.getLight().getNormalizedLightValue();
            dataset.add(new Value(currentAngle, lightValue));
        }
        configuration.updateSensorData(dataset);
    }
}
