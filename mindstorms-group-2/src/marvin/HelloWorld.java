package marvin;

import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class HelloWorld {

    private static final class PreviousStep implements ButtonListener {
        @Override
        public void buttonReleased(Button b) {
            if (selected) {
                return;
            }
            startStep--;
            startStep = Math.max(startStep, 0);
            updateCurrentStartStep();
        }

        @Override
        public void buttonPressed(Button b) {
        }
    }

    private static final class NextStep implements ButtonListener {
        @Override
        public void buttonReleased(Button b) {
            if (selected) {
                return;
            }
            startStep++;
            startStep = Math.min(startStep, defaultSteps.size() - 1);
            updateCurrentStartStep();
        }

        @Override
        public void buttonPressed(Button b) {
        }
    }

    private static int startStep = 0;
    private static ArrayList<Step> defaultSteps;
    private static boolean selected;

    public static void main(String[] args) throws IOException {
        initializeDefaultSteps();
        Marvin marvin = new Marvin(configuration());
        marvin.drive();
    }

    private static void initializeDefaultSteps() {
        defaultSteps = new ArrayList<Step>();
        defaultSteps.add(new FollowWall());
        defaultSteps.add(new WallToLine());
        defaultSteps.add(new FollowLine());
        defaultSteps.add(new FollowEdge());
        defaultSteps.add(new Elevator());
        defaultSteps.add(new FollowWall());
        defaultSteps.add(new TurnBeforeReel());
        defaultSteps.add(new ReelBoard());
        defaultSteps.add(new TurnBeforeReel());
        defaultSteps.add(new HangingBridge());
        defaultSteps.add(new FollowLine());
        defaultSteps.add(new TurnTable());
        defaultSteps.add(new FollowLine());
        defaultSteps.add(new EmptyStep());
    }

    private static Configuration configuration() throws IOException {
        Configuration configuration = new Configuration();
        selectSteps(configuration);
        return configuration;
    }

    private static void selectSteps(Configuration configuration) {
        PreviousStep privousStep = new PreviousStep();
        NextStep nextStep = new NextStep();
        Button.LEFT.addButtonListener(privousStep);
        Button.RIGHT.addButtonListener(nextStep);
        while (Button.ENTER.isUp() && !configuration.isCancel()) {
            updateCurrentStartStep();
            Delay.msDelay(50);
        }
        if (configuration.isCancel()) {
            System.exit(0);
        }
        selected = true;
        for (int index = startStep; index < defaultSteps.size(); index++) {
            configuration.addStep(defaultSteps.get(index));
        }
        LCD.clear();
        System.out.println("Start step:" + startStep);
        configuration.printSteps();
    }

    private static void updateCurrentStartStep() {
        LCD.clear();
        LCD.drawString("Start step:", 0, 0);
        LCD.drawString(startStep + " " + defaultSteps.get(startStep).getName(), 0, 1);
        LCD.drawString("< > to select", 0, 3);
        LCD.drawString("ENTER to start", 0, 4);
        LCD.drawString("ESCAPE to cancel", 0, 5);
    }

}
