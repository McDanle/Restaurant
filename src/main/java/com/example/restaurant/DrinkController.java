package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DrinkController {

    //Buko Juice

    @FXML
    private Label BukoJuiceQuantityLabel;
    private int BukoJuiceQuantity = 0;

    @FXML
    public void increaseBukoJuiceQuantity() {
        BukoJuiceQuantity++;
        updateBukoJuiceQuantityLabel();
    };

    @FXML
    public void decreaseBukoJuiceQuantity() {
        if (BukoJuiceQuantity > 0) {
            BukoJuiceQuantity--;
        }
        updateBukoJuiceQuantityLabel();
    }

    private void updateBukoJuiceQuantityLabel() {
        BukoJuiceQuantityLabel.setText(Integer.toString(BukoJuiceQuantity));
    }

    @FXML
    public void handleAddToCartBukoJuice() {
        DashBoardController.addItemToCartStatic("Buko Juice", 10.00, BukoJuiceQuantity);
    }

    //Coke

    @FXML
    private Label CokeQuantityLabel;
    private int CokeQuantity = 0;

    @FXML
    public void increaseCokeQuantity() {
        CokeQuantity++;
        updateCokeQuantityLabel();
    }
    @FXML
    public void decreaseCokeQuantity() {
        if (CokeQuantity > 0) {
            CokeQuantity--;
        }
        updateCokeQuantityLabel();
    }

    private void updateCokeQuantityLabel() {
        CokeQuantityLabel.setText(Integer.toString(CokeQuantity));
    }

    @FXML
    public void handleAddToCartCoke() {
        DashBoardController.addItemToCartStatic("Coke", 20.00, CokeQuantity);
    }

    //Iced Tea

    @FXML
    private Label IcedTeaQuantityLabel;
    private int IcedTeaQuantity = 0;

    @FXML
    public void increaseIcedTeaQuantity() {
        IcedTeaQuantity++;
        updateIcedTeaQuantityLabel();
    }

    @FXML
    public void decreaseIcedTeaQuantity() {
        if (IcedTeaQuantity > 0) {
            IcedTeaQuantity--;
        }
        updateIcedTeaQuantityLabel();
    }

    private void updateIcedTeaQuantityLabel() {
        IcedTeaQuantityLabel.setText(Integer.toString(IcedTeaQuantity));
    }

    @FXML
    public void handleAddToCartIcedTea() {
        DashBoardController.addItemToCartStatic("Iced Tea", 20.00, IcedTeaQuantity);
    }
}
