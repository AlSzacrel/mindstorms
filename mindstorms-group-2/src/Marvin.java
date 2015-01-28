import java.io.IOException;

import lejos.util.Delay;

public class Marvin {

    private boolean running = true;
    private final Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() throws IOException {
        configuration.restoreLastSensorPosition();
        while (running) {
            cancelRun();
            configuration.displayInformation();
            configuration.getSensorDataCollector().collectData();
            configuration.getMovementPrimitives().slow();
            configuration.getMovementPrimitives().drive();
            configuration.followLine();
            Delay.msDelay(500);
            configuration.getMovementPrimitives().stop();
        }
        configuration.saveLastSensorPosition();
    }

    private void cancelRun() {
        if (configuration.cancel()) {
            running = false;
        }
    }
}
