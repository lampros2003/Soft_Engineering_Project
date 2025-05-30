package com.analytics;

import com.common.DBManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AnalyticsDBManager {
    private final DBManager dbManager;

    public AnalyticsDBManager() {
        this.dbManager = new DBManager();
    }

    /**
     * Get a specific data value from the general data table
     */
    public String getGeneralData(String key) {
        String sql = "SELECT data_value FROM GENERAL_DATA WHERE data_key = ?";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("data_value");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data for key " + key + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Fetch today's detailed data rows for a given key
     */
    public List<TodayRecord> getTodayData(String key) throws SQLException {
        String query = "SELECT data_hour, data_value, data_change FROM ANALYTICS_TODAY " +
                "WHERE data_key = ? ORDER BY data_hour";
        List<TodayRecord> list = new java.util.ArrayList<>();
        try (Connection conn = dbManager.connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String hour = rs.getString("data_hour");
                double val = rs.getDouble("data_value");
                double change = rs.getDouble("data_change");
                list.add(new TodayRecord(hour, val, change));
            }
        }
        return list;
    }

    /**
     * Container for a single today's detail record
     */
    public static class TodayRecord {
        private final String hour;
        private final double value;
        private final double change;

        public TodayRecord(String hour, double value, double change) {
            this.hour = hour;
            this.value = value;
            this.change = change;
        }

        public String getHour() {
            return hour;
        }

        public double getValue() {
            return value;
        }

        public double getChange() {
            return change;
        }
    }
}
