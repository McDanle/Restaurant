package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML private TextField gmailField;

    @FXML private PasswordField newPasswordField;
    @FXML private TextField newPasswordVisibleField;
    @FXML private Button newPasswordToggle;
    @FXML private ImageView newPasswordEye;

    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField confirmPasswordVisibleField;
    @FXML private Button confirmPasswordToggle;
    @FXML private ImageView confirmPasswordEye;

    private Image eyeOpenImage;
    private Image eyeClosedImage;

    @FXML
    public void initialize() {
        eyeOpenImage = new Image(getClass().getResource("/com/example/restaurant/pictures/open-eye.png").toExternalForm());
        eyeClosedImage = new Image(getClass().getResource("/com/example/restaurant/pictures/close-eye.png").toExternalForm());

        setupPasswordToggle(newPasswordField, newPasswordVisibleField, newPasswordToggle, newPasswordEye);
        setupPasswordToggle(confirmPasswordField, confirmPasswordVisibleField, confirmPasswordToggle, confirmPasswordEye);
    }

    private void setupPasswordToggle(PasswordField passwordField, TextField visibleField, Button toggleButton, ImageView eyeIcon) {
        visibleField.setText(passwordField.getText());

        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!visibleField.isVisible()) {
                visibleField.setText(newVal);
            }
        });

        visibleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (visibleField.isVisible()) {
                passwordField.setText(newVal);
            }
        });

        eyeIcon.setImage(eyeClosedImage);

        toggleButton.setOnAction(e -> {
            boolean isPasswordVisible = visibleField.isVisible();

            visibleField.setVisible(!isPasswordVisible);
            visibleField.setManaged(!isPasswordVisible);

            passwordField.setVisible(isPasswordVisible);
            passwordField.setManaged(isPasswordVisible);

            eyeIcon.setImage(isPasswordVisible ? eyeClosedImage : eyeOpenImage);
        });
    }

    @FXML
    private void handleResetPassword() {
        String gmail = gmailField.getText().trim();
        String newPassword = newPasswordField.isVisible() ? newPasswordField.getText().trim() : newPasswordVisibleField.getText().trim();
        String confirmPassword = confirmPasswordField.isVisible() ? confirmPasswordField.getText().trim() : confirmPasswordVisibleField.getText().trim();

        if (gmail.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }

        if (!UserData.userExists(gmail)) {
            showAlert(Alert.AlertType.ERROR, "Gmail does not exist!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match!");
            return;
        }

        User user = UserData.getUserByGmail(gmail);
        if (user.getPassword().equals(newPassword)) {
            showAlert(Alert.AlertType.ERROR, "New password must be different from the old password.");
            return;
        }

        boolean updated = UserData.updatePassword(gmail, newPassword);
        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Password updated successfully!");
            handleBackToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Something went wrong while updating password.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane loginRoot = loader.load();
            Stage stage = (Stage) gmailField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load login screen.");
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
