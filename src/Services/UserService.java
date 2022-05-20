package Services;

import Helpers.Common;
import Models.Passenger;
import Models.PassengerType;
import ViewModels.TravelViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * user service
 */
public class UserService {

    private Map<String, Passenger> passengers;
    private Map<Passenger, TravelViewModel> passengerTravel;

    public UserService() {
        this.initService();
    }

    /**
     * add new user
     */
    public void requestAddNewUser() {
        boolean quite = false;
        while (!quite){
            System.out.println("Please input the new user information\n");
            String userInput = Common.waitUsersChoice("Please follow this format: [userId] [first name] [last name] [email] [Senior|Junior|Adult]:");
            String[] components = userInput.split("\\s+");
            if(components.length != 5) {
                System.out.println("Invalid input");
                continue;
            }

            String strPassType = components[4];
            PassengerType passType = PassengerType.ADULT;
            if(strPassType.equals("Senior")){
                passType = PassengerType.SENIOR;
            } else if(strPassType.equals("Junior")) {
                passType = PassengerType.JUNIOR;
            } else if(strPassType.equals("Adult")) {
                passType = PassengerType.ADULT;
            } else {
                System.out.println("Invalid input");
                continue;
            }

            if(this.passengers.containsKey(components[0])) {
                System.out.println("User id already existed. Please try another user id.");
                continue;
            }

            Passenger passenger = new Passenger(components[0], components[1], components[2], components[3], passType);
            this.passengers.put(passenger.getUserId(), passenger);
            quite = true;
        }
    }

    public Passenger getPassengerById(String userId) {
        return this.passengers.get(userId);
    }

    public List<Passenger> getOrderedPassengers() {
        return new ArrayList(this.passengers.values());
    }

    public boolean hasUser(String userId) {
        return this.passengers.containsKey(userId);
    }

    private void initService() {

        this.passengers = new HashMap();
        this.passengerTravel = new HashMap();

        Passenger passenger = new Passenger("lc", "Lawrence", "Cavedon", "lawrence.cavedon@rmit.edu.au", PassengerType.SENIOR);
//        passenger.setBalance(20.0F);
        this.passengers.put(passenger.getUserId(), passenger);

        passenger = new Passenger("vm", "Xiang", "Li", "vuhuy.mai@rmit.edu.au", PassengerType.ADULT);
        this.passengers.put(passenger.getUserId(), passenger);
    }
}


