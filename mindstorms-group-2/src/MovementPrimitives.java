import java.util.LinkedList;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

import conf;

public class MovementPrimitives {
	private static final double CORRECTION_FACTOR = 0.75;
	private int speed = 0;
	public final Configuration conf;
	
	public MovementPrimitives(Configuration conf) 
	{
		this.conf = conf;
	}

	public void fullSpeed() {
		speed = Math.min(conf.getLeftWheel.getMaxSpeed(),conf.getRightWheel.getMaxSpeed());
	}

	public void slow() {
		speed = 0.5*Math.min(conf.getLeftWheel.getMaxSpeed(),conf.getRightWheel.getMaxSpeed());
	}

	public void crawl() {
		speed = 0.25*Math.min(conf.getLeftWheel.getMaxSpeed(),conf.getRightWheel.getMaxSpeed());
	}

	public void drive() {
		conf.getLeftWheel.setSpeed(speed);
		conf.getRightWheel.setSpeed(speed);
	}

	public void backup() {
		conf.getLeftWheel.setSpeed(-speed);
		conf.getRightWheel.setSpeed(-speed);
	}

	public void stop() {
		speed = 0;
		drive();
	}

	public void correctionLeft() {
		conf.getLeftWheel.setSpeed(CORRECTION_FACTOR*speed);
		conf.getRightWheel.setSpeed(speed);
	}

	public void correctionRight() {
		conf.getLeftWheel.setSpeed(speed);
		conf.getRightWheel.setSpeed(CORRECTION_FACTOR*speed);
	}

	public void turnLeft() {
		conf.getLeftWheel.setSpeed(0);
		conf.getRightWheel.setSpeed(speed);
	}

	public void turnRight() {
		conf.getLeftWheel.setSpeed(speed);
		conf.getRightWheel.setSpeed(0);
	}

	public void spinLeft() {
		conf.getLeftWheel.setSpeed(-speed);
		conf.getRightWheel.setSpeed(speed);
	}

	public void spinRight() {
		conf.getLeftWheel.setSpeed(speed);
		conf.getRightWheel.setSpeed(-speed);
	}
}
