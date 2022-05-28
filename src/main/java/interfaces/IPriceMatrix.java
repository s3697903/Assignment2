package main.java.interfaces;

import main.java.models.*;
import main.java.helpers.*;

/**
 * The price matrix interface
 */
public interface IPriceMatrix {

    void set2HoursTicketDuration(float duration);

    void set2HoursTicketPriceForZone1(float price);
    float get2HoursTicketPriceForZone1();

    void set2HoursTicketPriceForZone1_2(float price);
    float get2HoursTicketPriceForZone1_2();

    void setAllDayTicketPriceForZone1(float price);
    float getAllDayTicketPriceForZone1();

    void setAllDayTicketPriceForZone1_2(float price);
    float getAllDayTicketPriceForZone1_2();

    /**
     * Calcuate price based on input parameters.
     * @param passengerType the passenger type
     * @param travelPass the travel pass object
     * @param journey the new journey
     * @return the ticket receipt object.
     */
    TiReceipt calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey);
}
