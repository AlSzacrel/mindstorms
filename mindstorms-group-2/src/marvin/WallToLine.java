package marvin;

import lejos.nxt.LightSensor;

public class WallToLine implements Step {

    @Override
    public void run(Configuration configuration) {
        SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
        LightSensor light = configuration.getLight();
        MovementPrimitives movement = configuration.getMovementPrimitives();
        sensorDataCollector.turnToCenter();
        while (sensorDataCollector.isDark(light.getNormalizedLightValue()) && !configuration.isCancel()) {
            movement.drive();
        }
        sensorDataCollector.turnToLeftMaximum();
    }

    @Override
    public String getName() {
        return "WallToLine";
    }

}
