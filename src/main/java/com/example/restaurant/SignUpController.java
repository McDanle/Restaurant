package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField gmailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button CAButton;

    // Handle the "Create Account" button click
    @FXML
    public void handleCreateAccount() {
        String name = nameField.getText();
        String gmail = gmailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || gmail.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled in!");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            User newUser = new User(name, gmail, password);
            UserData.addUser(newUser);
            showAlert("Success", "Account created successfully!");
            clearFields();

            // After successful account creation, load the login screen
            loadLoginScreen();
        }
    }

    // Handle the "Log-in" button click (for demonstration)
    @FXML
    public void handleLogin2() {
        loadLoginScreen();
    }

    // Show alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear the input fields after a successful account creation
    private void clearFields() {
        nameField.clear();
        gmailField.clear();
        passwordField.clear();
    }

    // Load the login screen after account creation
    private void loadLoginScreen() {
        try {
            // Load the login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);

            // Create a new scene and set it on the stage
            Stage stage = (Stage) CAButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean isMaximized = stage.isMaximized();

            stage.setTitle("Login Page");
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMaximized(isMaximized);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login screen.");
        }
    }
}
