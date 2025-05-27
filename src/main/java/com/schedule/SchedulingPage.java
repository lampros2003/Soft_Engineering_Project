package com.schedule;

import com.mainpackage.PopUp;
import com.mainpackage.PopUp;
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

