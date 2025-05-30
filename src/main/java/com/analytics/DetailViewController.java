package com.analytics;

import javafx.fxml.FXML;

import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DetailViewController {

    @FXML
    private Button backButton;

    @FXML
    private Label titleLabel;
    
    @FXML
    private Text metricNameText;
    
    @FXML
    private Text metricValueText;
    
    @FXML
    private LineChart<String, Number> dataChart;
    

    
    @FXML
    private TableView<MetricDataPoint> dataTable;
    
    @FXML
    private TableColumn<MetricDataPoint, String> timeColumn;
    
    @FXML
    private TableColumn<MetricDataPoint, String> valueColumn;
    
    @FXML
    private TableColumn<MetricDataPoint, String> changeColumn;

    @FXML
    private Button printToCsvButton;

    private AnalyticsData analyticsData;

    @FXML
    private void initialize() {
        // Set up the table columns
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("change"));
    }

    public void setAnalyticsData(AnalyticsData analyticsData) {
        this.analyticsData = analyticsData;
        
        // Update UI elements with the data
        metricNameText.setText(analyticsData.getMetricName());
        metricValueText.setText("Current Value: " + analyticsData.getCurrentValue());
        
        // Update the title label based on the metric name
        if (analyticsData.getMetricName().toLowerCase().contains("last year")) {
            titleLabel.setText("Last Year's Analytics - " + analyticsData.getMetricName());
        } else {
            titleLabel.setText("Detailed Analytics - " + analyticsData.getMetricName());
        }
        
        // Clear any previous data
        dataChart.getData().clear();
        
        // Load data from AnalyticsData object
        loadData();
    }
    
    private void loadData() {
        if (analyticsData == null) {
            return;
        }
        dataChart.getData().add(analyticsData.getChartSeries());
        dataTable.setItems(analyticsData.getTableData());
    }

    @FXML
    private void onPrintToCsvClicked() {
        if (analyticsData == null || analyticsData.getTableData() == null || analyticsData.getTableData().isEmpty()) {
            // Optionally show an alert to the user that there's no data
            System.out.println("No data to export.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        // Suggest a filename based on the metric name
        String suggestedFileName = analyticsData.getMetricName().replaceAll("[^a-zA-Z0-9\\s]", "").replace(" ", "_") + "_data.csv";
        fileChooser.setInitialFileName(suggestedFileName);

        File file = fileChooser.showSaveDialog(printToCsvButton.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write header
                writer.println(timeColumn.getText() + "," + valueColumn.getText() + "," + changeColumn.getText());

                // Write data
                for (MetricDataPoint dataPoint : analyticsData.getTableData()) {
                    writer.println(
                            escapeCsv(dataPoint.getTime()) + "," +
                            escapeCsv(dataPoint.getValue()) + "," +
                            escapeCsv(dataPoint.getChange())
                    );
                }
                System.out.println("CSV file saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                // Optionally show an error alert to the user
            }
        }
    }

    private String escapeCsv(String data) {
        if (data == null) {
            return "";
        }
        // If data contains comma, quote, or newline, wrap it in double quotes
        // and escape existing double quotes by doubling them.
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }

    @FXML
    private void onBackButtonClicked() {
        // Get the stage this button is on and close it
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    
    // Data class for table items
    public static class MetricDataPoint {
        private String time;
        private String value;
        private String change;

        public MetricDataPoint(String time, String value, String change) {
            this.time = time;
            this.value = value;
            this.change = change;
        }

        public String getTime() { return time; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getChange() { return change; }
    }
}

