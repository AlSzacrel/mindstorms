package tools;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Forward {

    public static void main(String[] args) {
        RConsole.openUSB(0);
        NXTRegulatedMotor sensorMotor = Motor.A;
        sensorMotor.setSpeed(0.1f * sensorMotor.getMaxSpeed());
        RConsole.println("restore until sensor is left");
        sensorMotor.backward();
        sensorMotor.setStallThreshold(1, 25);
        sensorMotor.waitComplete();
        sensorMotor.stop();
        Delay.msDelay(1000);
        RConsole.println("stopped");
        Delay.msDelay(1000);
        sensorMotor.resetTachoCount();
        sensorMotor.forward();
        Delay.msDelay(1000);
        sensorMotor.stop();
        RConsole.println("isStalled (reset):" + sensorMotor.isStalled());
        RConsole.println("Sensor is complete left");
    }

}
