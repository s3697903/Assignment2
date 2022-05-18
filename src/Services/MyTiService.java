package Services;

import Helpers.Common;
import Helpers.TiReceipt;
import Helpers.Tuple;
import Interfaces.IPriceMatrix;
import Interfaces.ITravelViewModel;
import Models.*;
import ViewModels.TravelViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyTiService {
    private static Integer MaxCredit = 100;
    private static Integer TopUpPrecise = 5;
    private IPriceMatrix priceMatrix;
    private UserService userService;
    private String[] tiMainMenus;
    private Map<String, Station> stations;
    private Map<String, ITravelViewModel> userTravelViewModels;

    private enum PurchaseFMS {
        INPUT_USER,
        INPUT_DEPARTURE,
        INPUT_ARRIVAL,
        INPUT_DATE,
        INPUT_START_TIME,
        INPUT_END_TIME,
    }

    private enum UpdatePriceFMS {
        INPUT_2HourZone1Price,
        INPUT_2HourZone1_2Price,
        INPUT_2HourDuration,
        INPUT_AllDayZone1Price,
        INPUT_AllDayZone1_2Price,
    }

    public MyTiService() {
        this.initService();
    }

    public void start() {
        boolean quite = false;

        while (!quite) {
            MyTiService.printMenu("Options:", this.tiMainMenus);
            String userInput = Common.waitUsersChoice("What is your selection: ");
            if(userInput.equals("1")){
                this.handleBuyJourney();
            } else if(userInput.equals("2")){
                this.handlTopup();
            } else if(userInput.equals("3")){
                this.handleDisplayingBalance();
            } else if(userInput.equals("4")){
                this.printUserReport();
            } else if(userInput.equals("5")) {
                this.updatePricing();
            } else if(userInput.equals("6")) {
                this.printStationReport();
            } else if(userInput.equals("7")) {
                this.handleAddUser();
            } else if(userInput.equals("8")){
                quite = true;
            } else{
                System.out.println("Sorry, that is an invalid option!");
            }

            System.out.println("\n");
        }
        System.out.println("Goodbye!");
    }

    private void initService() {
        this.tiMainMenus = new String[]{ "1.Buy a Journey for a User", "2.Recharge a MyTi card for a User", "3.Show remaining credit for a User", "4.Print User Reports", "5.Update pricing", "6.Show Station statistics", "7.Add a new User", "8.Quit"};
        this.userService = new UserService();
        this.stations = new HashMap();
        this.userTravelViewModels = new HashMap();
        this.priceMatrix = new PriceMatrix();
        this.initStations();
    }

    private void initStations(){
        Station station = new Station("Central", ZoneType.ZONE1);
        this.stations.put(station.getName(), station);

        station = new Station("Flagstaff", ZoneType.ZONE1);
        this.stations.put(station.getName(), station);

        station = new Station("Richmond", ZoneType.ZONE1);
        this.stations.put(station.getName(), station);

        station = new Station("Lilydale", ZoneType.ZONE1_2);
        this.stations.put(station.getName(), station);

        station = new Station("Epping", ZoneType.ZONE1_2);
        this.stations.put(station.getName(), station);
    }

    private void handleBuyJourney(){
        boolean quite = false;
        PurchaseFMS state = PurchaseFMS.INPUT_USER;
        String userId = "";
        String startStationName = "";
        String endStationName = "";
        String strDate = "";
        String departureTime = "";
        String arrivalTime = "";
        LocalDateTime localStartDate = null;
        LocalDateTime localEndDate = null;

        while (!quite){
            switch (state){
                case INPUT_USER:
                    userId = Common.waitUsersChoice("Which user:");
                    if(!this.userService.hasUser(userId)){
                        System.out.println("Cannot find the user: " + userId);
                    } else {
                        state = PurchaseFMS.INPUT_DEPARTURE;
                    }
                    break;
                case INPUT_DEPARTURE:
                    startStationName = Common.waitUsersChoice("From what station:");
                    if(!this.stations.containsKey(startStationName)){
                        System.out.println("Cannot find the station: " + startStationName);
                    } else {
                        state = PurchaseFMS.INPUT_ARRIVAL;
                    }
                    break;
                case INPUT_ARRIVAL:
                    endStationName = Common.waitUsersChoice("To what station:");
                    if(!this.stations.containsKey(endStationName)){
                        System.out.println("Cannot find the station: " + endStationName);
                    } else {
                        state = PurchaseFMS.INPUT_DATE;
                    }
                    break;
                case INPUT_DATE:
                    strDate = Common.waitUsersChoice("What date(dd/MM/yyyy):");
                    state = PurchaseFMS.INPUT_START_TIME;
                    break;
                case INPUT_START_TIME:
                    departureTime = Common.waitUsersChoice("Departure time(HH:mm):");
                    state = PurchaseFMS.INPUT_END_TIME;
                    break;
                case INPUT_END_TIME:
                    arrivalTime = Common.waitUsersChoice("Arrival time(HH:mm):");

                    // valid the date and time
                    try{
                        localStartDate = MyTiService.getLocalDateTimeFromString(strDate + " " + departureTime);
                        localEndDate = MyTiService.getLocalDateTimeFromString(strDate + " " + arrivalTime);
                        quite = true;
                    } catch (Exception ex) {
                        state = PurchaseFMS.INPUT_DATE;
                        System.out.println("Invalid date and time. Please try again.");
                    }

                    break;
                default:
                    break;
            }
        }

        ITravelViewModel vm = this.getTravelVMByUserId(userId);
        Station departure = this.stations.get(startStationName);
        Station arrival = this.stations.get(endStationName);
        Journey journey = new Journey(localStartDate, localEndDate, departure, arrival);
        TiReceipt receipt = vm.startNewJourney(journey);

        if(receipt != null){
            departure.increaseJourneyCount();
            arrival.increaseJourneyCount();
        }

        MyTiService.printOutTiReceipt(receipt, userId);
        this.printUserBalance(userId);
    }

    private void handleAddUser() {
        this.userService.requestAddNewUser();
    }

    private void handlTopup(){
        String userId = Common.waitUsersChoice("Which user:");
        if(!this.userService.hasUser(userId)){
            System.out.println("Cannot find the user: " + userId);
            return;
        }
        Passenger passenger = this.userService.getPassengerById(userId);

        if(passenger.getBalance() >= MaxCredit){
            System.out.println(String.format("Sorry, the max amount of credit allowed is $%.2f", (float)MaxCredit));
            return;
        }

        while (true){
            String userInput = Common.waitUsersChoice("How much do you want to add: ");
            Tuple<Boolean, Integer> result = Common.convertStr2Int(userInput,true);
            if(!result.first() || result.second() < 0){
                System.out.println("Sorry, that is an invalid option!");
                continue;
            }

            if(result.second() == 0){
                break; // cancel top up
            }

            if(passenger.getBalance() + result.second() > MaxCredit){
                System.out.println(String.format("Sorry, the max amount of credit allowed is $%.2f", (float)MaxCredit));
                continue;
            }

            if (result.second() % TopUpPrecise != 0) {
                System.out.println(String.format("Sorry, you can only add multiples of $%.2f", (float)TopUpPrecise));
                continue;
            }

            float balance = passenger.getBalance() + result.second();
            passenger.setBalance(balance);

            break;
        }

        System.out.println(String.format("Your credit = $%.2f",passenger.getBalance()));
    }

    private void handleDisplayingBalance(){
        String userId = Common.waitUsersChoice("Which user:");
        this.printUserBalance(userId);
    }

    private void printUserBalance(String userId){
        Passenger passenger = this.userService.getPassengerById(userId);
        if(passenger == null) {
            System.out.println("Can not find the user: " + userId);
        } else {
            System.out.println(String.format("User %s balance is $%.2f", userId, passenger.getBalance()));
        }
    }

    private void printUserReport(){
        List<Passenger> passengers = this.userService.getOrderedPassengers();
        passengers.forEach((user) -> {
            ITravelViewModel vm = this.userTravelViewModels.get(user.getUserId());
            System.out.println(String.format("Travel pass for user: %s %s", user.getUserId(), user.getFullName()));
            if(vm == null){
                System.out.println("N/A");
            } else {
                System.out.println(String.format("  %s",vm.toString()));
            }
        });
    }

    private void updatePricing(){
        boolean quite = false;
        UpdatePriceFMS state = UpdatePriceFMS.INPUT_2HourZone1Price;
        while (!quite){
            if(state == UpdatePriceFMS.INPUT_2HourZone1Price){
                String twoHourPriceForZone1 = Common.waitUsersChoice("Please input the price of 2 hours for zone1(tap enter if you don't want to change it): ");
                if(!Common.isTextNullOrEmpty(twoHourPriceForZone1)){
                    try{
                        float price = Float.parseFloat(twoHourPriceForZone1);
                        if(price <= 0.0){
                            throw new Exception();
                        }
                        this.priceMatrix.set2HoursTicketPriceForZone1(price);
                        state = UpdatePriceFMS.INPUT_2HourZone1_2Price;
                    }catch (Exception ex){
                        System.out.println("Sorry, this is an invalid input, please try again.");
                    }
                }
            } else if(state == UpdatePriceFMS.INPUT_2HourZone1_2Price){
                String twoHourPriceForZone1_2 = Common.waitUsersChoice("Please input the price of 2 hours for zone1&2(tap enter if you don't want to change it): ");
                if(!Common.isTextNullOrEmpty(twoHourPriceForZone1_2)){
                    try{
                        float price = Float.parseFloat(twoHourPriceForZone1_2);
                        if(price <= 0.0){
                            throw new Exception();
                        }
                        this.priceMatrix.set2HoursTicketPriceForZone1_2(price);
                        state = UpdatePriceFMS.INPUT_2HourDuration;
                    }catch (Exception ex){
                        System.out.println("Sorry, this is an invalid input, please try again.");
                    }
                }
            } else if(state == UpdatePriceFMS.INPUT_2HourDuration) {
                String twoHourDuration = Common.waitUsersChoice("Please input the duration of 2 hours (tap enter if you don't want to change it): ");
                if(!Common.isTextNullOrEmpty(twoHourDuration)){
                    try{
                        float duration = Float.parseFloat(twoHourDuration);
                        if(duration <= 0.0){
                            throw new Exception();
                        }

                        this.priceMatrix.set2HoursTicketDuration(duration);
                        state = UpdatePriceFMS.INPUT_AllDayZone1Price;
                    }catch (Exception ex){
                        System.out.println("Sorry, this is an invalid input, please try again.");
                    }
                }
            } else if(state == UpdatePriceFMS.INPUT_AllDayZone1Price){
                String allDayPriceForZone1 = Common.waitUsersChoice("Please input the price of all day ticket for zone1(tap enter if you don't want to change it): ");
                if(!Common.isTextNullOrEmpty(allDayPriceForZone1)){
                    try{
                        float price = Float.parseFloat(allDayPriceForZone1);
                        if(price <= 0.0){
                            throw new Exception();
                        }
                        this.priceMatrix.setAllDayTickePriceForZone1(price);
                        state = UpdatePriceFMS.INPUT_AllDayZone1_2Price;
                    }catch (Exception ex){
                        System.out.println("Sorry, this is an invalid input, please try again.");
                    }
                }
            } else if(state == UpdatePriceFMS.INPUT_AllDayZone1_2Price){
                String allDayPriceForZone1_2 = Common.waitUsersChoice("Please input the price of all day ticket for zone1&2(tap enter if you don't want to change it): ");
                if(!Common.isTextNullOrEmpty(allDayPriceForZone1_2)){
                    try{
                        float price = Float.parseFloat(allDayPriceForZone1_2);
                        if(price <= 0.0){
                            throw new Exception();
                        }
                        this.priceMatrix.setAllDayTickePriceForZone1_2(price);
                        quite = true;
                    }catch (Exception ex){
                        System.out.println("Sorry, this is an invalid input, please try again.");
                    }
                }
            } else {
                System.out.println("Sorry, invalid options");
                state = UpdatePriceFMS.INPUT_2HourZone1Price;
            }
        }
    }

    private void printStationReport(){}

    private ITravelViewModel getTravelVMByUserId(String userId) {
        if(this.userTravelViewModels.containsKey(userId)){
            return this.userTravelViewModels.get(userId);
        }

        Passenger passenger = this.userService.getPassengerById(userId);
        TravelViewModel vm = new TravelViewModel(passenger, this.priceMatrix);

        this.userTravelViewModels.put(userId, vm);

        return vm;
    }

    /**
     * print menu
     * @param instruction an instruction message
     * @param menus the menus
     */
    private static void printMenu(String instruction, String[] menus){
        System.out.println(instruction);
        for (String menu : menus) {
            System.out.println(menu);
        }
    }

    private static LocalDateTime getLocalDateTimeFromString(String formatTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(formatTime, formatter);
        return dateTime;
    }

    private static void printOutTiReceipt(TiReceipt receipt, String userId) {
        if(receipt.getNoEnoughBlance()) {
            System.out.println("You don't have enough balance.");
            return;
        }

        if(receipt.getNewTicket()) {

            String strPassType = MyTiService.getPassTypeText(receipt.getPassType());
            String strZone = MyTiService.getZoneTypeText(receipt.getZoneType());
            String strConcession = receipt.getConcession()? "(Concession)" : "";

            String message = String.format("%s %s %s Travel Pass purchased for %s for $%.2f", strPassType, strZone, strConcession, userId, receipt.getCost());
            System.out.println(message);

            if(receipt.getExpireTime() != null) {
                System.out.println("Valid until " + Common.convertLocalDateTimeToString(receipt.getExpireTime()));
            }
        } else {

            String strPassType = MyTiService.getPassTypeText(receipt.getPassType());
            String strZone = MyTiService.getZoneTypeText(receipt.getZoneType());
            String message = String.format("Current %s %s Travel Pass still valid", strPassType, strZone);
            System.out.println(message);
        }
    }

    private static String getPassTypeText(TravelPassType passType){
        String strPassType = "";
        if(passType == TravelPassType.TwoHour) {
            strPassType = "2 Hours";
        } else if(passType == TravelPassType.AllDay) {
            strPassType = "All day";
        }

        return strPassType;
    }

    private static String getZoneTypeText(ZoneType zoneType){
        String strZone = "";
        if(zoneType == ZoneType.ZONE1) {
            strZone = "Zone 1";
        } else if(zoneType == ZoneType.ZONE1_2) {
            strZone = "Zone 1&2";
        }

        return strZone;
    }
}
