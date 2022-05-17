package Models;

public class Passenger {
    private int userId = 0;
    private float balance = 0.0F;
    private PassengerType userType = PassengerType.ADULT;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;

    public float getBalance() {
        return this.balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public PassengerType getUserType() {
        return this.userType;
    }
}
