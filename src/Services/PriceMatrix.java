package Services;

import Helpers.PriceInfo;
import Interfaces.IPriceMatrix;
import Models.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PriceMatrix implements IPriceMatrix {

    private static float JUNIOR_DISCOUNT = 0.5F;
    private static float SENIOR_DISCOUNT = 0.5F;
    private static float SENIOR_SAT_DISCOUNT = 0.0F;
    private static float[][] prices = new float[][]{{2.5F, 3.5F}, {4.9F, 6.8F}};

    public PriceInfo calculatePrice(PassengerType passengerType, TravelPass travelPass, Journey journey) {
        PriceInfo priceInfo = new PriceInfo();
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

        return priceInfo;
    }

    private static float getDiscountRate(PassengerType passengerType, Date travelDate) {
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

        LocalDate localDate = LocalDate.from(travelDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        if("SUNDAY".equals(localDate.getDayOfWeek().toString()) && passengerType == PassengerType.SENIOR) {
            discountRate *= SENIOR_SAT_DISCOUNT;
        }

        return discountRate;
    }

    private static int getDifferHour(Date startDate, Date endDate) {
        long dayM = 1000 * 24 * 60 * 60;
        long hourM = 1000 * 60 * 60;
        long differ = endDate.getTime() - startDate.getTime();
        long hour = differ % dayM / hourM + 24 * (differ / dayM);
        return Integer.parseInt(String.valueOf(hour));
    }
}
