package Models;

import java.util.ArrayList;
import java.util.List;

public class TravelPass {
    private float ticketCost = 0.0F;
    private TravelPassType passType;
    private ZoneType zoneType;
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
}
