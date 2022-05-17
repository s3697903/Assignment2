package Models;

public class Passenger {
    private String userId;
    private float balance = 0.0F;
    private PassengerType userType = PassengerType.ADULT;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;

    public Passenger(String userId, String firstName, String lastName, String email, PassengerType passengerType){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = passengerType;
    }

    public float getBalance() {
        return this.balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String  getUserId() {
        return this.userId;
    }
    public PassengerType getUserType() {
        return this.userType;
    }
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
