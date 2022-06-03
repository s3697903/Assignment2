package main.java.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.java.helpers.Common;
import main.java.interfaces.*;
import main.java.models.Passenger;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML
    protected ListView lstUser;

    @FXML
    protected TextArea taReport;

    @FXML
    protected Button btnClose;

    private IUserViewModel userViewModel;
    private Map<String, ITravelViewModel> userTravelViewModels;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lstUser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                printReport((Passenger) newValue);
            }
        });
    }

    public void setUserViewModel(IUserViewModel userViewModel){
        this.userViewModel = userViewModel;
        this.showUsers(this.userViewModel.getOrderedPassengers());
    }
    public void setUserTravelViewModel(Map<String, ITravelViewModel> userTravelViewModels) {
        this.userTravelViewModels = userTravelViewModels;
    }

    @FXML
    protected void onClose(ActionEvent actionEvent){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void showUsers(List<Passenger> passengerList) {
        ObservableList<Passenger> observableList = FXCollections.observableArrayList();
        passengerList.forEach((item) -> {
            observableList.add(item);
        });

        this.lstUser.setItems(observableList);

        this.lstUser.setCellFactory(param -> new ListCell<Passenger>() {
            @Override
            protected void updateItem(Passenger item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName());
                }
            }
        });
    }

    private void printReport(Passenger passenger){
        taReport.setText("");

        ITravelViewModel vm = this.userTravelViewModels.get(passenger.getUserId());
        String userInfo = String.format("Travel pass for user: %s %s", passenger.getUserId(), passenger.getFullName());
        String message = Common.EMPTY_STR;

        if(vm == null){
            message = "N/A";
        } else {
            message = vm.toString();
        }

        String report = String.format("%s\n%s", userInfo, message);
        this.taReport.setText(report);
    }
}
