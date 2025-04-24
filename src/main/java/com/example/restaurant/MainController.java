package com.example.restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class MainController {

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            // Load login.fxml
            AnchorPane loginPage = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage and set new scene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = window.getWidth();
            double height = window.getHeight();
            boolean isMaximized = window.isMaximized();

            window.setScene(loginScene);
            window.setTitle("LogIn");
            window.setWidth(width);
            window.setHeight(height);
            window.setMaximized(isMaximized);
            window.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Load signup.fxml
            AnchorPane signupPage = FXMLLoader.load(getClass().getResource("signup-view.fxml"));
            Scene signupScene = new Scene(signupPage);

            // Get current stage and set new scene
            Stage window2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = window2.getWidth();
            double height = window2.getHeight();
            boolean isMaximized = window2.isMaximized();

            window2.setScene(signupScene);
            window2.setTitle("Sign Up Page");
            window2.setWidth(width);
            window2.setHeight(height);
            window2.setMaximized(isMaximized);
            window2.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
