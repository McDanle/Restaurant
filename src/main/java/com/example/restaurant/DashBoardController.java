package com.example.restaurant;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class DashBoardController {

    @FXML private VBox menuItemsContainer;
    @FXML private TableView<Item> cartTable;
    @FXML private TableColumn<Item, String> itemNameColumn;
    @FXML private TableColumn<Item, Double> itemPriceColumn;
    @FXML private TableColumn<Item, Integer> quantityColumn;
    @FXML private TableColumn<Item, Double> totalColumn;
    @FXML private Label totalPriceLabel;
    @FXML private VBox gcashDetailsBox;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private Label nameLabel;
    @FXML private TextField gcashNumberField;
    @FXML private TextField gcashReferenceField;

    private static String currentUsername;
    private String gcashName = "";
    private String gcashNumber = "";

    private ObservableList<Item> cartItems = FXCollections.observableArrayList();
    private static ObservableList<Item> staticCartItems;
    private static ObservableList<Order> orderHistory = FXCollections.observableArrayList();
    public static DashBoardController staticControllerInstance;

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    @FXML
    private void initialize() {
        staticControllerInstance = this;

        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        itemPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        cartTable.setItems(cartItems);
        staticCartItems = cartItems;

        handleMainCourse();
        updateTotalPrice();

        paymentMethodComboBox.getItems().addAll("Cash", "GCash");
        paymentMethodComboBox.setValue("Cash");

        gcashDetailsBox.setVisible(false);
        gcashDetailsBox.setManaged(false);
    }

    private void updateTotalPrice() {
        double total = cartItems.stream().mapToDouble(Item::getTotal).sum();
        totalPriceLabel.setText(String.format("Total: ₱%.2f", total));
    }

    @FXML
    private void handlePaymentMethodChange() {
        boolean isGCash = "GCash".equals(paymentMethodComboBox.getValue());
        gcashDetailsBox.setVisible(isGCash);
        gcashDetailsBox.setManaged(isGCash);
    }

    @FXML private void handleMainCourse() { loadMenu("/com/example/restaurant/Food-view.fxml"); }
    @FXML private void handleDrinks() { loadMenu("/com/example/restaurant/Drinks.fxml"); }
    @FXML private void handleHistory() { loadMenu("/com/example/restaurant/history.fxml"); }
    @FXML private void handleProfile() { loadMenu("/com/example/restaurant/Profile.fxml"); }

    private void loadMenu(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox menuContent = loader.load();
            menuItemsContainer.getChildren().setAll(menuContent);
        } catch (IOException e) {
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

        if ("GCash".equalsIgnoreCase(paymentMethod)) {
            String number = gcashNumberField.getText().trim();
            String reference = gcashReferenceField.getText().trim();

            if (number.isEmpty() || reference.isEmpty()) {
                showAlert("Incomplete GCash Payment", "Please complete the GCash payment details.");
                return;
            }

            if (!number.matches("\\d{11}")) {
                showAlert("Invalid GCash Number", "Please enter a valid 11-digit GCash number.");
                return;
            }

            if (reference.length() < 6) {
                showAlert("Invalid Reference", "Reference number seems too short.");
                return;
            }

            gcashNumber = number;
            gcashName = getCurrentUsername();

            processOrder("GCash", number, reference);

            gcashNumberField.clear();
            gcashReferenceField.clear();

        } else {
            gcashNumber = "";
            gcashName = "";
            processOrder("Cash", null, null);
        }
    }


    private void processOrder(String paymentMethod, String number, String reference) {
        double total = 0;
        StringBuilder orderDetails = new StringBuilder();
        StringBuilder itemNames = new StringBuilder();
        StringBuilder itemQuantities = new StringBuilder();
        StringBuilder itemPrices = new StringBuilder();

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        for (Item item : cartItems) {
            total += item.getTotal();
            itemNames.append(item.getName()).append("\n");
            itemQuantities.append("x").append(item.getQuantity()).append("\n");
            itemPrices.append(String.format("₱%.2f", item.getPrice())).append("\n");

            orderDetails.append("- ").append(item.getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = ₱").append(String.format("%.2f", item.getTotal()))
                    .append("\n");
        }

        orderDetails.append("\nPayment Method: ").append(paymentMethod);
        if ("GCash".equalsIgnoreCase(paymentMethod)) {
            orderDetails.append("\nGCash Number: ").append(number);
            orderDetails.append("\nReference No.: ").append(reference);
        }

        orderHistory.add(new Order(
                itemNames.toString().trim(),
                itemQuantities.toString().trim(),
                itemPrices.toString().trim(),
                paymentMethod,
                total,
                time,
                currentUsername
        ));

        if (HistoryController.staticHistoryControllerInstance != null) {
            HistoryController.staticHistoryControllerInstance.loadOrderHistory();
        }

        showReceipt("Order-" + System.currentTimeMillis(), orderDetails.toString(), total, paymentMethod, currentUsername, gcashNumber);


        cartItems.clear();
        updateTotalPrice();
        gcashName = "";
        gcashNumber = "";
    }

    private void showReceipt(String orderId, String orderText, double totalAmount, String paymentMethod, String customerName, String contactNumber) {
        StringBuilder receipt = new StringBuilder();

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        String formattedTime = now.format(DateTimeFormatter.ofPattern("hh:mm a"));

        double vat = totalAmount * 0.12;
        double subtotal = totalAmount - vat;

        receipt.append("🍽 TastyTap Restaurant\n");
        receipt.append("📍 123 Street, City\n");
        receipt.append("📞 (123) 456-7890\n");
        receipt.append("📧 tastytap@example.com\n\n");

        receipt.append("🧾 Order Receipt\n");
        receipt.append("Order ID: ").append(orderId).append("\n\n");
        receipt.append("Date: ").append(formattedDate).append("\n");
        receipt.append("Time: ").append(formattedTime).append("\n\n");

        receipt.append("Payment Method: ").append(paymentMethod).append("\n");
        receipt.append("Order Type: Dine-in / Take-out\n\n");

        receipt.append("👤 Customer Info\n");
        receipt.append("Name: ").append(customerName).append("\n");
        receipt.append("Contact: ").append(contactNumber).append("\n\n");

        receipt.append("🍔 Order Summary\n");
        receipt.append("Qty\tItem\t\tUnit Price\tTotal\n");
        receipt.append(orderText).append("\n\n");

        receipt.append(String.format("Subtotal:\t\t\t\t₱%.2f\n", subtotal));
        receipt.append(String.format("VAT (12%%):\t\t\t\t₱%.2f\n", vat));
        receipt.append("Discount:\t\t\t\t₱0.00\n");
        receipt.append(String.format("Total Amount:\t\t\t₱%.2f\n\n", totalAmount));

        receipt.append("💵 Payment Info\n");
        receipt.append(String.format("Amount Paid:\t\t\t₱%.2f\n", totalAmount));
        receipt.append("Change:\t\t\t\t₱0.00\n\n");

        receipt.append("✅ Order Status\n");
        receipt.append("✔️ Payment Received\n");
        receipt.append("🧑‍🍳 Preparing your order\n");
        receipt.append("⏱ Estimated Time: 30 minutes\n\n");

        receipt.append("🙏 Thank You!\n");
        receipt.append("Thank you for ordering from TastyTap!\n");
        receipt.append("We appreciate your support and hope to serve you again soon.");

        TextArea receiptArea = new TextArea(receipt.toString());
        receiptArea.setEditable(false);
        receiptArea.setWrapText(true);
        receiptArea.setPrefWidth(500);
        receiptArea.setPrefHeight(600);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receipt");
        alert.setHeaderText("Order Receipt");
        alert.getDialogPane().setContent(receiptArea);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUsername(String username) {
        currentUsername = username;
        if (nameLabel != null) {
            nameLabel.setText("Welcome, " + username + "!");
        }
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

    public static ObservableList<Order> getOrderHistory() {
        return orderHistory;
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
        public void setName(String name) { this.name.set(name); }
        public StringProperty nameProperty() { return name; }

        public double getPrice() { return price.get(); }
        public void setPrice(double price) { this.price.set(price); }

        public int getQuantity() { return quantity.get(); }
        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
            this.total.set(getPrice() * quantity);
        }

        public double getTotal() { return total.get(); }
        public DoubleProperty totalProperty() { return total; }

        public IntegerProperty quantityProperty() { return quantity; }
        public DoubleProperty priceProperty() { return price; }
    }


    public static class Order implements Serializable {
        private static final long serialVersionUID = 1L;

        private final StringProperty itemNames;
        private final StringProperty itemQuantities;
        private final StringProperty itemPrices;
        private final StringProperty paymentMethod;
        private final DoubleProperty totalAmount;
        private final StringProperty time;
        private final StringProperty username;

        public Order(String itemNames, String itemQuantities, String itemPrices, String paymentMethod, double totalAmount, String time, String username) {
            this.itemNames = new SimpleStringProperty(itemNames);
            this.itemQuantities = new SimpleStringProperty(itemQuantities);
            this.itemPrices = new SimpleStringProperty(itemPrices);
            this.paymentMethod = new SimpleStringProperty(paymentMethod);
            this.totalAmount = new SimpleDoubleProperty(totalAmount);
            this.time = new SimpleStringProperty(time);
            this.username = new SimpleStringProperty(username);
        }

        public StringProperty itemNamesProperty() { return itemNames; }
        public StringProperty itemQuantitiesProperty() { return itemQuantities; }
        public StringProperty itemPricesProperty() { return itemPrices; }
        public StringProperty paymentMethodProperty() { return paymentMethod; }
        public DoubleProperty totalAmountProperty() { return totalAmount; }
        public StringProperty timeProperty() { return time; }
        public StringProperty usernameProperty() { return username; }

        public String getItemNames() { return itemNames.get(); }
        public String getItemQuantities() { return itemQuantities.get(); }
        public String getItemPrices() { return itemPrices.get(); }
        public String getPaymentMethod() { return paymentMethod.get(); }
        public double getTotalAmount() { return totalAmount.get(); }
        public String getTime() { return time.get(); }
        public String getUsername() { return username.get(); }
    }
}
