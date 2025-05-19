package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label gmailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField editNameField;

    @FXML
    private TextField editPhoneField;

    @FXML
    private TextField editAddressField;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView profileImageView;

    private User currentUser;

    public void initialize() {
        currentUser = SessionManager.getLoggedInUser();

        if (currentUser != null) {
            nameLabel.setText(currentUser.getName());
            gmailLabel.setText(currentUser.getGmail());
            phoneLabel.setText(currentUser.getPhone());
            addressLabel.setText(currentUser.getAddress());

            // Load saved profile image
            if (currentUser.getImagePath() != null && !currentUser.getImagePath().isEmpty()) {
                try {
                    Image image = new Image(currentUser.getImagePath());
                    profileImageView.setImage(image);
                } catch (Exception e) {
                    System.out.println("Could not load image: " + e.getMessage());
                }
            }

            toggleEditMode(false);
        }
    }

    @FXML
    private void handleEditProfile() {
        if (currentUser != null) {
            editNameField.setText(currentUser.getName());
            editPhoneField.setText(currentUser.getPhone());
            editAddressField.setText(currentUser.getAddress());

            toggleEditMode(true);
        }
    }

    @FXML
    private void handleSaveChanges() {
        String newName = editNameField.getText().trim();
        String newPhone = editPhoneField.getText().trim();
        String newAddress = editAddressField.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled.");
            return;
        }

        if (newName.length() < 3) {
            showAlert("Validation Error", "Name must be at least 3 characters long.");
            return;
        }

        currentUser.setName(newName);
        currentUser.setPhone(newPhone);
        currentUser.setAddress(newAddress);

        nameLabel.setText(newName);
        phoneLabel.setText(newPhone);
        addressLabel.setText(newAddress);

        toggleEditMode(false);
    }

    @FXML
    private void handleLogout() {
        if (!confirmLogout()) return;

        SessionManager.setLoggedInUser(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);

            Stage stage = (Stage) nameLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            showAlert("Load Error", "Could not load login page.");
        }
    }

    private void toggleEditMode(boolean isEditing) {
        nameLabel.setVisible(!isEditing);
        editNameField.setVisible(isEditing);

        phoneLabel.setVisible(!isEditing);
        editPhoneField.setVisible(isEditing);

        addressLabel.setVisible(!isEditing);
        editAddressField.setVisible(isEditing);

        editButton.setVisible(!isEditing);
        saveButton.setVisible(isEditing);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) profileImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                String imagePath = selectedFile.toURI().toString();
                Image image = new Image(imagePath);
                profileImageView.setImage(image);

                currentUser.setImagePath(imagePath); // Save image path to user
            } catch (Exception e) {
                showAlert("Upload Error", "Unable to load selected image.");
            }
        }
    }

    @FXML
    private void handleChangePassword() {

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Please enter your current and new password.");


        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);


        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Current Password");

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New Password");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm New Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPassword, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirm New Password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Enable/Disable change button depending on whether all fields are filled
        Node changeButton = dialog.getDialogPane().lookupButton(changeButtonType);
        changeButton.setDisable(true);

        currentPassword.textProperty().addListener((obs, oldVal, newVal) ->
                changeButton.setDisable(currentPassword.getText().trim().isEmpty()
                        || newPassword.getText().trim().isEmpty()
                        || confirmPassword.getText().trim().isEmpty())
        );
        newPassword.textProperty().addListener((obs, oldVal, newVal) ->
                changeButton.setDisable(currentPassword.getText().trim().isEmpty()
                        || newPassword.getText().trim().isEmpty()
                        || confirmPassword.getText().trim().isEmpty())
        );
        confirmPassword.textProperty().addListener((obs, oldVal, newVal) ->
                changeButton.setDisable(currentPassword.getText().trim().isEmpty()
                        || newPassword.getText().trim().isEmpty()
                        || confirmPassword.getText().trim().isEmpty())
        );

        // Convert the result to a password array when the change button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                return new String[] {
                        currentPassword.getText(),
                        newPassword.getText(),
                        confirmPassword.getText()
                };
            }
            return null;
        });

        dialog.showAndWait().ifPresent(passwords -> {
            String currentPassInput = passwords[0];
            String newPassInput = passwords[1];
            String confirmPassInput = passwords[2];

            // Check current password (assuming User has getPassword() method storing hashed or plain password)
            if (!currentPassInput.equals(currentUser.getPassword())) {
                showAlert("Error", "Current password is incorrect.");
                return;
            }

            if (newPassInput.isEmpty()) {
                showAlert("Error", "New password cannot be empty.");
                return;
            }

            if (!newPassInput.equals(confirmPassInput)) {
                showAlert("Error", "New passwords do not match.");
                return;
            }

            // Update password
            currentUser.setPassword(newPassInput);
            showAlert("Success", "Password changed successfully.");
        });
    }

}
