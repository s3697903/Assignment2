package main.java.viewmodels;

import main.java.interfaces.IDataSourceListener;
import main.java.interfaces.IUserViewModel;
import main.java.models.Passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserViewModel implements IUserViewModel {

    private IDataSourceListener dataSource = null;
    private Map<String, Passenger> passengers;

    public UserViewModel() {
        this.passengers = new HashMap();
    }

    public void setDataSourceListener(IDataSourceListener dataSourceListener){
        this.dataSource = dataSourceListener;
    }

    public boolean addNewUser(Passenger passenger) {
        if(passenger == null){
            return false;
        }

        if(this.passengers.containsKey(passenger.getUserId())) {
            return false;
        }

        this.passengers.put(passenger.getUserId(), passenger);

        if(this.dataSource != null){
            this.dataSource.didDataSourceChanged();
        }

        return true;
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
}
