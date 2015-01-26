public class Marvin {

    private boolean running = true;
    private Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() {

        while (running) {
            cancelRun();
            configuration.displayInformation();
            configuration.runCurrentStep();
        }
    }

    private void cancelRun() {
        if (configuration.cancel()) {
            running = false;
        }
    }
}
