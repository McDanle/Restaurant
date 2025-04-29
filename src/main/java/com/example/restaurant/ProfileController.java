package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label gmailLabel;

    @FXML
    private TextField editNameField;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    private User currentUser;

    public void initialize() {
        currentUser = SessionManager.getLoggedInUser();
        if (currentUser != null) {
            nameLabel.setText("Name: " + currentUser.getName());
            gmailLabel.setText("Gmail: " + currentUser.getGmail());
        }
    }

    @FXML
    private void handleEditProfile() {
        if (currentUser != null) {
            editNameField.setText(currentUser.getName());
            editNameField.setVisible(true);
            saveButton.setVisible(true);
            editButton.setDisable(true);
        }
    }

    @FXML
    private void handleSaveChanges() {
        String newName = editNameField.getText();
        if (!newName.isEmpty()) {
            currentUser.setName(newName);
            nameLabel.setText("Name: " + newName);
            editNameField.setVisible(false);
            saveButton.setVisible(false);
            editButton.setDisable(false);
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.setLoggedInUser(null); // Clear session
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);

            Stage stage = (Stage) nameLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
