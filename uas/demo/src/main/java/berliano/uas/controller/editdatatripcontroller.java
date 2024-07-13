package berliano.uas.controller;

import berliano.uas.model.datatiket;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editdatatripcontroller {
    @FXML
    private TextField originField;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField scheduleField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField jumlahTiketField; // New field

    private Stage dialogStage;
    private datatiket datatiket;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTravel(datatiket datatiket) {
        this.datatiket = datatiket;
        originField.setText(datatiket.getOrigin());
        destinationField.setText(datatiket.getDestination());
        scheduleField.setText(datatiket.getSchedule());
        priceField.setText(Double.toString(datatiket.getPrice()));
        jumlahTiketField.setText(Integer.toString(datatiket.getJumlahTiket())); // Set jumlahTiket
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        datatiket.setOrigin(originField.getText());
        datatiket.setDestination(destinationField.getText());
        datatiket.setSchedule(scheduleField.getText());
        datatiket.setPrice(Double.parseDouble(priceField.getText()));
        datatiket.setJumlahTiket(Integer.parseInt(jumlahTiketField.getText())); // Update jumlahTiket

        saveClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
