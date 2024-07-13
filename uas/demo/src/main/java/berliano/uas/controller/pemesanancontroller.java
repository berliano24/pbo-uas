package berliano.uas.controller;

import berliano.uas.model.pemesanan;
import berliano.uas.model.pemesananDAO;
import berliano.uas.model.datatiket;
import berliano.uas.model.datatiketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class pemesanancontroller {

    @FXML
    private TableView<pemesanan> bookingTable;
    @FXML
    private TableColumn<pemesanan, Integer> idColumn;
    @FXML
    private TableColumn<pemesanan, String> customerNameColumn;
    @FXML
    private TableColumn<pemesanan, String> travelOriginColumn;
    @FXML
    private TableColumn<pemesanan, String> travelDestinationColumn;
    @FXML
    private TableColumn<pemesanan, String> travelScheduleColumn;
    @FXML
    private TableColumn<pemesanan, Integer> ticketCountColumn;
    @FXML
    private TableColumn<pemesanan, Double> totalCostColumn;

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField ticketCountField;
    @FXML
    private ChoiceBox<datatiket> travelChoiceBox;

    private pemesananDAO bookingDAO;
    private datatiketDAO travelDAO;
    private ObservableList<pemesanan> bookingData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bookingDAO = new pemesananDAO();
        travelDAO = new datatiketDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        travelOriginColumn.setCellValueFactory(new PropertyValueFactory<>("travelOrigin"));
        travelDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("travelDestination"));
        travelScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("travelSchedule"));
        ticketCountColumn.setCellValueFactory(new PropertyValueFactory<>("ticketCount"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        loadBookingData();
        loadTravelChoices();
    }

    private void loadBookingData() {
        bookingData.setAll(bookingDAO.getAllpemesanan());
        bookingTable.setItems(bookingData);
    }

    private void loadTravelChoices() {
        List<datatiket> travels = travelDAO.getAlldatatiket();
        travelChoiceBox.getItems().setAll(travels);
        travelChoiceBox.setConverter(new javafx.util.StringConverter<datatiket>() {
            @Override
            public String toString(datatiket travel) {
                if (travel == null) {
                    return "";
                }
                return travel.getOrigin() + " to " + travel.getDestination() + " at " + travel.getSchedule();
            }

            @Override
            public datatiket fromString(String string) {
                return travelChoiceBox.getItems().stream()
                        .filter(ap -> (ap.getOrigin() + " to " + ap.getDestination() + " at " + ap.getSchedule())
                                .equals(string))
                        .findFirst().orElse(null);
            }
        });
    }

    @FXML
    private void addBooking() {
        String customerName = customerNameField.getText();
        datatiket selectedTravel = travelChoiceBox.getValue();
        if (selectedTravel == null) {
            showAlert("No Travel Selected", "Please select a travel route.");
            return;
        }
        int ticketCount;
        try {
            ticketCount = Integer.parseInt(ticketCountField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Ticket Count", "Please enter a valid number for ticket count.");
            return;
        }

        if (ticketCount > selectedTravel.getJumlahTiket()) {
            showAlert("Insufficient Tickets", "Not enough tickets available.");
            return;
        }

        double totalCost = ticketCount * selectedTravel.getPrice();

        pemesanan booking = new pemesanan(0, customerName, selectedTravel.getId(), selectedTravel.getOrigin(),
                selectedTravel.getDestination(), selectedTravel.getSchedule(), ticketCount, totalCost);
        bookingDAO.addpemesanan(booking);
        loadBookingData();
    }

    @FXML
    private void printTicket() {
        pemesanan selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("No Booking Selected", "Please select a booking to print the ticket.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/berliano/uas/cetaktiketview.fxml"));
            Scene scene = new Scene(loader.load());

            cetaktiketcontroller controller = loader.getController();
            controller.setBooking(selectedBooking);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Print Ticket");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}