package io.simulator.meter;

public record SensorData(
String sensorId,
long timestamp,
double consumption

) {}
