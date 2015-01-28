import java.io.IOException;

public class Marvin {

    private boolean running = true;
    private Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() throws IOException {
        configuration.restoreLastSensorPosition();
        while (running) {
            cancelRun();
            configuration.displayInformation();
        }
        configuration.saveLastSensorPosition();
    }

    private void cancelRun() {
        if (configuration.cancel()) {
            running = false;
        }
    }
}
