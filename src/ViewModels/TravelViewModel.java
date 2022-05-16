package ViewModels;

import Interfaces.IPriceMatrix;
import Interfaces.ITravelViewModel;
import Models.Passenger;
import Models.TravelPass;

import java.util.List;

public class TravelViewModel implements ITravelViewModel {
    private Passenger passenger;
    private List<TravelPass> travelPasses;
    private IPriceMatrix priceMatrix;

    public TravelViewModel(IPriceMatrix priceMatrix) {
        this.priceMatrix = priceMatrix;
    }
}
