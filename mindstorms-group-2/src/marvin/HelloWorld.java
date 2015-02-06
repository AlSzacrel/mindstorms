package marvin;

import java.io.IOException;

public class HelloWorld {

    public static void main(String[] args) throws IOException {
        Marvin marvin = new Marvin(configuration());
        marvin.drive();
    }

    private static Configuration configuration() throws IOException {
        Configuration configuration = new Configuration();
        // configuration.addStep(new FollowWall());
        // configuration.addStep(new FollowLine());
        // configuration.addStep(new FollowEdge());
        // configuration.addStep(new Elevator());
        configuration.addStep(new FollowWall());
        configuration.addStep(new TurnBeforeReel());
        configuration.addStep(new ReelBoard());
        configuration.addStep(new HangingBridge());
        return configuration;
    }

}
