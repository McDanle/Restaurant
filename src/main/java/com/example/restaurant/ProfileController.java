package com.example.restaurant;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML private Label nameLabel, gmailLabel, phoneLabel, addressLabel;
    @FXML private TextField editNameField, editPhoneField, editAddressField;
    @FXML private Button editNameButton, editPhoneButton, editAddressButton, saveButton;
    @FXML private ImageView profileImageView;
    @FXML private Circle clipCircle;
    @FXML private Button deleteAccountButton;

    private boolean editingName = false, editingPhone = false, editingAddress = false;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = SessionManager.getLoggedInUser();
        if (currentUser != null) {
            setupProfileImageClip();
            setupPhoneFieldFormatter();
            loadUserData();
            toggleEditMode(false);
        }
    }

    private void setupPhoneFieldFormatter() {
        editPhoneField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d{0,11}") ? change : null));
    }

    private void setupProfileImageClip() {
        clipCircle.centerXProperty().bind(profileImageView.fitWidthProperty().divide(2));
        clipCircle.centerYProperty().bind(profileImageView.fitHeightProperty().divide(2));
        clipCircle.radiusProperty().bind(Bindings.min(
                profileImageView.fitWidthProperty(), profileImageView.fitHeightProperty()).divide(2));
        profileImageView.setClip(clipCircle);
    }

    private void loadUserData() {
        nameLabel.setText(currentUser.getName());
        gmailLabel.setText(currentUser.getGmail());
        phoneLabel.setText(currentUser.getPhone());
        addressLabel.setText(currentUser.getAddress());

        String imagePath = currentUser.getImagePath();
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            try {
                Image image = imagePath.startsWith("file:") || imagePath.startsWith("http")
                        ? new Image(imagePath)
                        : new Image("file:" + imagePath);
                profileImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Could not load image: " + e.getMessage());
            }
        }
    }

    @FXML private void handleEditName() {
        switchToEditMode(editNameField, nameLabel, editNameButton);
        setEditingFlags(true, false, false);
    }

    @FXML private void handleEditPhone() {
        switchToEditMode(editPhoneField, phoneLabel, editPhoneButton);
        setEditingFlags(false, true, false);
    }

    @FXML private void handleEditAddress() {
        switchToEditMode(editAddressField, addressLabel, editAddressButton);
        setEditingFlags(false, false, true);
    }

    private void setEditingFlags(boolean name, boolean phone, boolean address) {
        editingName = name;
        editingPhone = phone;
        editingAddress = address;
    }

    private void switchToEditMode(TextField editField, Label label, Button editButton) {
        editField.setText(label.getText());
        toggleEditMode(true);

        editNameButton.setVisible(!editingName);
        editPhoneButton.setVisible(!editingPhone);
        editAddressButton.setVisible(!editingAddress);

        editNameField.setVisible(editingName);
        editPhoneField.setVisible(editingPhone);
        editAddressField.setVisible(editingAddress);

        nameLabel.setVisible(!editingName);
        phoneLabel.setVisible(!editingPhone);
        addressLabel.setVisible(!editingAddress);
    }

    @FXML private void handleSaveChanges() {
        boolean valid = true;

        if (editingName) {
            String newName = editNameField.getText().trim();
            if (newName.isEmpty() || newName.length() < 3) {
                showAlert("Invalid Name", "Name must be at least 3 characters.");
                valid = false;
            } else {
                currentUser.setName(newName);
                nameLabel.setText(newName);
            }
        }

        if (editingPhone) {
            String newPhone = editPhoneField.getText().trim();
            if (!newPhone.matches("\\d{11}")) {
                showAlert("Invalid Phone", "Phone must be exactly 11 digits.");
                valid = false;
            } else {
                currentUser.setPhone(newPhone);
                phoneLabel.setText(newPhone);
            }
        }

        if (editingAddress) {
            String newAddress = editAddressField.getText().trim();
            if (newAddress.isEmpty()) {
                showAlert("Invalid Address", "Address cannot be empty.");
                valid = false;
            } else {
                currentUser.setAddress(newAddress);
                addressLabel.setText(newAddress);
            }
        }

        if (valid) {
            showAlert("Success", "Profile updated successfully.");
            toggleEditMode(false);
        }

        setEditingFlags(false, false, false);
    }

    private void toggleEditMode(boolean isEditing) {
        saveButton.setVisible(isEditing);

        if (!isEditing) {
            nameLabel.setVisible(true);
            phoneLabel.setVisible(true);
            addressLabel.setVisible(true);

            editNameField.setVisible(false);
            editPhoneField.setVisible(false);
            editAddressField.setVisible(false);

            editNameButton.setVisible(true);
            editPhoneButton.setVisible(true);
            editAddressButton.setVisible(true);
        }
    }

    @FXML private void handleChangePassword() {
        Dialog<String[]> dialog = createPasswordChangeDialog();
        dialog.showAndWait().ifPresent(this::processPasswordChange);
    }

    private Dialog<String[]> createPasswordChangeDialog() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Please enter your current and new password.");

        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        PasswordField currentPass = new PasswordField();
        PasswordField newPass = new PasswordField();
        PasswordField confirmPass = new PasswordField();

        currentPass.setPromptText("Current Password");
        newPass.setPromptText("New Password");
        confirmPass.setPromptText("Confirm New Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPass, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPass, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPass, 1, 2);
        dialog.getDialogPane().setContent(grid);

        Node changeButton = dialog.getDialogPane().lookupButton(changeButtonType);
        changeButton.setDisable(true);

        currentPass.textProperty().addListener((obs, o, n) -> updateChangeButtonState(changeButton, currentPass, newPass, confirmPass));
        newPass.textProperty().addListener((obs, o, n) -> updateChangeButtonState(changeButton, currentPass, newPass, confirmPass));
        confirmPass.textProperty().addListener((obs, o, n) -> updateChangeButtonState(changeButton, currentPass, newPass, confirmPass));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                return new String[]{currentPass.getText(), newPass.getText(), confirmPass.getText()};
            }
            return null;
        });

        dialog.setOnShown(event -> {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.setWidth(400);
            stage.setHeight(250);
        });

        return dialog;
    }

    private void updateChangeButtonState(Node button, PasswordField current, PasswordField newPass, PasswordField confirm) {
        boolean disable = current.getText().isEmpty() || newPass.getText().isEmpty() || confirm.getText().isEmpty();
        button.setDisable(disable);
    }

    private void processPasswordChange(String[] passwords) {
        String current = passwords[0];
        String newPass = passwords[1];
        String confirm = passwords[2];

        if (!current.equals(currentUser.getPassword())) {
            showAlert("Error", "Current password is incorrect.");
        } else if (!newPass.equals(confirm)) {
            showAlert("Error", "New passwords do not match.");
        } else if (newPass.isEmpty()) {
            showAlert("Error", "New password cannot be empty.");
        } else if (newPass.equals(current)) {
            showAlert("Error", "New password cannot be the same as the current password.");
        } else {
            currentUser.setPassword(newPass);
            showAlert("Success", "Password changed successfully.");
        }
    }

    @FXML private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File file = fileChooser.showOpenDialog((Stage) profileImageView.getScene().getWindow());
        if (file != null) {
            try {
                String imagePath = file.toURI().toString();
                profileImageView.setImage(new Image(imagePath));
                currentUser.setImagePath(imagePath);
            } catch (Exception e) {
                showAlert("Upload Error", "Unable to load selected image.");
            }
        }
    }

    @FXML private void handleLogout() {
        if (!confirmLogout()) return;

        SessionManager.setLoggedInUser(null);
        try {
            AnchorPane loginPage = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Stage stage = (Stage) nameLabel.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            showAlert("Load Error", "Could not load login page.");
        }
    }

    private boolean confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setContentText("Are you sure you want to logout?");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteAccount() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Account");
        confirmAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmAlert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteUserAccount();
            SessionManager.setLoggedInUser(null);
            loadLoginScreen();
        }
    }

    private void deleteUserAccount() {
        User currentUser = SessionManager.getLoggedInUser();
        if (currentUser != null) {
            boolean deleted = UserData.deleteUser(currentUser.getGmail());
            if (deleted) {
                UserData.clearCurrentUser();
                SessionManager.setLoggedInUser(null);
                System.out.println("User account deleted successfully.");
            } else {
                System.out.println("Failed to delete user account.");
            }
        } else {
            System.out.println("No user logged in or user is null.");
        }
    }



    private void loadLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/restaurant/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) deleteAccountButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load login screen.");
        }
    }
}
