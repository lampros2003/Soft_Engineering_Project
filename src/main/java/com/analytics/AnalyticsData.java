package com.analytics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import com.analytics.DetailViewController.MetricDataPoint;

public class AnalyticsData {
    private String dataKey;
    private String metricName;
    private String currentValue;
    private String metricType;
    private XYChart.Series<String, Number> chartSeries;
    private ObservableList<MetricDataPoint> tableData;
    private AnalyticsDBManager dbManager;

    public AnalyticsData(String dataKey, String metricName, String currentValue, String metricType) {
        this.dataKey = dataKey;
        this.metricName = metricName;
        this.currentValue = currentValue;
        this.metricType = metricType;
        this.dbManager = new AnalyticsDBManager();
        // Ensure today's detail table exists
        // Load today's data
        loadDataFromDB();
    }

    /** Load today's data from ANALYTICS_TODAY table; empty if none */
    private void loadDataFromDB() {
        try {
            java.util.List<AnalyticsDBManager.TodayRecord> today = dbManager.getTodayData(dataKey);
            chartSeries = new XYChart.Series<>();
            chartSeries.setName(metricName);
            tableData = FXCollections.observableArrayList();
            if (today != null) {
                for (AnalyticsDBManager.TodayRecord rec : today) {
                    String hour = rec.getHour();
                    double val = rec.getValue();
                    double ch = rec.getChange();
                    chartSeries.getData().add(new XYChart.Data<>(hour, val));
                    String valLabel;
                    String chLabel;
                    if (metricType.equalsIgnoreCase("currency")) {
                        valLabel = String.format("$%.2f", val);
                        chLabel = String.format("%+.2f", ch);
                    } else if (metricType.equalsIgnoreCase("percentage")) {
                        valLabel = String.format("%.1f%%", val);
                        chLabel = String.format("%+.1f%%", ch);
                    } else if (metricType.equalsIgnoreCase("tables") || metricType.equalsIgnoreCase("count")) {
                        valLabel = String.valueOf((int) val);
                        chLabel = String.format("%+d", (int) ch);
                    } else {
                        valLabel = String.format("%.1f", val);
                        chLabel = String.format("%+.1f", ch);
                    }
                    tableData.add(new MetricDataPoint(hour, valLabel, chLabel));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chartSeries = new XYChart.Series<>();
            chartSeries.setName(metricName);
            tableData = FXCollections.observableArrayList();
        }
    }

    // Getters
    public String getMetricName() {
        return metricName;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getMetricType() {
        return metricType;
    }

    public XYChart.Series<String, Number> getChartSeries() {
        return chartSeries;
    }

    public ObservableList<MetricDataPoint> getTableData() {
        return tableData;
    }
}


