package tools;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Forward {

    public static void main(String[] args) {
        Motor.A.setSpeed(Motor.A.getMaxSpeed());
        Motor.B.setSpeed(Motor.A.getMaxSpeed());
        while (Button.ESCAPE.isUp()) {
            Motor.B.forward();
            Motor.A.forward();
        }
    }

}
