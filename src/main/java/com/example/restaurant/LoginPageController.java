package com.example.restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField gmailField;

    @FXML
    private TextField passwordField;

    // This method handles login and redirects to the dashboard
    @FXML
    private void handleDashBoard(ActionEvent event) {
        String gmail = gmailField.getText();
        String password = passwordField.getText();

        if (UserData.validateLogin(gmail, password)) {
            try {
                BorderPane dashboardPage = FXMLLoader.load(getClass().getResource("DashBoard.fxml"));
                Scene dashboardScene = new Scene(dashboardPage);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double width = window.getWidth();
                double height = window.getHeight();
                boolean isMaximized = window.isMaximized();

                window.setScene(dashboardScene);
                window.setTitle("Dashboard");
                window.setWidth(width);
                window.setHeight(height);
                window.setMaximized(isMaximized);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Gmail or Password!");
            alert.showAndWait();
        }
    }


    // This method handles redirecting to the Sign Up page
    @FXML
    private void handleSignUp2(ActionEvent event) {
        try {
            AnchorPane signupPage = FXMLLoader.load(getClass().getResource("signup-view.fxml"));
            Scene signupScene = new Scene(signupPage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = window.getWidth();
            double height = window.getHeight();
            boolean isMaximized = window.isMaximized();

            window.setScene(signupScene);
            window.setTitle("Sign Up Page");
            window.setWidth(width);
            window.setHeight(height);
            window.setMaximized(isMaximized);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
