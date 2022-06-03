package main.java.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.interfaces.*;
import main.java.models.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {

    @FXML
    protected ListView lstUser;

    @FXML
    protected TextArea taUserDetail;

    @FXML
    protected Label lblBalance;

    @FXML
    protected TextField tfTopup;

    @FXML
    protected Button btnClose;

    private IUserViewModel userViewModel;
    private String curSelectedUserId;

    public void setUserViewModel(IUserViewModel userViewModel){
        this.userViewModel = userViewModel;
        this.showUsers(this.userViewModel.getOrderedPassengers());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lstUser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                showDetail((main.java.models.Passenger) newValue);
            }
        });
    }

    @FXML
    protected void onClose(ActionEvent actionEvent){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onTopup(ActionEvent actionEvent){
        float balance = 0F;
        String errMsg = null;
        try{
            balance = Float.parseFloat( this.tfTopup.getText());
            if(balance <= 0){
                throw new IllegalArgumentException();
            }
        } catch (Exception ex){
            errMsg = "Please input a valid balance";
        }

        if(errMsg != null) {
            this.showAlert(Alert.AlertType.WARNING, "Warning", errMsg);
        }

        Passenger passenger = this.userViewModel.getPassengerById(this.curSelectedUserId);
        float totalBalance = passenger.getBalance() + balance;
        passenger.setBalance(totalBalance);

        this.showDetail(passenger);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    private void showDetail(Passenger passenger) {
        this.curSelectedUserId = passenger.getUserId();
         this.taUserDetail.setText(passenger.toString());
         this.lblBalance.setText(String.format("Balance: $%.2f", passenger.getBalance()));
    }
    private void showUsers(List<main.java.models.Passenger> passengerList) {
        ObservableList<main.java.models.Passenger> observableList = FXCollections.observableArrayList();
        passengerList.forEach((item) -> {
            observableList.add(item);
        });

        this.lstUser.setItems(observableList);

        this.lstUser.setCellFactory(param -> new ListCell<main.java.models.Passenger>() {
            @Override
            protected void updateItem(main.java.models.Passenger item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUserId() + " " + item.getFullName());
                }
            }
        });
    }
}
