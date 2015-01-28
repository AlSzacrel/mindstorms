package tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class Sensor {

    private static final class SaveSensorPosition implements RegulatedMotorListener {

        private final NXTRegulatedMotor sensorArm;
        private final DataOutputStream someFile;
        private final LightSensor light;
        private final UltrasonicSensor ultra;

        public SaveSensorPosition() throws IOException {
            sensorArm = Motor.C;
            light = new LightSensor(SensorPort.S4);
            ultra = new UltrasonicSensor(SensorPort.S2);
            File file = new File("someFileName.txt");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            someFile = new DataOutputStream(new FileOutputStream(file));
        }

        @Override
        public void rotationStopped(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
            // TODO Auto-generated method stub

        }

        @Override
        public void rotationStarted(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
            try {
                someFile.writeInt(sensorArm.getPosition());
                someFile.writeInt(sensorArm.getTachoCount());
                someFile.writeInt(ultra.getDistance());
                someFile.writeInt(light.readNormalizedValue());
                someFile.writeInt(light.readValue());

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NXTRegulatedMotor sensorArm = Motor.C;
        sensorArm.addListener(new SaveSensorPosition());
        LightSensor light = new LightSensor(SensorPort.S4);
        UltrasonicSensor ultra = new UltrasonicSensor(SensorPort.S2);
        ultra.continuous();
        while (Button.ESCAPE.isUp()) {
            LCD.drawInt(sensorArm.getPosition(), 4, 0, 0);
            LCD.drawInt(sensorArm.getTachoCount(), 4, 0, 1);
            LCD.drawInt(ultra.getDistance(), 4, 0, 2);
            LCD.drawInt(light.readNormalizedValue(), 4, 0, 4);
            LCD.drawInt(light.readValue(), 4, 0, 5);

            // sensorArm.rotateTo(120);
            // LCPBTResponder lcpThread = new LCPBTResponder();
            // lcpThread.setDaemon(true);
            // lcpThread.start();
        }
    }
}
