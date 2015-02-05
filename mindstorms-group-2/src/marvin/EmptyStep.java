package marvin;

import lejos.util.Delay;

public class EmptyStep implements Step {

    @Override
    public void run(Configuration configuration) {
        Delay.msDelay(1000);
    }

    @Override
    public String getName() {
        return "EmptyStep";
    }

}
