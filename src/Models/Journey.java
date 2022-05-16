package Models;

import java.util.Date;

public class Journey {
    private Date startTime;
    private Date endDate;
    private Station departureStation;
    private Station arrivalStation;

    public Date getStartTime() {
        return this.startTime;
    }
}
