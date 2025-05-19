package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField gmailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private ImageView toggleEyeIcon;
    @FXML private Button CAButton;

    private boolean isPasswordVisible = false;

    private Image eyeOpenImage;
    private Image eyeClosedImage;

    @FXML
    public void initialize() {
        eyeOpenImage = new Image(getClass().getResource("/com/example/restaurant/pictures/open-eye.png").toExternalForm());
        eyeClosedImage = new Image(getClass().getResource("/com/example/restaurant/pictures/close-eye.png").toExternalForm());

        toggleEyeIcon.setImage(eyeClosedImage);

        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    public void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            toggleEyeIcon.setImage(eyeOpenImage);
        } else {
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            toggleEyeIcon.setImage(eyeClosedImage);
        }
    }

    @FXML
    public void handleCreateAccount() {
        String name = nameField.getText();
        String gmail = gmailField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

        if (name.isEmpty() || gmail.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled in!");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            User newUser = new User(name, gmail, password);
            UserData.addUser(newUser);
            showAlert("Success", "Account created successfully!");
            clearFields();

            loadLoginScreen();
        }
    }

    @FXML
    public void handleLogin2() {
        loadLoginScreen();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nameField.clear();
        gmailField.clear();
        passwordField.clear();
        visiblePasswordField.clear();
    }

    private void loadLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);

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
