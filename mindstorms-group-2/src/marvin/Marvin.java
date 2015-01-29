package marvin;

import java.io.IOException;

import lejos.nxt.comm.RConsole;

public class Marvin {

    private boolean running = true;
    private final Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() throws IOException {
        RConsole.openUSB(0);
        RConsole.println("restoreSensorPosition");
        configuration.resetSensorPosition();
        RConsole.println("collect data");
        configuration.getSensorDataCollector().collectData();
        RConsole.println(configuration.getLines().toString());
        // while (running) {
        // cancelRun();
        // configuration.displayInformation();
        // configuration.getMovementPrimitives().slow();
        // configuration.getMovementPrimitives().drive();
        // configuration.followLine();
        // Delay.msDelay(500);
        // configuration.getMovementPrimitives().stop();
        // }
    }

    private void cancelRun() {
        if (configuration.isCancel()) {
            running = false;
        }
    }
}
