package main.java.helpers;

import main.java.models.*;
import java.time.LocalDateTime;

/**
 * Ticket receipt class
 */
public class TiReceipt {

    // if this is a new ticket.
    // true: a new ticket purchased.
    // false: it's not a new ticket and it's reused others
    private boolean newTicket;

    private float cost;
    private TravelPassType passType;
    private ZoneType zoneType;
    private boolean concession;
    private boolean noEnoughBalance;

    // ticket expired datetime.
    private LocalDateTime expireTime;

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

    public void setExpireTime(LocalDateTime expireTime){
        this.expireTime = expireTime;
    }

    public LocalDateTime getExpireTime() {
        return this.expireTime;
    }
}
