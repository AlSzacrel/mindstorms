package marvin;

import java.io.IOException;

public class Marvin {

    private boolean running = true;
    private final Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() throws IOException {
        configuration.startConsole();
        // RConsole.println("collect data");
        // RConsole.println(configuration.getLines().toString());
        configuration.getMovementPrimitives().crawl();
        while (running) {
            cancelRun();
            configuration.displayInformation();
            // configuration.getMovementPrimitives().crawl();
            // configuration.getMovementPrimitives().drive();
            configuration.runCurrentStep();

            // DataSet dataRow =
            // configuration.getSensorDataCollector().collectDataRow();
            // RConsole.println(dataRow.toString());
            // configuration.getSensorDataCollector().collectData();
            // configuration.getMovementPrimitives().slow();
            // configuration.getMovementPrimitives().drive();
            // configuration.followLine();
            // configuration.followLeftWall();
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
