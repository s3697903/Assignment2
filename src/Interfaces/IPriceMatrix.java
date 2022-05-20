package Interfaces;

import Helpers.TiReceipt;
import Models.*;

/**
 * The price matrix interface
 */
public interface IPriceMatrix {

    void set2HoursTicketDuration(float duration);
    void set2HoursTicketPriceForZone1(float price);
    void set2HoursTicketPriceForZone1_2(float price);
    void setAllDayTickePriceForZone1(float price);
    void setAllDayTickePriceForZone1_2(float price);

    /**
     * Calcuate price based on input parameters.
     * @param passengerType the passenger type
     * @param travelPass the travel pass object
     * @param journey the new journey
     * @return the ticket receipt object.
     */
    TiReceipt calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey);
}
