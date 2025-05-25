package com.schedule;

import com.mainpackage.SceneSwitching;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class SchedulingPage {

    @FXML
    void clickedAutoCompleteSchedule(MouseEvent event) {
        System.out.println("ClickedAutoCompleteSchedule");
    }

    @FXML
    void clickedFinalize(MouseEvent event) {
        System.out.println("ClickedFinalize");
        PopUp.Popup("/schedule/empty_shifts_pop_up.fxml");
    }

    @FXML
    void clickedLastWeekSchedule(MouseEvent event) {
        System.out.println("ClickedLastWeekSchedule");
    }

    @FXML
    void display_employees_pop_up(ActionEvent event) {
        System.out.println("display_employees_pop_up");
        PopUp.Popup("/schedule/employee_pop_up.fxml");

    }

}

