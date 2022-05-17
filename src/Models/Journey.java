package Models;

import java.util.Date;

public class Journey {
    private Date startTime;
    private Date endDate;
    private Station departureStation;
    private Station arrivalStation;

    public Journey(Date startTime, Date endDate, Station departure, Station arrival){
        this.startTime = startTime;
        this.endDate = endDate;
        this.departureStation = departure;
        this.arrivalStation = arrival;
    }

    public Date getStartTime() {
        return this.startTime;
    }
    public Date getEndDate() {
        return this.endDate;
    }
}
