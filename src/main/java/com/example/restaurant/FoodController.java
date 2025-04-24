package com.example.restaurant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class FoodController {

    //BananaCue

    @FXML
    private Label bananacueQuantityLabel;

    private int bananacueQuantity = 0;

    @FXML
    public void increaseBananaCueQuantity() {
        bananacueQuantity++;
        updateBananaCueLabel();
    }

    @FXML
    public void decreaseBananaCueQuantity() {
        if (bananacueQuantity > 0) {
            bananacueQuantity--;
        }
        updateBananaCueLabel();
    }

    private void updateBananaCueLabel() {
        bananacueQuantityLabel.setText(String.valueOf(bananacueQuantity));
    }

    @FXML
    public void handleAddToCartBananaCue() {
        if (bananacueQuantity > 0) {
            DashBoardController.addItemToCartStatic("Banana Cue", 10.00, bananacueQuantity);
        }
    }

    //KWEK KWEK

    @FXML
    private Label KwekKwekQuantityLabel;
    private int KwekKwekQuantity = 0;

    @FXML
    public void increaseKwekKwekQuantity() {
        KwekKwekQuantity++;
        updateKwekKwekLabel();
    }
    @FXML
    public void decreaseKwekKwekQuantity() {
        if (KwekKwekQuantity > 0) {
            KwekKwekQuantity--;
        }
        updateKwekKwekLabel();
    }

    private void updateKwekKwekLabel(){
        KwekKwekQuantityLabel.setText(String.valueOf(KwekKwekQuantity));
    }

    @FXML
    private void handleAddToCartKwekKwek() {
        if (KwekKwekQuantity > 0) {
            DashBoardController.addItemToCartStatic("Kwek Kwek", 15.00, KwekKwekQuantity);
        }
    }

    //Ham Burger

    @FXML
    private Label HamBurgerQuantityLabel;
    private int HamBurgerQuantity = 0;

    @FXML
    public void increaseHamBurgerQuantity() {
        HamBurgerQuantity++;
        updateHamBurgerQuantityLabel();
    }
    @FXML
    public void decreaseHamBurgerQuantity() {
        if (HamBurgerQuantity > 0) {
            HamBurgerQuantity--;
        }
        updateHamBurgerQuantityLabel();
    }

    private void updateHamBurgerQuantityLabel() {
        HamBurgerQuantityLabel.setText(String.valueOf(HamBurgerQuantity));
    }

    @FXML
    private void handleAddToCartHamBurger() {
        if (HamBurgerQuantity > 0) {
            DashBoardController.addItemToCartStatic("Ham Burger", 59.00, HamBurgerQuantity);
        }
    }

    //Hot Dog

    @FXML
    private Label HotDogQuantityLabel;
    private int HotDogQuantity = 0;

    @FXML
    public void increaseHotDogQuantity() {
        HotDogQuantity++;
        updateHotDogQuantityLabel();
    }

    @FXML
    public void decreaseHotDogQuantity() {
        if (HotDogQuantity > 0) {
            HotDogQuantity--;
        }
        updateHotDogQuantityLabel();
    }

    private void updateHotDogQuantityLabel() {
        HotDogQuantityLabel.setText(String.valueOf(HotDogQuantity));
    }

    @FXML
    private void handleAddToCartHotDog() {
        if (HotDogQuantity > 0) {
            DashBoardController.addItemToCartStatic("Hot Dog", 35.00, HotDogQuantity);
        }
    }

}
