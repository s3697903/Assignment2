package main.java.interfaces;

import main.java.models.Passenger;
import java.util.List;

public interface IUserViewModel {

    void setDataSourceListener(IDataSourceListener dataSourceListener);

    boolean addNewUser(Passenger passenger);
    Passenger getPassengerById(String userId);

    List<Passenger> getOrderedPassengers();

    boolean hasUser(String userId);
}
