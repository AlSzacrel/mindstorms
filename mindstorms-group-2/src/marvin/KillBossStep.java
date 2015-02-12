package marvin;

import lejos.util.Delay;

public class KillBossStep implements Step {

    @Override
    public void run(Configuration configuration) {
        configuration.getMovementPrimitives().stop();
        Delay.msDelay(1000);
        configuration.getMovementPrimitives().drive();
        configuration.getMovementPrimitives().fullSpeed();
    }

    @Override
    public String getName() {
        return "EmptyStep";
    }

}
