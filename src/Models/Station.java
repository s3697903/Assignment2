package Models;

/**
 * the station model
 */
public class Station {
    private int stationId;
    private String name;
    private ZoneType zoneType = ZoneType.ZONE1;
    private int departureCount = 0;
    private int arrivalCount = 0;

    public Station(String stationName, ZoneType zoneType) {
        this.name = stationName;
        this.zoneType = zoneType;
    }

    public String toString() {
        String strText = String.format("Station %s in %s has %d journeys started and %d journeys finished.", this.name, this.zoneType, this.departureCount,this.arrivalCount);
        return strText;
    }

    public String getName(){
        return name;
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void increaseDepartureCount() {
        this.departureCount += 1;
    }
    public int getDepartureCount() {return this.departureCount;}

    public void increaseArrivalCount() {
        this.arrivalCount += 1;
    }
    public int getArrivalCount() {return this.arrivalCount;}

}
