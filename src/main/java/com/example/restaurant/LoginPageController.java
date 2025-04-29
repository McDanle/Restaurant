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

    @FXML
    private void handleDashBoard(ActionEvent event) {
        String gmail = gmailField.getText();
        String password = passwordField.getText();

        if (UserData.validateLogin(gmail, password)) {

            User user = UserData.getUserByGmail(gmail);

            SessionManager.setLoggedInUser(user);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
                BorderPane dashboardPage = loader.load();

                // ✅ Pass the logged-in user's name to the controller
                DashBoardController controller = loader.getController();
                controller.setUsername(user.getName()); // or user.getGmail() if you prefer

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
                showError("Error loading Dashboard.fxml!");
            }
        } else {
            showError("Invalid Gmail or Password!");
        }
    }

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
            showError("Error loading signup-view.fxml!");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
