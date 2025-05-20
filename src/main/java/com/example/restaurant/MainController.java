package com.example.restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    private void handleLoginPage(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Get current dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();

            Scene scene = new Scene(loginRoot, width, height); // Set same size
            stage.setScene(scene);
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUpPage(ActionEvent event) {
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("signup-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Get current dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();

            Scene scene = new Scene(signUpRoot, width, height);
            stage.setScene(scene);
            stage.setTitle("Sign Up Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
