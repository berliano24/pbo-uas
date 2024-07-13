package berliano.uas.controller;

import berliano.uas.model.datatiket;
import berliano.uas.model.datatiketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class pengaturantiketcontroller {

    @FXML
    private TableView<datatiket> datatiketTable;
    @FXML
    private TableColumn<datatiket, Integer> idColumn;
    @FXML
    private TableColumn<datatiket, String> originColumn;
    @FXML
    private TableColumn<datatiket, String> destinationColumn;
    @FXML
    private TableColumn<datatiket, String> scheduleColumn;
    @FXML
    private TableColumn<datatiket, Double> priceColumn;
    @FXML
    private TableColumn<datatiket, Integer> jumlahTiketColumn;

    private datatiketDAO travelDAO;
    private ObservableList<datatiket> travelData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        travelDAO = new datatiketDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        jumlahTiketColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahTiket"));

        loadTravelData();
        checkLowTickets();
    }

    private void loadTravelData() {
        List<datatiket> travels = travelDAO.getAlldatatiket();
        travelData.setAll(travels);
        datatiketTable.setItems(travelData);
    }

    private void checkLowTickets() {
        List<datatiket> lowTicketTravels = travelDAO.getTravelsWithLowTickets(3);
        if (!lowTicketTravels.isEmpty()) {
            StringBuilder message = new StringBuilder("The following routes have less than 3 tickets left:\n");
            for (datatiket travel : lowTicketTravels) {
                message.append(travel.getOrigin()).append(" to ").append(travel.getDestination())
                        .append(" at ").append(travel.getSchedule()).append(" - ")
                        .append(travel.getJumlahTiket()).append(" tickets left\n");
            }
            showAlert("Low Tickets", message.toString());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
