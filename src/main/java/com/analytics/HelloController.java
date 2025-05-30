package com.analytics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.mainpackage.SceneSwitching;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController
{
    // Database manager
    private AnalyticsDBManager dbManager;

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

    @FXML
    public void initialize() {
        // Initialize database manager
        dbManager = new AnalyticsDBManager();
        // Initialize the database table if it doesn't exist

        // Load data from database
        loadDataFromDatabase();
    }

    /**
     * Load data from database and update the UI
     */
    private void loadDataFromDatabase() {
        // Find and update labels for Today's Analytics
        updateTodayAnalyticsLabels();

        // Update Month's Analytics (labels already have fx:id)
        updateLabelFromDB(monthlyOccupationLabel, "monthly_occupation");
        updateLabelFromDB(monthlyEarningsLabel, "monthly_earnings", true, false); // Add currency formatting
        updateLabelFromDB(monthlyOrdersLabel, "monthly_orders");
        updateLabelFromDB(monthlyStatsLabel, "monthly_stats", false, true); // Add percentage formatting

        // Update Last Month's Analytics (labels already have fx:id)
        updateLastMonthAnalyticsLabels();
    }

    /**
     * Update the labels for Last Month's Analytics section
     */
    private void updateLastMonthAnalyticsLabels() {
        updateLabelFromDB(lastMonthOccupationLabel, "last_month_tables");
        updateLabelFromDB(lastMonthEarningsLabel, "last_month_earnings", true, false); // Add currency formatting
        updateLabelFromDB(lastMonthOrdersLabel, "last_month_orders");
        updateLabelFromDB(lastMonthStatsLabel, "last_month_customers"); // This was monthly_stats, changed to last_month_customers
    }

    /**
     * Update the labels for Today's Analytics section (which don't have fx:id)
     */
    private void updateTodayAnalyticsLabels() {
        // Find labels in cards
        Label occupiedTablesLabel = findLabelInCard(occupiedTablesCard);
        Label earningsLabel = findLabelInCard(earningsCard);
        Label ordersPlacedLabel = findLabelInCard(ordersPlacedCard);
        Label otherStatisticLabel = findLabelInCard(otherStatisticCard);

        // Update the labels with data from database
        updateLabelFromDB(occupiedTablesLabel, "occupied_tables");
        updateLabelFromDB(earningsLabel, "total_earnings", true, false); // Add currency formatting
        updateLabelFromDB(ordersPlacedLabel, "total_orders");
        updateLabelFromDB(otherStatisticLabel, "customer_satisfaction", false, true); // Add percentage formatting
    }

    /**
     * Find the value label in a card (the third child, which shows the value)
     */
    private Label findLabelInCard(VBox card) {
        if (card != null && card.getChildren().size() >= 3 && card.getChildren().get(2) instanceof Label) {
            return (Label) card.getChildren().get(2);
        }
        return null;
    }

    /**
     * Update a label with data from the database
     */
    private void updateLabelFromDB(Label label, String key) {
        updateLabelFromDB(label, key, false, false); // Default no special formatting
    }

    /**
     * Update a label with data from the database, with optional currency/percentage formatting
     */
    private void updateLabelFromDB(Label label, String key, boolean isCurrency, boolean isPercentage) {
        if (label != null) {
            String value = dbManager.getGeneralData(key);
            if (value != null) {
                if (isCurrency && !value.startsWith("$")) {
                    label.setText("$" + value);
                } else if (isPercentage && !value.endsWith("%")) {
                    label.setText(value + "%");
                } else {
                    label.setText(value);
                }
            } else {
                // Set a default placeholder if data is not found
                if (isCurrency) {
                    label.setText("$0.00");
                } else if (isPercentage) {
                    label.setText("0%");
                } else {
                    label.setText("N/A");
                }
            }
        }
    }

    @FXML
    void returnToDashboard(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/adminView/dummyDashboard.fxml");
    }

    private void navigateToDetailedView(String metricName, String currentValue, String metricType, Node sourceNode) {
        try {
            String dbKey = null;
            boolean isCurrency = metricType.equals("currency");
            boolean isPercentage = metricType.equals("percentage");

            // Determine the database key based on the metricName
            dbKey = switch (metricName) {
                case "Occupied Tables" -> "occupied_tables";
                case "Earnings" -> "total_earnings";
                case "Orders Placed" -> "total_orders";
                case "Customer Satisfaction" -> // Changed from "Other Statistic"
                        "customer_satisfaction";
                case "Monthly Occupation" -> "monthly_occupation";
                case "Monthly Earnings" -> "monthly_earnings";
                case "Monthly Orders" -> "monthly_orders";
                case "Monthly Customer Satisfaction" -> // Changed from "Monthly Statistics"
                        "monthly_stats";
                case "Last Month's Occupation" -> "last_month_tables";
                case "Last Month's Earnings" -> "last_month_earnings";
                case "Last Month's Orders" -> "last_month_orders";
                case "Last Month's Customer Satisfaction" -> // Changed from "Last Month's Statistics"
                        "last_month_customers";
                default -> dbKey;
            };

            if (dbKey != null) {
                currentValue = dbManager.getGeneralData(dbKey);
                if (currentValue == null) { // Default if key not found
                    currentValue = isCurrency ? "$0.00" : (isPercentage ? "0%" : "N/A");
                } else {
                    if (isCurrency && !currentValue.startsWith("$")) {
                        currentValue = "$" + currentValue;
                    } else if (isPercentage && !currentValue.endsWith("%")) {
                        currentValue = currentValue + "%";
                    }
                }
            } else {
                // Fallback to the label's text if no direct dbKey mapping (should ideally not happen for these specific cards)
                Label sourceLabel = getLabelFromCardOrSelf(sourceNode);
                if (sourceLabel != null) currentValue = sourceLabel.getText();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/analytics/detailed-view.fxml"));
            Parent detailedViewRoot = loader.load();

            DetailViewController controller = loader.getController();
            AnalyticsData data = new AnalyticsData(dbKey, metricName, currentValue, metricType);
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
    }

    private Label getLabelFromCardOrSelf(Node sourceNode) {
        if (sourceNode instanceof Label) {
            return (Label) sourceNode;
        } else if (sourceNode instanceof VBox) {
            return findLabelInCard((VBox) sourceNode);
        }
        return null;
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
        navigateToDetailedView("Customer Satisfaction", "N/A", "percentage", otherStatisticCard);
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
        navigateToDetailedView("Monthly Customer Satisfaction", monthlyStatsLabel.getText(), "percentage", monthlyStatsCard);
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
        navigateToDetailedView("Last Month's Customer Satisfaction", lastMonthStatsLabel.getText(), "percentage", lastMonthStatsCard);
    }
}
