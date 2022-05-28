package main.java.gui;

import main.java.helpers.Common;
import main.java.helpers.Tuple;
import main.java.interfaces.IUserViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.models.Passenger;
import main.java.models.PassengerType;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable{

    @FXML
    protected Button btnSave;

    @FXML
    protected TextField tbFirstName;

    @FXML
    protected TextField tfLastName;

    @FXML
    protected TextField tfEmail;

    @FXML
    protected TextField tfBalance;

    @FXML
    protected ComboBox cbUserType;

    @FXML
    protected TextField tfUserId;

    @FXML
    protected Button btnClose;

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private PassengerType userType;
    private float balance;

    private IUserViewModel userViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> userTypeList = FXCollections.observableArrayList();
        userTypeList.add("Senior");
        userTypeList.add("Adult");
        userTypeList.add("Junior");
        this.cbUserType.setItems(userTypeList);
    }

    public void setUserViewModel(IUserViewModel userViewModel){
        this.userViewModel = userViewModel;
    }

    @FXML
    protected void close(ActionEvent actionEvent){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void save(ActionEvent actionEvent) {
        Tuple<Boolean, String> result = this.validateInput();

        if(!result.first()) {
            this.showAlert(Alert.AlertType.WARNING, "Warning", result.second());
            return;
        }

        Passenger passenger = new Passenger(this.userId, this.firstName,this.lastName, this.email,this.userType,this.balance);
        if(this.userViewModel.addNewUser(passenger)){
            this.showAlert(Alert.AlertType.CONFIRMATION, "Success", "Add a new user successfully");
        } else {
            this.showAlert(Alert.AlertType.ERROR, "Failed", "Add a new user failed");
        }
    }

    private Tuple<Boolean, String > validateInput() {

        this.userId = this.tfUserId.getText();
        if(Common.isTextNullOrEmpty(this.userId)){
            return new Tuple(false, "Please input user id");
        }

        this.firstName = this.tbFirstName.getText();
        if(Common.isTextNullOrEmpty(this.firstName)){
            return new Tuple(false, "Please input first name");
        }

        this.lastName = this.tfLastName.getText();
        if(Common.isTextNullOrEmpty(this.lastName)){
            return new Tuple(false, "Please input last name");
        }

        this.email = this.tfEmail.getText();
        if(Common.isTextNullOrEmpty(this.email)){
            return new Tuple(false, "Please input email");
        }

        try{
            this.balance = Float.parseFloat( this.tfBalance.getText());
        } catch (Exception ex){
            return new Tuple(false, "Please input a valid balance");
        }

        String strUserType = this.cbUserType.getSelectionModel().getSelectedItem().toString();
        if(Common.isTextNullOrEmpty(strUserType)){
            return new Tuple(false, "Please select a user type");
        }

        try{
            this.userType = Common.getPassengerTypeFromText(strUserType);
        }catch (Exception ex){
            return new Tuple(false, "Invalid user type");
        }

        return new Tuple(true, Common.EMPTY_STR);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
