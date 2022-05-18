package Services;

import Helpers.Common;
import Helpers.TiReceipt;
import Models.*;
import ViewModels.TravelViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyTiService {
    private UserService userService;
    private String[] tiMainMenus;
    private Map<String, Station> stations;
    private Map<String, TravelViewModel> userTravelViewModels;

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

        String userId = Common.waitUsersChoice("Which user:");
        String startStationName = Common.waitUsersChoice("From what station:");
        String endStationName = Common.waitUsersChoice("To what station:");
        String strDate = Common.waitUsersChoice("What date:");
        String departureTime = Common.waitUsersChoice("Departure time:");
        String arrivalTime = Common.waitUsersChoice("Arrival time:");

        TravelViewModel vm = this.getTravelVMByUserId(userId);
        LocalDateTime localStartDate = MyTiService.getLocalDateTimeFromString(strDate + " " + departureTime);
        LocalDateTime localEndDate = MyTiService.getLocalDateTimeFromString(strDate + " " + arrivalTime);

        Journey journey = new Journey(localStartDate, localEndDate, null, null);
        TiReceipt receipt = vm.startNewJourney(journey);
        MyTiService.printOutTiReceipt(receipt, userId);
    }

    private void handleAddUser() {
        this.userService.requestAddNewUser();
    }

    private void handlTopup(){
    }

    private void handleDisplayingBalance(){

    }

    private void printBalance(String userId) {
        float balance = this.userService.getUserBalance(userId);
        String strMsg = String.format("Credit remaining for %s: %.2f", userId, balance);
        System.out.println(strMsg);
    }

    private void printUserReport(){

    }

    private void updatePricing(){

    }

    private void printStationReport(){}

    private TravelViewModel getTravelVMByUserId(String userId) {
        if(this.userTravelViewModels.containsKey(userId)){
            return this.userTravelViewModels.get(userId);
        }

        Passenger passenger = this.userService.getPassengerById(userId);
        TravelViewModel vm = new TravelViewModel(passenger, new PriceMatrix());

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

            String message = String.format("%s, %s %s Travel Pass purchased for %s for $%.2f", strPassType, strZone, strConcession, userId, receipt.getCost());
            System.out.println(message);

            if(receipt.getExpireTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                System.out.println("Valid until " + receipt.getExpireTime().format(formatter));
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
