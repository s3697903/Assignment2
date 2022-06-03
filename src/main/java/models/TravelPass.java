package main.java.models;

import main.java.helpers.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * the travle pass model
 */
public class TravelPass {

    private float ticketCost = 0.0F;
    private TravelPassType passType;
    private ZoneType zoneType;

    // the journeys in this travel pass, as multiple journeys might share one travel pass.
    private List<Journey> journeies;

    public TravelPass() {
        this.journeies = new ArrayList<>();
    }

    public void addJourney(Journey journey) {
        this.journeies.add(journey);
    }

    public float getTicketCost() {
        return this.ticketCost;
    }

    public void setTicketCost(float cost) {
        this.ticketCost = cost;
    }

    public TravelPassType getPassType() {
        return this.passType;
    }

    public void setPassType(TravelPassType passType) {
        this.passType = passType;
    }

    public ZoneType getZoneType() {
        return this.zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public List<Journey> getJourneies() {
        return this.journeies;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.journeies.forEach((journey) -> {
            sb.append(String.format("Cost: $%.2f StartTime: %s EndTime: %s Departure: %s Arrival: %s", this.ticketCost, Common.convertLocalDateTimeToString(journey.getStartTime()), Common.convertLocalDateTimeToString(journey.getEndDate()), journey.getDepartureStation().getName(), journey.getArrivalStation().getName()));
            sb.append("\n");
        });

        return sb.toString();
    }
}
