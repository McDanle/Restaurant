package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class historyController {

    @FXML
    private ListView<String> historyListView;

    @FXML
    public void initialize() {
        // Sample history entries (replace with real data later)
        historyListView.getItems().addAll(
                "Lumpia x2 - ₱10.00",
                "Adobo x1 - ₱45.00",
                "Halo-halo x3 - ₱75.00"
        );
    }
}
