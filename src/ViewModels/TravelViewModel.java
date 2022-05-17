package ViewModels;

import Helpers.PriceInfo;
import Interfaces.IPriceMatrix;
import Interfaces.ITravelViewModel;
import Models.Journey;
import Models.Passenger;
import Models.TravelPass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TravelViewModel implements ITravelViewModel {
    private Passenger passenger;
    private Map<String, List<TravelPass>> travels;
    private IPriceMatrix priceMatrix;

    public TravelViewModel(Passenger passenger, IPriceMatrix priceMatrix) {
        this.travels = new HashMap();
        this.priceMatrix = priceMatrix;
        this.passenger = passenger;
    }

    public boolean addNewJourney(Journey journey) {
        if (this.passenger.getBalance() <= 0.0) {
            return false; // not enough balance.
        }

        TravelPass travelPass = this.getLastTravelInDay(journey.getStartTime());

        // calculate the ticket price
        PriceInfo priceInfo = this.priceMatrix.calculatePrice(this.passenger.getUserType(),travelPass, journey);
        float balance = passenger.getBalance() - priceInfo.getCost();
        if(balance < 0.0F) { // no enough balance.
            return false;
        }

        passenger.setBalance(balance);

        if(priceInfo.getNewTicket()){
            travelPass = new TravelPass();
            String key = this.getDayTextFromDate(journey.getStartTime());
            List<TravelPass> travelPasses = new ArrayList<>();
            travelPasses.add(travelPass);
            this.travels.put(key, travelPasses);
        }

        travelPass.setPassType(priceInfo.getPassType());
        travelPass.setZoneType(priceInfo.getZoneType());
        travelPass.addJourney(journey);
        return true;
    }

    private TravelPass getLastTravelInDay(Date date) {
        String strDate = this.getDayTextFromDate(date);
        List<TravelPass> travelPasses = this.travels.get(strDate);
        if(null == travelPasses || travelPasses.size() == 0) {
            return null;
        }

        return travelPasses.get(travelPasses.size() - 1);
    }

    private String getDayTextFromDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        return dateFormat.format(date);
    }
}
