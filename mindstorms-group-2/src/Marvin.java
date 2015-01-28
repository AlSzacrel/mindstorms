import java.io.IOException;

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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
