package ViewModels;

import Helpers.TiReceipt;
import Interfaces.IPriceMatrix;
import Interfaces.ITravelViewModel;
import Models.Journey;
import Models.Passenger;
import Models.TravelPass;

import java.time.LocalDateTime;
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

    public TiReceipt startNewJourney(Journey journey) {
        TravelPass travelPass = this.getLastTravelInDay(journey.getStartTime());

        // calculate the ticket price
        TiReceipt receipt = this.priceMatrix.calculatePrice(this.passenger.getUserType(), travelPass, journey);
        if(receipt.getCost() > this.passenger.getBalance()) {
            receipt = new TiReceipt();
            receipt.setNoEnoughBalance(true);
            return receipt;
        }

        float balance = passenger.getBalance() - receipt.getCost();
        passenger.setBalance(balance);
        if(receipt.getNewTicket()){
            travelPass = new TravelPass();
            travelPass.setTicketCost(receipt.getCost());
            travelPass.setPassType(receipt.getPassType());
            travelPass.setZoneType(receipt.getZoneType());

            String key = this.getDayTextFromDate(journey.getStartTime());
            List<TravelPass> travelPasses = new ArrayList<>();
            travelPasses.add(travelPass);
            this.travels.put(key, travelPasses);
        }

        travelPass.addJourney(journey);
        return receipt;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.travels.values().forEach((travelPasses) -> {
            travelPasses.forEach((travelPass) -> {
                sb.append(travelPass.toString());
            });
        });
        return sb.toString();
    }

    private TravelPass getLastTravelInDay(LocalDateTime date) {
        String strDate = this.getDayTextFromDate(date);
        List<TravelPass> travelPasses = this.travels.get(strDate);
        if(null == travelPasses || travelPasses.size() == 0) {
            return null;
        }

        return travelPasses.get(travelPasses.size() - 1);
    }

    private String getDayTextFromDate(LocalDateTime date) {
        return date.toLocalDate().toString();
    }
}
