package berliano.uas.controller;

import berliano.uas.model.pemesanan;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class cetaktiketcontroller {

    @FXML
    private TextArea ticketTextArea;

    private pemesanan pemesanan;

    public void setBooking(pemesanan pemesanan) {
        this.pemesanan = pemesanan;
        displayTicketDetails();
    }

    private void displayTicketDetails() {
        if (pemesanan != null) {
            String ticketDetails = String.format(
                    "Customer Name: %s\nTravel Route: %s to %s\nSchedule: %s\nTicket Count: %d\nTotal Cost: %.2f",
                    pemesanan.getCustomerName(), pemesanan.getTravelOrigin(), pemesanan.getTravelDestination(),
                    pemesanan.getTravelSchedule(), pemesanan.getTicketCount(), pemesanan.getTotalCost());
            ticketTextArea.setText(ticketDetails);
        }
    }
}
