package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML private TextField gmailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private TextField currentPasswordVisibleField;
    @FXML private Button currentPasswordToggle;
    @FXML private ImageView currentPasswordEye;
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

        setupPasswordToggle(currentPasswordField, currentPasswordVisibleField, currentPasswordToggle, currentPasswordEye);
        setupPasswordToggle(newPasswordField, newPasswordVisibleField, newPasswordToggle, newPasswordEye);
        setupPasswordToggle(confirmPasswordField, confirmPasswordVisibleField, confirmPasswordToggle, confirmPasswordEye);
    }

    private void setupPasswordToggle(PasswordField passwordField, TextField visibleField, Button toggleButton, ImageView eyeIcon) {
        visibleField.setText(passwordField.getText());

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!visibleField.isVisible()) {
                visibleField.setText(newText);
            }
        });

        visibleField.textProperty().addListener((obs, oldText, newText) -> {
            if (visibleField.isVisible()) {
                passwordField.setText(newText);
            }
        });

        eyeIcon.setImage(eyeClosedImage);

        toggleButton.setOnAction(e -> {
            boolean isVisible = visibleField.isVisible();
            visibleField.setVisible(!isVisible);
            visibleField.setManaged(!isVisible);
            passwordField.setVisible(isVisible);
            passwordField.setManaged(isVisible);
            eyeIcon.setImage(isVisible ? eyeClosedImage : eyeOpenImage);
        });
    }
    @FXML
    private void handleResetPassword() {
        String gmail = gmailField.getText().trim();

        String currentPassword = currentPasswordField.isVisible() ? currentPasswordField.getText().trim() : currentPasswordVisibleField.getText().trim();
        String newPassword = newPasswordField.isVisible() ? newPasswordField.getText().trim() : newPasswordVisibleField.getText().trim();
        String confirmPassword = confirmPasswordField.isVisible() ? confirmPasswordField.getText().trim() : confirmPasswordVisibleField.getText().trim();

        if (gmail.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return;
        }

        if (!UserData.userExists(gmail)) {
            showAlert(Alert.AlertType.ERROR, "Gmail does not exist!");
            return;
        }

        User user = UserData.getUserByGmail(gmail);

        if (!user.getPassword().equals(currentPassword)) {
            showAlert(Alert.AlertType.ERROR, "Current password is incorrect!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match!");
            return;
        }

        if (user.getPassword().equals(newPassword)) {
            showAlert(Alert.AlertType.ERROR, "New password must be different from the old password.");
            return;
        }

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
