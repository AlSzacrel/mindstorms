package marvin;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

import communication.BluetoothCommunication;
import communication.GateConnection;

public class ReelBoard implements Step {

    private static final int SIDE_WALL_THRESHOLD = 8;
    private static final int GAIN = 2;
    private static final int MAX_CORRECTION = 100;
    private static final int MIN_CORRECTION = -MAX_CORRECTION;

    @Override
    public void run(Configuration configuration) {
        configuration.getMovementPrimitives().stop();
        configuration.getMovementPrimitives().reelSpeed();
        configuration.getMovementPrimitives().resetSpeed();
        configuration.getSensorDataCollector().turnToWallPosition();
        // DifferentialPilot pilot = new
        // DifferentialPilot(DifferentialPilot.WHEEL_SIZE_RCX,
        // DifferentialPilot.WHEEL_SIZE_RCX * 2, configuration.getLeftWheel(),
        // configuration.getRightWheel());
        // pilot.setTravelSpeed(pilot.getMaxTravelSpeed());
        NXTRegulatedMotor leftWheel = configuration.getLeftWheel();
        NXTRegulatedMotor rightWheel = configuration.getRightWheel();
        try (GateConnection gate = BluetoothCommunication.connectToGate(configuration)) {
            leftWheel.rotate(1600, true);
            rightWheel.rotate(1600, true);
            SensorDataCollector sensorDataCollector = configuration.getSensorDataCollector();
            LightSensor light = configuration.getLight();
            boolean lineDetected = false;
            while (!configuration.isCancel() && !lineDetected && leftWheel.isMoving()) {
                followWall(configuration.getMovementPrimitives(), configuration.getUltraSonic());
                // lineDetected =
                // sensorDataCollector.isBright(light.getNormalizedLightValue());
            }
            leftWheel.stop();
            rightWheel.stop();

            // TODO watch distance
            // Delay.msDelay(5000);
            // configuration.getMovementPrimitives().stop();
            gate.passed();
            while (!gate.waitForSuccess()) {
                Delay.msDelay(50);
            }
            System.out.println("Successful passed");
        }
    }

    private void followWall(MovementPrimitives movement, UltrasonicSensor ultraSonic) {
        int distance = ultraSonic.getDistance();
        int correctionFactor = SIDE_WALL_THRESHOLD - distance;
        int gainedCorrection = correctionFactor * GAIN;
        int limittedCorrection = Math.max(gainedCorrection, MIN_CORRECTION);
        limittedCorrection = Math.min(limittedCorrection, MAX_CORRECTION);
        RConsole.println("d: " + distance + " c: " + correctionFactor + " g: " + gainedCorrection + " l: "
                + limittedCorrection);
        movement.correct(limittedCorrection);
    }

    @Override
    public String getName() {
        return "ReelBoard";
    }

}
