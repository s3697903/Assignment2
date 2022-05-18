package Services;

import Helpers.TiReceipt;
import Interfaces.IPriceMatrix;
import Models.*;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PriceMatrix implements IPriceMatrix {

    private static float JUNIOR_DISCOUNT = 0.5F;
    private static float SENIOR_DISCOUNT = 0.5F;
    private static float SENIOR_SAT_DISCOUNT = 0.0F;
    private static float[][] prices = new float[][]{{2.5F, 3.5F}, {4.9F, 6.8F}};

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
            receipt.setExpireTime(PriceMatrix.addHoursInSameDay(journey.getStartTime(), 2));
        } else if(travelPass.getPassType() == TravelPassType.AllDay) {
            price = 0.0F;
            receipt.setNewTicket(false);
            receipt.setExpireTime(PriceMatrix.getMidnightOfDay(travelPass.getJourneies().get(0).getStartTime()));
        } else{
            Journey firstJourney = travelPass.getJourneies().get(0); // should check start time at the first journey.

            // check if new journey fits in 2 hours
            if(PriceMatrix.getDifferHour(journey.getStartTime(), firstJourney.getEndDate()) < 2 ){
                price = 0.0F;
                receipt.setNewTicket(false);
                receipt.setExpireTime(PriceMatrix.addHoursInSameDay(firstJourney.getStartTime(), 2));
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
                    receipt.setExpireTime(PriceMatrix.addHoursInSameDay(journey.getStartTime(), 2));
                }

                receipt.setNewTicket(true);
                receipt.setZoneType(ZoneType.ZONE1);
            }
        }

        receipt.setCost(price * discountRate);
        receipt.setConcession(discountRate > 0.0F);

        return receipt;
    }

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

    private static long getDifferHour(LocalDateTime startDate, LocalDateTime endDate) {
        return Duration.between(endDate, startDate).toHours();
    }

    private static LocalDateTime addHoursInSameDay(LocalDateTime dateTime, Integer hours) {
        LocalDateTime dt = dateTime.plusHours(hours);
        LocalDateTime midnight = PriceMatrix.getMidnightOfDay(dateTime);
        if(dt.compareTo(midnight) > 0) {
            return midnight;
        }

        return dt;
    }

    private static LocalDateTime getMidnightOfDay(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = dateTime.toLocalDate().format(formatter);

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime midnight = LocalDateTime.parse(date + " 23:59",formatter);
        return midnight;
    }
}
