package com.example.restaurant;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private static String currentUsername;

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    private ObservableList<Item> cartItems = FXCollections.observableArrayList();
    private static ObservableList<Item> staticCartItems;
    private static ObservableList<Order> orderHistory = FXCollections.observableArrayList();

    private static DashBoardController staticControllerInstance;

    @FXML
    public void initialize() {
        staticControllerInstance = this; // Initialize static instance
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

    @FXML
    private void handleHistory() {
        loadMenu("/com/example/restaurant/history.fxml");
    }

    @FXML
    private void handleProfile() {
        loadMenu("/com/example/restaurant/Profile.fxml");
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

        double total = 0;
        StringBuilder orderDetails = new StringBuilder();
        StringBuilder itemNames = new StringBuilder();
        StringBuilder itemQuantities = new StringBuilder();
        StringBuilder itemPrices = new StringBuilder();

        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        for (Item item : cartItems) {
            total += item.getTotal();

            itemNames.append(item.getName()).append("\n");
            itemQuantities.append("x").append(item.getQuantity()).append("\n");
            itemPrices.append(String.format("₱%.2f", item.getPrice())).append("\n");

            orderDetails.append("- ")
                    .append(item.getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" = ₱")
                    .append(String.format("%.2f", item.getTotal()))
                    .append("\n");
        }

        orderDetails.append("\nPayment Method: ").append(paymentMethod);

        orderHistory.add(new Order(
                itemNames.toString().trim(),
                itemQuantities.toString().trim(),
                itemPrices.toString().trim(),
                paymentMethod,
                total,
                time,
                currentUsername
        ));

        // Refresh HistoryController after checkout
        if (HistoryController.staticHistoryControllerInstance != null) {
            HistoryController.staticHistoryControllerInstance.loadOrderHistory();
        }

        showReceipt(orderDetails.toString(), total, paymentMethod);
        cartItems.clear();
        updateTotalPrice();
    }

    private void showReceipt(String orderText, double totalAmount, String paymentMethod) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("======== TASTYTAP RECEIPT ========\n");
        receipt.append(orderText);
        receipt.append("\n----------------------------------\n");
        receipt.append(String.format("TOTAL: ₱%.2f\n", totalAmount));
        receipt.append("Payment Method: ").append(paymentMethod).append("\n");
        receipt.append("Thank you for your order!\n");
        receipt.append("==================================");

        TextArea receiptArea = new TextArea(receipt.toString());
        receiptArea.setEditable(false);
        receiptArea.setWrapText(true);

        Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
        receiptAlert.setTitle("Receipt");
        receiptAlert.setHeaderText("Order Receipt");
        receiptAlert.getDialogPane().setContent(receiptArea);
        receiptAlert.showAndWait();
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
                    staticControllerInstance.updateTotalPrice();
                    return;
                }
            }
            staticCartItems.add(new Item(name, price, quantity));
            staticControllerInstance.updateTotalPrice();
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

    public static ObservableList<Order> getOrderHistory() {
        return orderHistory;
    }

    public static class Order {
        private final SimpleStringProperty itemNames;
        private final SimpleStringProperty itemQuantities;
        private final SimpleStringProperty itemPrices;
        private final SimpleStringProperty paymentMethod;
        private final SimpleDoubleProperty totalAmount;
        private final SimpleStringProperty time;
        private final SimpleStringProperty username;

        public Order(String itemNames, String itemQuantities, String itemPrices,
                     String paymentMethod, double totalAmount, String time, String username) {
            this.itemNames = new SimpleStringProperty(itemNames);
            this.itemQuantities = new SimpleStringProperty(itemQuantities);
            this.itemPrices = new SimpleStringProperty(itemPrices);
            this.paymentMethod = new SimpleStringProperty(paymentMethod);
            this.totalAmount = new SimpleDoubleProperty(totalAmount);
            this.time = new SimpleStringProperty(time);
            this.username = new SimpleStringProperty(username);
        }

        public String getItemNames() { return itemNames.get(); }
        public String getItemQuantities() { return itemQuantities.get(); }
        public String getItemPrices() { return itemPrices.get(); }
        public String getPaymentMethod() { return paymentMethod.get(); }
        public double getTotalAmount() { return totalAmount.get(); }
        public String getTime() { return time.get(); }
        public String getUsername() { return username.get(); }

        public SimpleStringProperty itemNamesProperty() { return itemNames; }
        public SimpleStringProperty itemQuantitiesProperty() { return itemQuantities; }
        public SimpleStringProperty itemPricesProperty() { return itemPrices; }
        public SimpleStringProperty paymentMethodProperty() { return paymentMethod; }
        public SimpleDoubleProperty totalAmountProperty() { return totalAmount; }
        public SimpleStringProperty timeProperty() { return time; }
        public SimpleStringProperty usernameProperty() { return username; }
    }
    @FXML
    private Label nameLabel;

    private String Username;

    public void setUsername(String username) {
        currentUsername = username;
        if (nameLabel != null) {
            nameLabel.setText("Welcome, " + username + "!");
        }
    }

}
