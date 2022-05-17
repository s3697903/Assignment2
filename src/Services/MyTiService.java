package Services;

import Helpers.Common;
import Models.Passenger;
import Models.Station;

import java.util.*;

public class MyTiService {
    private UserService userService;
    private String[] tiMainMenus;
    private List<Station> stations;
    public MyTiService() {
        this.initService();
    }

    public void start() {

        MyTiService.printMenu("Options:", this.tiMainMenus);
        boolean quite = false;

        while (!quite) {
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
        }
        System.out.println("Goodbye!");
    }

    private void initService() {
        this.tiMainMenus = new String[]{ "1.Buy a Journey for a User", "2.Recharge a MyTi card for a User", "3.Show remaining credit for a User", "4.Print User Reports", "5.Update pricing", "6.Show Station statistics", "7.Add a new User", "8.Quit"};
        this.userService = new UserService();
        this.stations = new ArrayList();
    }
    private void handleBuyJourney(){
    }

    private void handleAddUser() {
        this.userService.requestAddNewUser();
    }

    private void handlTopup(){
    }

    private void handleDisplayingBalance(){

    }

    private void printUserReport(){

    }

    private void updatePricing(){

    }

    private void printStationReport(){

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
}
