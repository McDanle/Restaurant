package com.example.restaurant;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML private TextField gmailField;
    @FXML private TextField passwordField;
    @FXML private ProgressIndicator loadingSpinner;
    @FXML private TextField visiblePasswordField;
    @FXML private ImageView toggleEyeIcon;

    private Image eyeOpenImage;
    private Image eyeClosedImage;
    private boolean isPasswordVisible = false;

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
    private void togglePasswordVisibility(javafx.scene.input.MouseEvent event) {
        if (isPasswordVisible) {
            visiblePasswordField.setManaged(false);
            visiblePasswordField.setVisible(false);
            passwordField.setManaged(true);
            passwordField.setVisible(true);
            toggleEyeIcon.setImage(eyeClosedImage);
            isPasswordVisible = false;
        } else {
            visiblePasswordField.setManaged(true);
            visiblePasswordField.setVisible(true);
            passwordField.setManaged(false);
            passwordField.setVisible(false);
            toggleEyeIcon.setImage(eyeOpenImage);
            isPasswordVisible = true;
        }
    }

    @FXML
    private void handleDashBoard(ActionEvent event) {
        String gmail = gmailField.getText();
        String password = passwordField.getText();

        loadingSpinner.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(2000);

                if (UserData.validateLogin(gmail, password)) {
                    User user = UserData.getUserByGmail(gmail);
                    if (user == null) {
                        Platform.runLater(() -> {
                            loadingSpinner.setVisible(false);
                            showError("User not found!");
                        });
                        return;
                    }

                    SessionManager.setLoggedInUser(user);
                    System.out.println("Login successful for user: " + user.getName());

                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
                            BorderPane dashboardPage = loader.load();
                            DashBoardController controller = loader.getController();
                            controller.setUsername(user.getName());

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
                            showError("Error loading DashBoard.fxml!");
                        } finally {
                            loadingSpinner.setVisible(false);
                        }
                    });

                } else {
                    Platform.runLater(() -> {
                        loadingSpinner.setVisible(false);
                        showError("Invalid Gmail or Password!");
                    });
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    loadingSpinner.setVisible(false);
                    showError("Login process was interrupted!");
                });
            }
        }).start();
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

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            AnchorPane forgotPasswordPage = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));
            Scene forgotPasswordScene = new Scene(forgotPasswordPage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = window.getWidth();
            double height = window.getHeight();
            boolean isMaximized = window.isMaximized();

            window.setScene(forgotPasswordScene);
            window.setTitle("Forgot Password");
            window.setWidth(width);
            window.setHeight(height);
            window.setMaximized(isMaximized);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading ForgotPassword.fxml!");
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
