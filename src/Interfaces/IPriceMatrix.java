package Interfaces;

import Helpers.PriceInfo;
import Models.*;

public interface IPriceMatrix {

    public PriceInfo calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey);
}
