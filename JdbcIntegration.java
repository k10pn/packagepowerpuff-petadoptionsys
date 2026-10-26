package petadoptionsystem;

import java.sql.*;
import java.util.List;

public class JdbcIntegration {
    private final String url;
    public JdbcIntegration(String url) { this.url = url; }
    public void exportAppointments(List<Appointment> appointments) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS appointments (id TEXT PRIMARY KEY, pet_name TEXT, adopter_name TEXT, appointment_date TEXT, appointment_time TEXT, status TEXT)");
            String sql = "INSERT OR REPLACE INTO appointments VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) { for (Appointment a : appointments) { ps.setString(1,a.getId()); ps.setString(2,a.getPet().getName()); ps.setString(3,a.getProfile().getFullName()); ps.setString(4,a.getDate().toString()); ps.setString(5,a.getTime().toString()); ps.setString(6,a.getStatus().toString()); ps.addBatch(); } ps.executeBatch(); }
        }
    }
}
