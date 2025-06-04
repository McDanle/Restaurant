package com.example.restaurant;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HistoryController {

    public static HistoryController staticHistoryControllerInstance;

    @FXML
    private TableView<DashBoardController.Order> historyTable;
    @FXML
    private TableColumn<DashBoardController.Order, String> itemNamesColumn;
    @FXML
    private TableColumn<DashBoardController.Order, String> itemQuantitiesColumn;
    @FXML
    private TableColumn<DashBoardController.Order, String> itemPricesColumn;
    @FXML
    private TableColumn<DashBoardController.Order, String> paymentMethodColumn;
    @FXML
    private TableColumn<DashBoardController.Order, Double> totalAmountColumn;
    @FXML
    private TableColumn<DashBoardController.Order, String> timeColumn;
    @FXML
    private TableColumn<DashBoardController.Order, String> nameColumn;

    @FXML
    public void initialize() {
        staticHistoryControllerInstance = this;

        itemNamesColumn.setCellValueFactory(data -> data.getValue().itemNamesProperty());
        itemQuantitiesColumn.setCellValueFactory(data -> data.getValue().itemQuantitiesProperty());
        itemPricesColumn.setCellValueFactory(data -> data.getValue().itemPricesProperty());
        paymentMethodColumn.setCellValueFactory(data -> data.getValue().paymentMethodProperty());
        totalAmountColumn.setCellValueFactory(data -> data.getValue().totalAmountProperty().asObject());
        timeColumn.setCellValueFactory(data -> data.getValue().timeProperty());
        nameColumn.setCellValueFactory(data -> data.getValue().usernameProperty());

        loadOrderHistory();
    }

    // ✅ FIXED: Now shows all orders, not just orders for logged-in user
    public void loadOrderHistory() {
        ObservableList<DashBoardController.Order> allOrders = DashBoardController.getOrderHistory();
        historyTable.setItems(allOrders);
    }
}
