package io.simulator.meter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SensorNode {

    private String id;
    private Random random;

    public SensorNode(String id){
        this.id = id;
        random = new Random();
    }

    public SensorData simulateSensorData(){

        String currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
        LoadProfile profile = LoadProfile.fromHour(Integer.parseInt(currentHour));

        double baseConsumption = profile.baseConsumption;
        double stdDev = baseConsumption * 0.15;

        // Scale down to 15 second interval
        double consumption = (baseConsumption + stdDev * random.nextGaussian()) / 240;

        // Convert to kW instantaneous
        double load = consumption / (15.0 / 3600.0);  

        return new SensorData(id, System.currentTimeMillis(), consumption, load);

    }


}
