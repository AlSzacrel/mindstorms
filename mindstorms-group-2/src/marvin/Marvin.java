package marvin;

import java.io.IOException;

public class Marvin {

    private boolean running = true;
    private final Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() throws IOException {
        // RConsole.openUSB(0);
        // RConsole.println("collect data");
        // RConsole.println(configuration.getLines().toString());
        while (running) {
            cancelRun();
            configuration.displayInformation();
            configuration.getSensorDataCollector().collectData();
            // configuration.getMovementPrimitives().slow();
            // configuration.getMovementPrimitives().drive();
            configuration.followLine();
            // Delay.msDelay(500);
            // configuration.getMovementPrimitives().stop();
        }
        configuration.save();
    }

    private void cancelRun() {
        if (configuration.isCancel()) {
            running = false;
        }
    }
}
