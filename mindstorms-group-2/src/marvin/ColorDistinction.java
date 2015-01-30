package marvin;

import communication.BluetoothCommunication;
import communication.LiftConnection;

public class ColorDistinction implements Step {

    // rot [435,447]
    // grüne [400,423]
    private static final int CHANGE_THRESH = 428;
    private static final int RED_THRESH = 450;
    private static final int GREEN_THRESH = 400;

    @Override
    public void run(Configuration configuration) {
        configuration.getMovementPrimitives().stop();
        LiftConnection lift = BluetoothCommunication.connectToLift();

        while (!configuration.isCancel()) {
            DataSet sensorData = configuration.getSensorDataCollector().collectDataRow();
            if (sensorData.size() == 0) {
                return;
            }
            // TODO request bluetooth value
            float color = color(sensorData);

            if (isGreen(color)) {
                break;
            }
        }
        // TODO drive into elevator
    }

    private float color(DataSet sensorData) {
        float colorAVG = sensorData.get(sensorData.size() - 1).getLightValue();
        // TODO adjust once also scanning from right to left
        for (int i = 0; i < sensorData.size(); i++) {
            colorAVG = Filter.avgEWMA(colorAVG, sensorData.get(i).getLightValue());
        }
        return colorAVG;
    }

    private boolean isGreen(float color) {
        return color > GREEN_THRESH && color <= CHANGE_THRESH;
    }

    private boolean isRed(float color) {
        return color > CHANGE_THRESH && color < RED_THRESH;
    }

}