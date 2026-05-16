package io.simulator.meter;

public enum LoadProfile {

    LOW(0.3),
    MEDIUM(0.8),
    HIGH(1.8);

    final double baseConsumption;
    
    private LoadProfile(double baseConsumption){
        this.baseConsumption = baseConsumption;
    }

    public static LoadProfile fromHour(int hour){
        if(hour > 23 || hour < 0){
            throw new IllegalArgumentException("Cannot have hour outside of 0-23");
        }
        if(hour >= 17 && hour <= 20) {
            return HIGH;
        }else if((hour >= 6 && hour <= 8) || (hour > 20 && hour <= 22)){
            return MEDIUM;
        }else{
            return LOW;
        }
    }

}
