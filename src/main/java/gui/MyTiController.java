package main.java.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.services.PriceMatrix;
import main.java.models.*;
import main.java.viewmodels.*;
import main.java.helpers.*;
import main.java.interfaces.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyTiController implements Initializable, IDataSourceListener {

    @FXML
    protected AnchorPane rootAnchorPane;

    @FXML
    protected ListView lstDeparture;

    @FXML
    protected ListView lstArrival;

    @FXML
    protected DatePicker dpDay;

    @FXML
    protected TextArea txtAreaReceipt;

    @FXML
    protected TextField tfStartTime;

    @FXML
    protected TextField tfEndTime;

    @FXML
    protected ListView lstUser;

    private IStationViewModel stationViewModel;
    private IUserViewModel userViewModel;
    private IPriceMatrix priceMatrix;

    private Map<String, ITravelViewModel> userTravelViewModels;

    private String selectUserId = null;
    private Station selectedDeparture = null;
    private Station selectedArrival = null;
    private LocalDateTime startDatetime = null;
    private LocalDateTime endDatetime = null;
    private Stage stage = null;
    private MenuBar menuBar = null;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.updateMenuBarWidth();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.attachMenuBar();

        this.stationViewModel = new StationViewModel();
        this.userViewModel = new UserViewModel();
        this.priceMatrix = new PriceMatrix();
        this.userTravelViewModels = new HashMap();
        this.showDepartures(this.stationViewModel.getStations());
        this.showArrivals(this.stationViewModel.getStations());

        FileAccessor fileAccessor = new FileAccessor();
        if(fileAccessor.readFile()){
            this.showUsers(fileAccessor.passengerList);
            fileAccessor.passengerList.forEach((user) -> {
                this.userViewModel.addNewUser(user);
            });

            // price
            this.priceMatrix.set2HoursTicketPriceForZone1(fileAccessor.prices[0][0]);
            this.priceMatrix.set2HoursTicketPriceForZone1_2(fileAccessor.prices[0][1]);
            this.priceMatrix.setAllDayTicketPriceForZone1(fileAccessor.prices[1][0]);
            this.priceMatrix.setAllDayTicketPriceForZone1_2(fileAccessor.prices[1][1]);

            this.userViewModel.setDataSourceListener(this);
        }

        return;
    }

    @FXML
    protected void purchaseTicket(ActionEvent actionEvent) {
        Tuple<Boolean, String> result = this.validateInput();
        if(!result.first()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText(result.second());
            alert.show();
            return;
        }

        ITravelViewModel vm = this.getTravelVMByUserId(this.selectUserId);
        Journey journey = new Journey(this.startDatetime, this.endDatetime, this.selectedDeparture, this.selectedArrival);
        TiReceipt receipt = vm.startNewJourney(journey);

        if(receipt != null){
            this.selectedDeparture.increaseDepartureCount();
            this.selectedArrival.increaseArrivalCount();
        }

        String strReceipt = Common.printOutTiReceipt(receipt, this.selectUserId);
        String info = this.txtAreaReceipt.getText();
        if(!Common.isTextNullOrEmpty(info)){
            info = info + "\n----------\n" + strReceipt;
        } else {
            info = strReceipt;
        }
        this.txtAreaReceipt.setText(info);
    }

    @FXML
    protected void quit(ActionEvent actionEvent){
        Platform.exit();
    }

    @FXML
    protected void save(ActionEvent actionEvent) {
        List<Passenger> users = this.userViewModel.getOrderedPassengers();
        StringBuilder sb = new StringBuilder();
        sb.append(FileAccessor.SECTION_STARTER + FileAccessor.USER_SECTION_START + "\n");
        users.forEach(item -> {
            sb.append(item.getUserId());
            sb.append(FileAccessor.DELIMITER);
            sb.append(Common.convertPassengerTypeToLowerText(item.getUserType()));
            sb.append(FileAccessor.DELIMITER);
            sb.append(item.getFullName());
            sb.append(FileAccessor.DELIMITER);
            sb.append(item.getEmail());
            sb.append(FileAccessor.DELIMITER);
            sb.append(item.getBalance());
            sb.append("\n");
        });
        sb.append(FileAccessor.SECTION_STARTER + FileAccessor.PRICES_SECTION_START + "\n");
        sb.append(String.format("2Hour:Zone1:%.2f\n", this.priceMatrix.get2HoursTicketPriceForZone1()));
        sb.append(String.format("2Hour:Zone12:%.2f\n", this.priceMatrix.get2HoursTicketPriceForZone1_2()));
        sb.append(String.format("AllDay:Zone1:%.2f\n", this.priceMatrix.getAllDayTicketPriceForZone1()));
        sb.append(String.format("AllDay:Zone12:%.2f\n", this.priceMatrix.getAllDayTicketPriceForZone1_2()));
        boolean success = new FileAccessor().writeFile(sb.toString());

        Alert alert = new Alert(success ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR);
        alert.setTitle(success? "Successfully" : "Failed");
        alert.setContentText(success? "Successfully saved into file" : "Failed to save file.");
        alert.show();
    }

    private void addNewUser() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MyTiApplication.class.getClassLoader().getResource("user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 585, 270);
        Stage stage = new Stage();
        stage.setTitle("Add new user");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        UserController controller = fxmlLoader.getController();
        controller.setUserViewModel(this.userViewModel);

        stage.show();
        stage.setOnCloseRequest((event) -> {
            stage.close();
        });

    }

    private void showReport() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyTiApplication.class.getClassLoader().getResource("report.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 300);
        Stage stage = new Stage();
        stage.setTitle("Report");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        ReportController controller = fxmlLoader.getController();
        controller.setUserViewModel(this.userViewModel);
        controller.setUserTravelViewModel(this.userTravelViewModels);

        stage.show();
        stage.setOnCloseRequest((event) -> {
            stage.close();
        });
    }

    private void editUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyTiApplication.class.getClassLoader().getResource("edituser.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 250);
        Stage stage = new Stage();
        stage.setTitle("Edit User");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        main.java.gui.EditUserController controller = fxmlLoader.getController();
        controller.setUserViewModel(this.userViewModel);

        stage.show();
        stage.setOnCloseRequest((event) -> {
            stage.close();
        });
    }
    @Override
    public void didDataSourceChanged() {
        this.showUsers(this.userViewModel.getOrderedPassengers());
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
                    String text = String.format("%s %s", item.getFullName(), item.getUserType());
                    setText(text);
                }
            }
        });
    }

    private void showDepartures(List<Station> stations) {
        this.showStationList(this.lstDeparture, stations);
    }

    private void showArrivals(List<Station> stations) {
        this.showStationList(this.lstArrival, stations);
    }

    private void showStationList(ListView lstView, List<Station> stations){
        ObservableList<Station> observableList = FXCollections.observableArrayList();
        stations.forEach((item) -> {
            observableList.add(item);
        });

        lstView.setItems(observableList);
        lstView.setCellFactory(param -> new ListCell<Station>() {
            @Override
            protected void updateItem(Station item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

    }

    private Tuple<Boolean, String> validateInput() {

        // if a user was selected
        Passenger passenger = (Passenger) this.lstUser.getSelectionModel().getSelectedItem();
        if(passenger == null){
            return new Tuple(false, "Please select a user.");
        } else {
            this.selectUserId = passenger.getUserId();
        }

        this.selectedDeparture = (Station) this.lstDeparture.getSelectionModel().getSelectedItem();
        this.selectedArrival = (Station) this.lstArrival.getSelectionModel().getSelectedItem();

        if(this.selectedDeparture == null) {
            return new Tuple(false, "Please select departure station.");
        }

        if(this.selectedArrival == null ) {
            return new Tuple(false, "Please select arrival station.");
        }

        if(this.selectedDeparture.getStationId() == this.selectedArrival.getStationId()) {
            return new Tuple(false, "Departure and arrival cannot be the same");
        }

        LocalDate date = this.dpDay.getValue();
        if(date == null) {
            return new Tuple(false, "Please select a departure date.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStr = date.format(formatter);
        String startTimeStr = this.tfStartTime.getText();
        String endTimeStr = this.tfEndTime.getText();

        if(startTimeStr == null || startTimeStr.trim().length() == 0){
            return new Tuple(false, "Please input a start time.");
        }

        if(endTimeStr == null || endTimeStr.trim().length() == 0){
            return new Tuple(false, "Please input a end time.");
        }

        this.startDatetime = Common.getLocalDateTimeFromString(dateStr + " " + startTimeStr);

        if(this.startDatetime == null) {
            return new Tuple(false, "Please input a valid start time.");
        }

        this.endDatetime = Common.getLocalDateTimeFromString(dateStr + " " + endTimeStr);
        if(this.endDatetime == null ){
            return new Tuple(false, "Please input a valid end time.");
        }

        return new Tuple(true, Common.EMPTY_STR);
    }

    private ITravelViewModel getTravelVMByUserId(String userId) {
        if(this.userTravelViewModels.containsKey(userId)){
            return this.userTravelViewModels.get(userId);
        }

        Passenger passenger = this.userViewModel.getPassengerById(userId);
        TravelViewModel vm = new TravelViewModel(passenger, this.priceMatrix);

        this.userTravelViewModels.put(userId, vm);

        return vm;
    }

    private void attachMenuBar() {
        Menu menuFile = new Menu("File");
        Menu menuUser = new Menu("User");

        MenuItem menuNewUser = new MenuItem("New User");
        menuNewUser.setOnAction(actionEvent -> {
            try{
                this.addNewUser();
            } catch (Exception ex){
                System.out.println(ex.toString());
            }
        });

        MenuItem menuEditUser = new MenuItem("Edit User");
        menuEditUser.setOnAction(actionEvent -> {
            try {
                this.editUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        menuUser.getItems().addAll(menuNewUser, menuEditUser);

        MenuItem menuReport = new MenuItem("Report");
        menuReport.setOnAction(actionEvent ->{
            try {
                this.showReport();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem menuSave = new MenuItem("Save");
        menuSave.setOnAction(actionEvent -> {
            this.save(actionEvent);
        });

        MenuItem menuQuit = new MenuItem("Quit");
        menuQuit.setOnAction(actionEvent -> {
            this.quit(actionEvent);
        });

        menuFile.getItems().addAll(menuReport, menuSave, menuQuit);

        this.menuBar = new MenuBar(menuFile, menuUser);
        this.updateMenuBarWidth();
        this.rootAnchorPane.getChildren().add(menuBar);
    }

    private void updateMenuBarWidth() {
        if(this.stage != null) {
            this.menuBar.prefWidthProperty().bind(stage.widthProperty());
        }
        else {
            this.menuBar.setMinWidth(800);
        }
    }
}