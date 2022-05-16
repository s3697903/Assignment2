package Models;

import java.util.ArrayList;
import java.util.List;

public class TravelPass {
    private double ticketCost = 0.0;
    private TravelPassType passType;
    private ZoneType zoneType;
    private List<Journey> journeies;

    public TravelPass() {
        this.journeies = new ArrayList<>();
    }

    public void addJourney(Journey journey) {
        this.journeies.add(journey);
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(double cost) {
        this.ticketCost = cost;
    }
}
