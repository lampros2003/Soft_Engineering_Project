package com.analyticsui.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {

    
    // Today's Analytics
    @FXML private VBox occupiedTablesCard;
    @FXML private VBox earningsCard;
    @FXML private VBox ordersPlacedCard;
    @FXML private VBox otherStatisticCard;

    // Month's Analytics
    @FXML private VBox monthlyOccupationCard;
    @FXML private VBox monthlyEarningsCard;
    @FXML private VBox monthlyOrdersCard;
    @FXML private VBox monthlyStatsCard;
    @FXML private Label monthlyOccupationLabel;
    @FXML private Label monthlyEarningsLabel;
    @FXML private Label monthlyOrdersLabel;
    @FXML private Label monthlyStatsLabel;
    
    // Last Month's Analytics
    @FXML private VBox lastMonthOccupationCard;
    @FXML private VBox lastMonthEarningsCard;
    @FXML private VBox lastMonthOrdersCard;
    @FXML private VBox lastMonthStatsCard;
    @FXML private Label lastMonthOccupationLabel;
    @FXML private Label lastMonthEarningsLabel;
    @FXML private Label lastMonthOrdersLabel;
    @FXML private Label lastMonthStatsLabel;



    private void navigateToDetailedView(String metricName, String currentValue, String metricType, Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailed-view.fxml"));
            Parent detailedViewRoot = loader.load();
            
            DetailViewController controller = loader.getController();
            AnalyticsData data = new AnalyticsData(metricName, currentValue, metricType);
            controller.setAnalyticsData(data);
            
            // Create a new stage for the detailed view
            Stage detailStage = new Stage();
            detailStage.setTitle("Detailed Analytics - " + metricName);
            detailStage.setScene(new Scene(detailedViewRoot));

            // Set the owner of the new window to the main application window
            if (sourceNode != null && sourceNode.getScene() != null && sourceNode.getScene().getWindow() != null) {
                detailStage.initOwner(sourceNode.getScene().getWindow());
            }

            // Show the detailed view as a popup
            detailStage.show(); 
        } catch (IOException e) {
            Logger logger = Logger.getLogger(HelloController.class.getName());
            logger.log(Level.SEVERE, "Failed to load detailed view", e);
    }

    // Today's Analytics handlers (existing)
    @FXML
    protected void onOccupiedTablesCardClicked() {
        navigateToDetailedView("Occupied Tables", "0/?", "tables", occupiedTablesCard);
    }

    @FXML
    protected void onEarningsCardClicked() {
        navigateToDetailedView("Earnings", "$0.00", "currency", earningsCard);
    }

    @FXML
    protected void onOrdersPlacedCardClicked() {
        navigateToDetailedView("Orders Placed", "0", "count", ordersPlacedCard);
    }

    @FXML
    protected void onOtherStatisticCardClicked() {
        navigateToDetailedView("Other Statistic", "N/A", "general", otherStatisticCard);
    }

    // Month's Analytics handlers
    @FXML
    protected void onMonthlyOccupationCardClicked() {
        navigateToDetailedView("Monthly Occupation", monthlyOccupationLabel.getText(), "percentage", monthlyOccupationCard);
    }

    @FXML
    protected void onMonthlyEarningsCardClicked() {
        navigateToDetailedView("Monthly Earnings", monthlyEarningsLabel.getText(), "currency", monthlyEarningsCard);
    }

    @FXML
    protected void onMonthlyOrdersCardClicked() {
        navigateToDetailedView("Monthly Orders", monthlyOrdersLabel.getText(), "count", monthlyOrdersCard);
    }

    @FXML
    protected void onMonthlyStatsCardClicked() {
        navigateToDetailedView("Monthly Statistics", monthlyStatsLabel.getText(), "general", monthlyStatsCard);
    }

    // Last Month's Analytics handlers
    @FXML
    protected void onLastMonthOccupationCardClicked() {
        navigateToDetailedView("Last Month's Occupation", lastMonthOccupationLabel.getText(), "percentage", lastMonthOccupationCard);
    }

    @FXML
    protected void onLastMonthEarningsCardClicked() {
        navigateToDetailedView("Last Month's Earnings", lastMonthEarningsLabel.getText(), "currency", lastMonthEarningsCard);
    }

    @FXML
    protected void onLastMonthOrdersCardClicked() {
        navigateToDetailedView("Last Month's Orders", lastMonthOrdersLabel.getText(), "count", lastMonthOrdersCard);
    }

    @FXML
    protected void onLastMonthStatsCardClicked() {
        navigateToDetailedView("Last Month's Statistics", lastMonthStatsLabel.getText(), "general", lastMonthStatsCard);
    }
}
