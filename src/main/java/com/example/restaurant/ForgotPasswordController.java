package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField gmailField;

    @FXML
    private PasswordField currentPasswordField;  // Added this

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleResetPassword() {
        String gmail = gmailField.getText().trim();
        String currentPassword = currentPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Check for empty fields
        if (gmail.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return;
        }

        // Check if user exists
        if (!UserData.userExists(gmail)) {
            showAlert(Alert.AlertType.ERROR, "Gmail does not exist!");
            return;
        }

        User user = UserData.getUserByGmail(gmail);

        // Validate current password
        if (!user.getPassword().equals(currentPassword)) {
            showAlert(Alert.AlertType.ERROR, "Current password is incorrect!");
            return;
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match!");
            return;
        }

        // Check if new password is different from the old password
        if (user.getPassword().equals(newPassword)) {
            showAlert(Alert.AlertType.ERROR, "New password must be different from the old password.");
            return;
        }

        // Update password
        boolean updated = UserData.updatePassword(gmail, newPassword);
        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Password updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Something went wrong while updating password.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane loginPage = fxmlLoader.load();

            Scene loginScene = new Scene(loginPage);

            Stage window = (Stage) gmailField.getScene().getWindow();
            window.setScene(loginScene);
            window.setTitle("Login Page");

            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
