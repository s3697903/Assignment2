package main.java.tests;

import main.java.helpers.TiReceipt;
import main.java.interfaces.IPriceMatrix;
import main.java.models.*;
import main.java.services.PriceMatrix;
import main.java.viewmodels.TravelViewModel;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TravelViewModelTest {

    @Test
    public void testOneJourney() {

        // Prepare
        Passenger passenger = this.getMockupPassenger();
        IPriceMatrix priceMatrix = this.getMockupPriceMatrix();
        TravelViewModel vm = new TravelViewModel(passenger, priceMatrix);
        Station departure = new Station(1,"Central", ZoneType.ZONE1);
        Station arrival = new Station(2,"Flagstaff", ZoneType.ZONE1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse("19/05/2022 15:00", formatter);
        LocalDateTime endTime = LocalDateTime.parse("19/05/2022 15:05", formatter);
        LocalDateTime expiredTime = LocalDateTime.parse("19/05/2022 17:00", formatter);
        Journey journey = new Journey(startTime, endTime, departure, arrival);


        TiReceipt expectedReceipt = new TiReceipt();
        expectedReceipt.setCost(1.25F);
        expectedReceipt.setNewTicket(true);
        expectedReceipt.setPassType(TravelPassType.TwoHour);
        expectedReceipt.setConcession(true);
        expectedReceipt.setNoEnoughBalance(false);
        expectedReceipt.setExpireTime(expiredTime);
        expectedReceipt.setZoneType(ZoneType.ZONE1);

        // Action
        TiReceipt actualReceipt = vm.startNewJourney(journey);

        // Assert
        assertEquals(expectedReceipt.getCost(),           actualReceipt.getCost());
        assertEquals(expectedReceipt.getPassType(),       actualReceipt.getPassType());
        assertEquals(expectedReceipt.getZoneType(),       actualReceipt.getZoneType());
        assertEquals(expectedReceipt.getNewTicket(),      actualReceipt.getNewTicket());
        assertEquals(expectedReceipt.getConcession(),     actualReceipt.getConcession());
        assertEquals(expectedReceipt.getNoEnoughBlance(), actualReceipt.getNoEnoughBlance());
        assertEquals(expectedReceipt.getExpireTime(),     actualReceipt.getExpireTime());
    }

    @Test
    public void testTwoJourney() {

        // Prepare
        Passenger passenger = this.getMockupPassenger();
        IPriceMatrix priceMatrix = this.getMockupPriceMatrix();
        TravelViewModel vm = new TravelViewModel(passenger, priceMatrix);
        Station departure = new Station(1, "Central", ZoneType.ZONE1);
        Station arrival = new Station(2, "Flagstaff", ZoneType.ZONE1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse("19/05/2022 15:00", formatter);
        LocalDateTime endTime = LocalDateTime.parse("19/05/2022 15:05", formatter);
        LocalDateTime expiredTime = LocalDateTime.parse("19/05/2022 17:00", formatter);
        Journey journey = new Journey(startTime, endTime, departure, arrival);
        TiReceipt actualReceipt = vm.startNewJourney(journey);

        startTime = LocalDateTime.parse("19/05/2022 16:00", formatter);
        endTime = LocalDateTime.parse("19/05/2022 16:05", formatter);
        expiredTime = LocalDateTime.parse("19/05/2022 17:00", formatter);
        journey =  new Journey(startTime, endTime, departure, arrival);

        actualReceipt = vm.startNewJourney(journey);

        // Expected
        TiReceipt expectedReceipt = new TiReceipt();
        expectedReceipt.setCost(0.0F);
        expectedReceipt.setNewTicket(false);
        expectedReceipt.setPassType(TravelPassType.TwoHour);
        expectedReceipt.setConcession(true);
        expectedReceipt.setNoEnoughBalance(false);
        expectedReceipt.setExpireTime(expiredTime);
        expectedReceipt.setZoneType(ZoneType.ZONE1);

        // Assert
        assertEquals(expectedReceipt.getCost(),           actualReceipt.getCost());
        assertEquals(expectedReceipt.getPassType(),       actualReceipt.getPassType());
        assertEquals(expectedReceipt.getZoneType(),       actualReceipt.getZoneType());
        assertEquals(expectedReceipt.getNewTicket(),      actualReceipt.getNewTicket());
        assertEquals(expectedReceipt.getConcession(),     actualReceipt.getConcession());
        assertEquals(expectedReceipt.getNoEnoughBlance(), actualReceipt.getNoEnoughBlance());
        assertEquals(expectedReceipt.getExpireTime(),     actualReceipt.getExpireTime());
    }

    private Passenger getMockupPassenger(){
        Passenger passenger = new Passenger("lc", "Lawrence", "Cavedon", "lawrence.cavedon@rmit.edu.au", PassengerType.SENIOR, 20.0F);
        return passenger;
    }

    private IPriceMatrix getMockupPriceMatrix() {
        return new PriceMatrix();
    }
}