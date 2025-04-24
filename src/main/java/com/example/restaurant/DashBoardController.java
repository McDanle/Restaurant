package com.example.restaurant;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DashBoardController {

    @FXML
    private VBox menuItemsContainer;

    @FXML
    private TableView<Item> cartTable;

    @FXML
    private TableColumn<Item, String> itemNameColumn;

    @FXML
    private TableColumn<Item, Double> itemPriceColumn;

    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    @FXML
    private TableColumn<Item, Double> totalColumn;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    private ObservableList<Item> cartItems = FXCollections.observableArrayList();
    private static ObservableList<Item> staticCartItems;

    private static DashBoardController staticControllerInstance;

    @FXML
    public void initialize() {
        staticControllerInstance = this;

        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        itemPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        cartTable.setItems(cartItems);
        staticCartItems = cartItems;

        handleMainCourse();
        updateTotalPrice();

        // Initialize payment methods
        paymentMethodComboBox.getItems().addAll("Cash", "GCash", "Credit Card");
        paymentMethodComboBox.setValue("Cash");
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Item item : cartItems) {
            total += item.getTotal();
        }
        totalPriceLabel.setText(String.format("Total: ₱%.2f", total));
    }

    @FXML
    private void handleMainCourse() {
        loadMenu("/com/example/restaurant/Food-view.fxml");
    }

    @FXML
    private void handleDrinks() {
        loadMenu("/com/example/restaurant/Drinks.fxml");
    }

    private void loadMenu(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox menuContent = loader.load();
            menuItemsContainer.getChildren().clear();
            menuItemsContainer.getChildren().add(menuContent);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Load Error", "Could not load the menu: " + fxmlPath);
        }
    }

    @FXML
    private void removeSelectedItem() {
        Item selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            updateTotalPrice();
        }
    }

    @FXML
    private void clearCart() {
        cartItems.clear();
        updateTotalPrice();
    }

    @FXML
    private void checkout() {
        if (cartItems.isEmpty()) {
            showAlert("Cart is empty!", "Please add items to your cart before checking out.");
            return;
        }

        String paymentMethod = paymentMethodComboBox.getValue();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            showAlert("Select Payment Method", "Please choose a payment method before proceeding.");
            return;
        }

        StringBuilder order = new StringBuilder();
        for (Item item : cartItems) {
            order.append("- ")
                    .append(item.getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" = ₱")
                    .append(String.format("%.2f", item.getTotal()))
                    .append("\n");
        }

        order.append("\nPayment Method: ").append(paymentMethod);

        showAlert("Order Placed!", "You ordered:\n" + order);
        cartItems.clear();
        updateTotalPrice();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void addItemToCartStatic(String name, double price, int quantity) {
        if (staticCartItems != null) {
            for (Item item : staticCartItems) {
                if (item.getName().equals(name)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    if (staticControllerInstance != null) {
                        staticControllerInstance.updateTotalPrice();
                    }
                    return;
                }
            }
            staticCartItems.add(new Item(name, price, quantity));
            if (staticControllerInstance != null) {
                staticControllerInstance.updateTotalPrice();
            }
        }
    }

    public static class Item {
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty price;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty total;

        public Item(String name, double price, int quantity) {
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.total = new SimpleDoubleProperty(price * quantity);
        }

        public String getName() { return name.get(); }
        public double getPrice() { return price.get(); }
        public int getQuantity() { return quantity.get(); }
        public double getTotal() { return total.get(); }

        public SimpleStringProperty nameProperty() { return name; }
        public SimpleDoubleProperty priceProperty() { return price; }
        public SimpleIntegerProperty quantityProperty() { return quantity; }
        public SimpleDoubleProperty totalProperty() { return total; }

        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
            this.total.set(this.price.get() * quantity);
        }
    }
}
