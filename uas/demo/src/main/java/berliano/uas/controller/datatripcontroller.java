package berliano.uas.controller;

import berliano.uas.model.database;
import berliano.uas.model.datatiket;
import berliano.uas.model.datatiketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class datatripcontroller {
    @FXML
    private TextField originField, destinationField, scheduleField, priceField, jumlahTiketField;
    @FXML
    private TextField searchOriginField, searchDestinationField;
    @FXML
    private TableView<datatiket> travelTable;
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

    public void initialize() {
        travelDAO = new datatiketDAO();
        database.createTable();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        jumlahTiketColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahTiket"));
        loaddatatiketData();

        travelTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTravelDetails(newValue));
    }

    private void showTravelDetails(datatiket travel) {
        if (travel != null) {
            originField.setText(travel.getOrigin());
            destinationField.setText(travel.getDestination());
            scheduleField.setText(travel.getSchedule());
            priceField.setText(Double.toString(travel.getPrice()));
            jumlahTiketField.setText(Integer.toString(travel.getJumlahTiket()));
        } else {
            originField.setText("");
            destinationField.setText("");
            scheduleField.setText("");
            priceField.setText("");
            jumlahTiketField.setText("");
        }
    }

    @FXML
    private void adddatatiket() {
        String origin = originField.getText();
        String destination = destinationField.getText();
        String schedule = scheduleField.getText();
        double price = Double.parseDouble(priceField.getText());
        int jumlahTiket = Integer.parseInt(jumlahTiketField.getText());

        datatiket travel = new datatiket(0, origin, destination, schedule, price, jumlahTiket);
        travelDAO.addTravel(travel);
        loaddatatiketData();
    }

    @FXML
    private void editdatatiket() {
        datatiket selectedTravel = travelTable.getSelectionModel().getSelectedItem();
        if (selectedTravel != null) {
            boolean saveClicked = showEditdatatiketDialog(selectedTravel);
            if (saveClicked) {
                travelDAO.updateTravel(selectedTravel); // Update database with new travel data
                updatedatatiketData(selectedTravel);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Travel Selected");
            alert.setContentText("Please select a travel in the table.");
            alert.showAndWait();
        }
    }

    private boolean showEditdatatiketDialog(datatiket travel) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/berliano/uas/editdatatrip.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Travel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            editdatatripcontroller controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTravel(travel);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void deletedatatiket() {
        datatiket selectedTravel = travelTable.getSelectionModel().getSelectedItem();
        if (selectedTravel != null) {
            travelDAO.deleteTravel(selectedTravel.getId());
            loaddatatiketData();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Travel Selected");
            alert.setContentText("Please select a travel in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void searchdatatiket() {
        String origin = searchOriginField.getText();
        String destination = searchDestinationField.getText();
        List<datatiket> travels = travelDAO.searchTravels(origin, destination);
        travelData.setAll(travels);
        travelTable.setItems(travelData);
    }

    private void loaddatatiketData() {
        List<datatiket> travels = travelDAO.getAlldatatiket();
        travelData.setAll(travels);
        travelTable.setItems(travelData);
    }

    private void updatedatatiketData(datatiket travel) {
        travelTable.refresh();
    }
}
