package Interfaces;

import Helpers.TiReceipt;
import Models.*;

public interface IPriceMatrix {

    public TiReceipt calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey);
}
