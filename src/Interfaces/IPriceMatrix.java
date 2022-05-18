package Interfaces;

import Helpers.TiReceipt;
import Models.*;

public interface IPriceMatrix {

    public void set2HoursTicketDuration(float duration);
    public void set2HoursTicketPriceForZone1(float price);
    public void set2HoursTicketPriceForZone1_2(float price);
    public void setAllDayTickePriceForZone1(float price);
    public void setAllDayTickePriceForZone1_2(float price);
    public TiReceipt calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey);
}
