package Services;

import Helpers.TiReceipt;
import Interfaces.IPriceMatrix;
import Models.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class PriceMatrix implements IPriceMatrix {

    private static float JUNIOR_DISCOUNT = 0.5F;
    private static float SENIOR_DISCOUNT = 0.5F;
    private static float SENIOR_SAT_DISCOUNT = 0.0F;
    private static float[][] prices = new float[][]{{2.5F, 3.5F}, {4.9F, 6.8F}};

    public TiReceipt calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey) {
        TiReceipt priceInfo = new TiReceipt();
        float price = 0.0F;
        float discountRate = PriceMatrix.getDiscountRate(passengerType, journey.getStartTime());
        if(null == travelPass || travelPass.getJourneies().size() <= 0) {
            // 2 hours ticket
            price = prices[0][0];
            priceInfo.setNewTicket(true);
            priceInfo.setPassType(TravelPassType.TwoHour);
            priceInfo.setZoneType(ZoneType.ZONE1);
        } else if(travelPass.getPassType() == TravelPassType.AllDay) {
            price = 0.0F;
            priceInfo.setNewTicket(false);
        } else{
            Journey firstJourney = travelPass.getJourneies().get(0); // should check start time at the first journey.

            // check if new journey fits in 2 hours
            if(PriceMatrix.getDifferHour(journey.getStartTime(), firstJourney.getEndDate()) < 2 ){
                price = 0.0F;
                priceInfo.setNewTicket(false);
            } else {
                // if outside 2 hours
                float allDayTi = prices[1][0];
                float twoHour = prices[0][0];
                if(allDayTi < 2 * twoHour) {
                    price = allDayTi - twoHour;
                    priceInfo.setPassType(TravelPassType.AllDay);
                } else {
                    price = twoHour;
                    priceInfo.setPassType(TravelPassType.TwoHour);
                }

                priceInfo.setNewTicket(true);
                priceInfo.setZoneType(ZoneType.ZONE1);
            }
        }

        priceInfo.setCost(price * discountRate);
        priceInfo.setConcession(discountRate > 0.0F);

        return priceInfo;
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
}
