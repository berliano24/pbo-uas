package berliano.uas.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class datatiketDAO {
    private Connection connection;

    public datatiketDAO() {
        connection = database.getConnection();
    }

    public void addTravel(datatiket travel) {
        String sql = "INSERT INTO travels (origin, destination, schedule, price, jumlahTiket) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, travel.getOrigin());
            pstmt.setString(2, travel.getDestination());
            pstmt.setString(3, travel.getSchedule());
            pstmt.setDouble(4, travel.getPrice());
            pstmt.setInt(5, travel.getJumlahTiket());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTravel(datatiket travel) {
        String sql = "UPDATE travels SET origin = ?, destination = ?, schedule = ?, price = ?, jumlahTiket = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, travel.getOrigin());
            pstmt.setString(2, travel.getDestination());
            pstmt.setString(3, travel.getSchedule());
            pstmt.setDouble(4, travel.getPrice());
            pstmt.setInt(5, travel.getJumlahTiket());
            pstmt.setInt(6, travel.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTravel(int id) {
        String sql = "DELETE FROM travels WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public datatiket getTravelById(int id) {
        datatiket travel = null;
        String sql = "SELECT * FROM travels WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                travel = new datatiket(
                        rs.getInt("id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("schedule"),
                        rs.getDouble("price"),
                        rs.getInt("jumlahTiket"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return travel;
    }

    public List<datatiket> getAlldatatiket() {
        List<datatiket> travels = new ArrayList<>();
        String sql = "SELECT * FROM travels";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                datatiket travel = new datatiket(
                        rs.getInt("id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("schedule"),
                        rs.getDouble("price"),
                        rs.getInt("jumlahTiket"));
                travels.add(travel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return travels;
    }

    public List<datatiket> searchTravels(String origin, String destination) {
        List<datatiket> travels = new ArrayList<>();
        String sql = "SELECT * FROM travels WHERE origin LIKE ? AND destination LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + origin + "%");
            pstmt.setString(2, "%" + destination + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                datatiket travel = new datatiket(
                        rs.getInt("id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("schedule"),
                        rs.getDouble("price"),
                        rs.getInt("jumlahTiket"));
                travels.add(travel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return travels;
    }

    public void updateJumlahTiket(int travelId, int jumlahTiket) {
        String sql = "UPDATE travels SET jumlahTiket = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, jumlahTiket);
            pstmt.setInt(2, travelId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<datatiket> getTravelsWithLowTickets(int threshold) {
        List<datatiket> travels = new ArrayList<>();
        String sql = "SELECT * FROM travels WHERE jumlahTiket < ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                datatiket travel = new datatiket(
                        rs.getInt("id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("schedule"),
                        rs.getDouble("price"),
                        rs.getInt("jumlahTiket"));
                travels.add(travel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return travels;
    }
}