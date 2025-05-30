package com.analytics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import com.analytics.DetailViewController.MetricDataPoint;

public class AnalyticsData {
    private String metricName;
    private String currentValue;
    private String metricType;
    private XYChart.Series<String, Number> chartSeries;
    private ObservableList<MetricDataPoint> tableData;

    public AnalyticsData(String metricName, String currentValue, String metricType) {
        this.metricName = metricName;
        this.currentValue = currentValue;
        this.metricType = metricType;
        
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Create sample chart data
        chartSeries = new XYChart.Series<>();
        chartSeries.setName(metricName);
        
        // Add some sample data points based on the metric type
        switch(metricType.toLowerCase()) {
            case "tables":
            case "count":
                for (int i = 1; i <= 12; i++) {
                    chartSeries.getData().add(new XYChart.Data<>("Hour " + i, Math.random() * 10));
                }
                break;
            case "currency":
                for (int i = 1; i <= 12; i++) {
                    chartSeries.getData().add(new XYChart.Data<>("Hour " + i, Math.random() * 100));
                }
                break;
            case "percentage":
                for (int i = 1; i <= 12; i++) {
                    chartSeries.getData().add(new XYChart.Data<>("Hour " + i, Math.random() * 100));
                }
                break;
            default:
                for (int i = 1; i <= 12; i++) {
                    chartSeries.getData().add(new XYChart.Data<>("Hour " + i, Math.random() * 50));
                }
        }

        // Create sample table data
        tableData = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            String timeLabel = "Hour " + i;
            String valueLabel;
            String changeLabel;
            
            double value = Math.random() * 100;
            double change = Math.random() * 10 - 5; // -5 to +5
            
            switch(metricType.toLowerCase()) {
                case "currency":
                    valueLabel = String.format("$%.2f", value);
                    changeLabel = String.format("%s%.2f%%", (change >= 0 ? "+" : ""), change);
                    break;
                case "percentage":
                    valueLabel = String.format("%.1f%%", value);
                    changeLabel = String.format("%s%.1f%%", (change >= 0 ? "+" : ""), change);
                    break;
                case "tables":
                    int tables = (int)(value % 20);
                    valueLabel = tables + "/20";
                    changeLabel = String.format("%s%d", (change >= 0 ? "+" : ""), (int)change);
                    break;
                default:
                    valueLabel = String.format("%.1f", value);
                    changeLabel = String.format("%s%.1f", (change >= 0 ? "+" : ""), change);
            }
            
            tableData.add(new MetricDataPoint(timeLabel, valueLabel, changeLabel));
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
