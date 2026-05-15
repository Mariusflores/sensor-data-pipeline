package io.simulator.pipeline;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.babyredis.client.BabyRedisClient;
import io.simulator.meter.SensorData;
import io.simulator.meter.SensorNode;

public class DataPipeline {

    public static void main(String[] args) {

        Set<BabyRedisClient> activeConnections = new HashSet<>();

        // Create the ExecutorService

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

        Thread shutdown = new Thread(() -> {
            scheduler.close();

            for (BabyRedisClient client : activeConnections) {
                client.close();
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdown);

        for (int i = 0; i < 10; i++) {
            BabyRedisClient client = new BabyRedisClient("localhost", 6379);
            activeConnections.add(client);

            String id = "meter-" + i;
            SensorNode node = new SensorNode(id);
            scheduler.scheduleAtFixedRate(() -> {

                SensorData data = node.simulateSensorData();

                Instant instant = Instant.ofEpochMilli(data.timestamp());
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Oslo"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);

                client.set(
                        String.format("%s:%s", data.sensorId(), formattedDateTime),
                        String.format("%s,%.5f", data.timestamp(), data.consumption()));

            }, 0, 15, TimeUnit.SECONDS);
        }
    }

}
