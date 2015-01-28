public class Marvin {

    private boolean running = true;
    private Configuration configuration;

    public Marvin(Configuration configuration) {
        this.configuration = configuration;
    }

    public void drive() {
        // TODO Move sensor head to left (use saved position to restore left
        // position)
        // TODO Reset tacho count so that tacho count on left position is 0
        while (running) {
            cancelRun();
            configuration.displayInformation();
        }
    }

    private void cancelRun() {
        if (configuration.cancel()) {
            running = false;
        }
    }
}
