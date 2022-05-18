package Models;

import java.time.LocalDateTime;
import java.util.Date;

public class Journey {
    private LocalDateTime startTime;
    private LocalDateTime endDate;
    private Station departureStation;
    private Station arrivalStation;

    public Journey(LocalDateTime startTime, LocalDateTime endDate, Station departure, Station arrival){
        this.startTime = startTime;
        this.endDate = endDate;
        this.departureStation = departure;
        this.arrivalStation = arrival;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }
    public LocalDateTime getEndDate() {
        return this.endDate;
    }
    public Station getDepartureStation() {return this.departureStation;}
    public Station getArrivalStation() {return this.arrivalStation;}
}
