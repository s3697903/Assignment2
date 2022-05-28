package main.java.services;

import main.java.helpers.TiReceipt;
import main.java.interfaces.IPriceMatrix;
import main.java.models.*;

import javax.print.attribute.standard.PresentationDirection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PriceMatrix implements IPriceMatrix {

    private static float JUNIOR_DISCOUNT = 0.5F;
    private static float SENIOR_DISCOUNT = 0.5F;
    private static float SENIOR_SAT_DISCOUNT = 0.0F;
    private float twoHoursDuration = 2.0F;
    private float[][] prices = new float[][]{{2.5F, 3.5F}, {4.9F, 6.8F}};

    /**
     * Calcuate price based on input parameters.
     * @param passengerType the passenger type
     * @param travelPass the travel pass object
     * @param journey the new journey
     * @return the ticket receipt object.
     */
    public TiReceipt calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey) {
        TiReceipt receipt = new TiReceipt();
        float price = 0.0F;
        float discountRate = PriceMatrix.getDiscountRate(passengerType, journey.getStartTime());
        if(null == travelPass || travelPass.getJourneies().size() <= 0) {
            // 2 hours ticket
            price = prices[0][0];
            receipt.setNewTicket(true);
            receipt.setPassType(TravelPassType.TwoHour);
            receipt.setZoneType(ZoneType.ZONE1);
            receipt.setExpireTime(PriceMatrix.addHoursInSameDay(journey.getStartTime(), this.twoHoursDuration));
        } else if(travelPass.getPassType() == TravelPassType.AllDay) {
            price = 0.0F;
            receipt.setPassType(TravelPassType.AllDay);
            receipt.setZoneType(ZoneType.ZONE1);
            receipt.setNewTicket(false);
            receipt.setExpireTime(PriceMatrix.getMidnightOfDay(travelPass.getJourneies().get(0).getStartTime()));
        } else{
            Journey firstJourney = travelPass.getJourneies().get(0); // should check start time at the first journey.

            // check if new journey fits in 2 hours
            if(PriceMatrix.getDifferHour(journey.getStartTime(), firstJourney.getEndDate()) < this.twoHoursDuration ){
                price = 0.0F;
                receipt.setPassType(TravelPassType.TwoHour);
                receipt.setZoneType(ZoneType.ZONE1);
                receipt.setNewTicket(false);
                receipt.setExpireTime(PriceMatrix.addHoursInSameDay(firstJourney.getStartTime(), this.twoHoursDuration));
            } else {
                // if outside 2 hours
                float allDayTi = prices[1][0];
                float twoHour = prices[0][0];
                if(allDayTi < 2 * twoHour) {
                    price = allDayTi - twoHour;
                    receipt.setPassType(TravelPassType.AllDay);
                    receipt.setExpireTime(PriceMatrix.getMidnightOfDay(firstJourney.getStartTime()));
                } else {
                    price = twoHour;
                    receipt.setPassType(TravelPassType.TwoHour);
                    receipt.setExpireTime(PriceMatrix.addHoursInSameDay(journey.getStartTime(), this.twoHoursDuration));
                }

                receipt.setNewTicket(true);
                receipt.setZoneType(ZoneType.ZONE1);
            }
        }

        receipt.setCost(price * discountRate);
        receipt.setConcession(discountRate > 0.0F);

        return receipt;
    }

    public void set2HoursTicketDuration(float duration){
        if(duration < 2.0)
            return;

        this.twoHoursDuration = duration;
    }
    public void set2HoursTicketPriceForZone1(float price){
        this.prices[0][0] = price;
    }
    public void set2HoursTicketPriceForZone1_2(float price){
        this.prices[0][1] = price;
    }
    public void setAllDayTickePriceForZone1(float price){
        this.prices[1][0] = price;
    }
    public void setAllDayTickePriceForZone1_2(float price){
        this.prices[1][1] = price;
    }

    /**
     * ger discount rate by passenger type and travel time.
     * @param passengerType the passenger type
     * @param travelDate the travel time.
     * @return the discount rate.
     */
    private static float getDiscountRate(PassengerType passengerType, LocalDateTime travelDate) {
        float discountRate = 1.0F;
        switch (passengerType) {
            case ADULT:
                discountRate = 1.0F;
                break;
            case JUNIOR:
                discountRate = JUNIOR_DISCOUNT;
                break;
            case SENIOR:
                discountRate = SENIOR_DISCOUNT;
                break;
        }

        if("SUNDAY".equals(travelDate.getDayOfWeek().toString()) && passengerType == PassengerType.SENIOR) {
            discountRate *= SENIOR_SAT_DISCOUNT;
        }

        return discountRate;
    }

    /**
     * calcuate the difference between two date time.
     * @param startDate the first date time
     * @param endDate the second date time
     * @return the diffenence by hour
     */
    private static long getDifferHour(LocalDateTime startDate, LocalDateTime endDate) {
        return Duration.between(endDate, startDate).toHours();
    }

    /**
     * add hours in a date time.
     * @param dateTime the date time
     * @param hours the hours need to add
     * @return the datetime which added hours. the max value is midnight.
     */
    private static LocalDateTime addHoursInSameDay(LocalDateTime dateTime, float hours) {
        LocalDateTime dt = dateTime.plusMinutes((int)(hours * 60));
        LocalDateTime midnight = PriceMatrix.getMidnightOfDay(dateTime);
        if(dt.compareTo(midnight) > 0) {
            return midnight;
        }

        return dt;
    }

    /**
     * get midnighte datetime
     * @param dateTime the date time.
     * @return the midnight.
     */
    private static LocalDateTime getMidnightOfDay(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = dateTime.toLocalDate().format(formatter);

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime midnight = LocalDateTime.parse(date + " 23:59",formatter);
        return midnight;
    }
}
