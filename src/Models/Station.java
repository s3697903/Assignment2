package Models;

import java.util.Date;

public class Station {
    private int stationId;
    private String name;
    private ZoneType zoneType = ZoneType.ZONE1;
    private int journeyCount;

    public Station(String stationName, ZoneType zoneType) {
        this.name = stationName;
        this.zoneType = zoneType;
    }

    public String getName(){
        return name;
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void increaseJourneyCount() {
        this.journeyCount += 1;
    }
}
