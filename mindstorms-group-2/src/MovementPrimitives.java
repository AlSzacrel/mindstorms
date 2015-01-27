import java.util.LinkedList;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class MovementPrimitives {
	private speed = 0;
	private static final double CORRECTION_FACTOR = 0.75;

	public void fullSpeed() {
		speed = Math.min(leftWheel.getMaxSpeed(),rightWheel.getMaxSpeed());
	}

	public void slow() {
		speed = 0.5*Math.min(leftWheel.getMaxSpeed(),rightWheel.getMaxSpeed());
	}

	public void crawl() {
		speed = 0.25*Math.min(leftWheel.getMaxSpeed(),rightWheel.getMaxSpeed());
	}

	public void drive() {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(speed);
	}

	public void backup() {
		leftWheel.setSpeed(-speed);
		rightWheel.setSpeed(-speed);
	}

	public void stop() {
		speed = 0;
		drive();
	}

	public void correctionLeft() {
		leftWheel.setSpeed(CORRECTION_FACTOR*speed);
		rightWheel.setSpeed(speed);
	}

	public void correctionRight() {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(CORRECTION_FACTOR*speed);
	}

	public void turnLeft() {
		leftWheel.setSpeed(0);
		rightWheel.setSpeed(speed);
	}

	public void turnRight() {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(0);
	}

	public void spinLeft() {
		leftWheel.setSpeed(-speed);
		rightWheel.setSpeed(speed);
	}

	public void spinRight() {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(-speed);
	}
}
