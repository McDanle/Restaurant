package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryController {

    @FXML private TableView<DashBoardController.Order> historyTable;
    @FXML private TableColumn<DashBoardController.Order, String> itemNamesColumn;
    @FXML private TableColumn<DashBoardController.Order, String> itemQuantitiesColumn;
    @FXML private TableColumn<DashBoardController.Order, String> itemPricesColumn;
    @FXML private TableColumn<DashBoardController.Order, String> paymentMethodColumn;
    @FXML private TableColumn<DashBoardController.Order, Double> totalAmountColumn;
    @FXML private TableColumn<DashBoardController.Order, String> timeColumn;

    @FXML
    public void initialize() {
        itemNamesColumn.setCellValueFactory(new PropertyValueFactory<>("itemNames"));
        itemQuantitiesColumn.setCellValueFactory(new PropertyValueFactory<>("itemQuantities"));
        itemPricesColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrices"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        historyTable.setItems(DashBoardController.getOrderHistory());

        // Optional: Make text wrap for long cells
        wrapText(itemNamesColumn);
        wrapText(itemQuantitiesColumn);
        wrapText(itemPricesColumn);
    }

    private void wrapText(TableColumn<DashBoardController.Order, String> column) {
        column.setCellFactory(col -> {
            TableCell<DashBoardController.Order, String> cell = new TableCell<DashBoardController.Order, String>() {
                private final javafx.scene.text.Text text = new javafx.scene.text.Text();

                {
                    setGraphic(text);
                    text.wrappingWidthProperty().bind(col.widthProperty());
                    text.getStyleClass().add("table-cell");
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });
    }
}