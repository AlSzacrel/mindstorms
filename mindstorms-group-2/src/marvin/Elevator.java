package marvin;

import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.LiftConnection;

public class Elevator implements Step {

    // rot [435,447]
    // grüne [400,423]
    // Colors changed. Red is now darker.
    private static final int THRESHOLD = 350;

    @Override
    public void run(Configuration configuration) {
        MovementPrimitives movement = configuration.getMovementPrimitives();
        configuration.getLight().setFloodlight(false);
        movement.stop();
        try (LiftConnection lift = BluetoothCommunication.connectToLift(configuration)) {
            while (!configuration.isCancel()) {
                DataSet sensorData = configuration.getSensorDataCollector().collectDataRow();
                if (sensorData.size() == 0) {
                    return;
                }
                float color = color(sensorData);
                if (isGreen(color)) {
                    break;
                }
                Delay.msDelay(200);
            }

            // TODO drive into elevator
            movement.drive();
            Delay.msDelay(500);
            movement.stop();
            lift.goDown();
            while (!lift.canExit()) {
                Delay.msDelay(1000);
            }
            movement.drive();
        }
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
        return color > THRESHOLD;
    }

}