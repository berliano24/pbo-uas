package berliano.uas.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class pemesananDAO {
    private Connection connection;

    public pemesananDAO() {
        connection = database.getConnection();
    }

    public void addpemesanan(pemesanan pemesanan) {
        String sql = "INSERT INTO bookings (customerName, travelId, ticketCount, totalCost) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pemesanan.getCustomerName());
            pstmt.setInt(2, pemesanan.getTravelId());
            pstmt.setInt(3, pemesanan.getTicketCount());
            pstmt.setDouble(4, pemesanan.getTotalCost());
            pstmt.executeUpdate();

            // Reduce the number of available tickets
            datatiketDAO travelDAO = new datatiketDAO();
            datatiket travel = travelDAO.getTravelById(pemesanan.getTravelId());
            int newJumlahTiket = travel.getJumlahTiket() - pemesanan.getTicketCount();
            travelDAO.updateJumlahTiket(pemesanan.getTravelId(), newJumlahTiket);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<pemesanan> getAllpemesanan() {
        List<pemesanan> bookings = new ArrayList<>();
        String sql = "SELECT b.id, b.customerName, b.travelId, t.origin, t.destination, t.schedule, b.ticketCount, b.totalCost "
                + "FROM bookings b JOIN travels t ON b.travelId = t.id";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pemesanan booking = new pemesanan(
                        rs.getInt("id"),
                        rs.getString("customerName"),
                        rs.getInt("travelId"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("schedule"),
                        rs.getInt("ticketCount"),
                        rs.getDouble("totalCost"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
