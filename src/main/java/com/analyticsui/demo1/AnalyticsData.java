package com.analyticsui.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class AnalyticsData {
    private final String metricName;
    private final String currentValue;
    private final String metricType;
    private final String timeframe;
    private XYChart.Series<String, Number> chartSeries;
    private ObservableList<DetailViewController.MetricDataPoint> tableData;

    public AnalyticsData(String metricName, String currentValue, String metricType) {
        this.metricName = metricName;
        this.currentValue = currentValue;
        this.metricType = metricType;

        // Determine timeframe from metric name
        if (metricName.toLowerCase().contains("monthly") || metricName.toLowerCase().contains("month")) {
            this.timeframe = "monthly";
        } else if (metricName.toLowerCase().contains("year")) {
            this.timeframe = "yearly";
        } else {
            this.timeframe = "daily";
        }
        generateSampleData();
    }

    private void generateSampleData() {
        this.chartSeries = new XYChart.Series<>();
        this.chartSeries.setName(metricName + " Over Time");

        if (timeframe.equals("yearly")) {
            // Yearly data points
            chartSeries.getData().add(new XYChart.Data<>("Q1", 125));
            chartSeries.getData().add(new XYChart.Data<>("Q2", 165));
            chartSeries.getData().add(new XYChart.Data<>("Q3", 205));
            chartSeries.getData().add(new XYChart.Data<>("Q4", 245));

            tableData = FXCollections.observableArrayList(
                    new DetailViewController.MetricDataPoint("Q1", "125", "+25"),
                    new DetailViewController.MetricDataPoint("Q2", "165", "+40"),
                    new DetailViewController.MetricDataPoint("Q3", "205", "+40"),
                    new DetailViewController.MetricDataPoint("Q4", "245", "+40")
            );
        } else if (timeframe.equals("monthly")) {
            // Monthly data points
            chartSeries.getData().add(new XYChart.Data<>("Jan", 2));
            chartSeries.getData().add(new XYChart.Data<>("Feb", 4));
            chartSeries.getData().add(new XYChart.Data<>("Mar", 3));
            chartSeries.getData().add(new XYChart.Data<>("Apr", 5));
            chartSeries.getData().add(new XYChart.Data<>("May", 6));
            chartSeries.getData().add(new XYChart.Data<>("Jun", 8));

            tableData = FXCollections.observableArrayList(
                    new DetailViewController.MetricDataPoint("January", "2", "+2"),
                    new DetailViewController.MetricDataPoint("February", "4", "+2"),
                    new DetailViewController.MetricDataPoint("March", "3", "-1"),
                    new DetailViewController.MetricDataPoint("April", "5", "+2"),
                    new DetailViewController.MetricDataPoint("May", "6", "+1"),
                    new DetailViewController.MetricDataPoint("June", "8", "+2")
            );
        } else {
            // Daily data points
            chartSeries.getData().add(new XYChart.Data<>("9 AM", 2));
            chartSeries.getData().add(new XYChart.Data<>("10 AM", 4));
            chartSeries.getData().add(new XYChart.Data<>("11 AM", 3));
            chartSeries.getData().add(new XYChart.Data<>("12 PM", 5));
            chartSeries.getData().add(new XYChart.Data<>("1 PM", 6));
            chartSeries.getData().add(new XYChart.Data<>("2 PM", 8));
            chartSeries.getData().add(new XYChart.Data<>("3 PM", 7));

            tableData = FXCollections.observableArrayList(
                    new DetailViewController.MetricDataPoint("9:00 AM", "2", "+2"),
                    new DetailViewController.MetricDataPoint("10:00 AM", "4", "+2"),
                    new DetailViewController.MetricDataPoint("11:00 AM", "3", "-1"),
                    new DetailViewController.MetricDataPoint("12:00 PM", "5", "+2"),
                    new DetailViewController.MetricDataPoint("1:00 PM", "6", "+1"),
                    new DetailViewController.MetricDataPoint("2:00 PM", "8", "+2"),
                    new DetailViewController.MetricDataPoint("3:00 PM", "7", "-1")
            );
        }
    }

    public String getMetricName() {
        return metricName;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getMetricType() {
        return metricType;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public XYChart.Series<String, Number> getChartSeries() {
        return chartSeries;
    }

    public ObservableList<DetailViewController.MetricDataPoint> getTableData() {
        return tableData;
    }
}
