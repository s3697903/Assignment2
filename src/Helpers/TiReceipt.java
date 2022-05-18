package Helpers;

import Models.TravelPassType;
import Models.ZoneType;

public class TiReceipt {
    private boolean newTicket;
    private float cost;
    private TravelPassType passType;
    private ZoneType zoneType;
    private boolean concession;
    private boolean noEnoughBalance;

    public boolean getNewTicket() {
        return newTicket;
    }
    public void setNewTicket(boolean newTicket) {
        this.newTicket = newTicket;
    }

    public float getCost() {
        return this.cost;
    }

    public void setCost (float cost) {
        this.cost = cost;
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

    public void setZoneType (ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public boolean getConcession() {
        return this.concession;
    }

    public void setConcession(boolean concession) {
        this.concession = concession;
    }

    public void setNoEnoughBalance(boolean enoughBalanc) {
        this.noEnoughBalance = enoughBalanc;
    }

    public boolean getNoEnoughBlance() {
        return this.noEnoughBalance;
    }
}
