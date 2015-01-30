package tools;

import java.io.IOException;

import lejos.nxt.Button;
import marvin.Configuration;
import marvin.SensorDataCollector;

public class CollectData {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        SensorDataCollector sensorData = new SensorDataCollector(configuration);

        while (Button.ESCAPE.isUp()) {
            sensorData.collectData();
        }

        configuration.save();
    }
}
