package com.example.lokaverkefni_21;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label myBalance;
    @FXML
    private Slider betSlider;
    @FXML
    private Label valueToBetLabel, mySumLabel, houseSumLabel;
    @FXML
    private Button betButton;
    @FXML
    private Button passButton, hitMeButton;
    @FXML
    private Label playerCard1, playerCard2, houseCard1, houseCard2, playerDraws, houseDraws;

    Game game;
    Scripts script = new Scripts();

    Alert a = new Alert(Alert.AlertType.NONE);


    private void ClearUI() {
        valueToBetLabel.setText("");
        playerCard1.setText("");
        playerCard2.setText("");
        houseCard1.setText("");
        houseCard2.setText("");
        playerDraws.setText("");
        houseDraws.setText("");
        mySumLabel.setText("");
        houseSumLabel.setText("");
        betButton.setDisable(false);

        betSlider.setValue(0);
        betSlider.setMax(MyCurrentBalance());
        // usdPrice.setText(String.format("Current USD price: %.2f$ / 1M SMLY", GetUSDValueOfMillionSMLY()));
        myBalance.setText(String.format("My balance: %.2f SMLY", MyCurrentBalance()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //usdPrice.setText(String.format("Current USD price: %.2f$ / 1M SMLY", GetUSDValueOfMillionSMLY()));
        myBalance.setText(String.format("My balance: %.2f SMLY", MyCurrentBalance()));
        //myCardsLabel.setText("");

        passButton.setDisable(true);
        hitMeButton.setDisable(true);

        betSlider.setMin(0);
        betSlider.setMax(MyCurrentBalance());

        betSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
                valueToBetLabel.textProperty().setValue(String.format("%.2f", newValue)));
    }

    public double GetUSDValueOfMillionSMLY() {
        return Double.parseDouble(script.RunScript("./scripts/getCurrentUSDValue.sh")) * 1000000;
    }

    public double MyCurrentBalance() {
        return Double.parseDouble(script.RunScript("./scripts/getMyCurrentBalance.sh"));
    }

    private void addRestartButton() {
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setTitle("Game Over");
        a.setHeaderText(game.getGameOverMessage());
        a.setContentText("Play again?");
        Optional<ButtonType> result = a.showAndWait();

        if (result.get() == ButtonType.OK) {
            game.ResetGame();
            ClearUI();
        }
        else if (result.get() == ButtonType.CANCEL) System.out.println("Clicked CANCEL");
    }

    public void betHandle(ActionEvent actionEvent) throws IOException {
        if (betSlider.getValue() > 0) {
            double betAmount = betSlider.getValue();
            game = new Game(betAmount);
            betButton.setDisable(true);
            hitMeButton.setDisable(false);
            passButton.setDisable(false);

            houseCard1.setText(String.format("%d", game.getHouseCardsInHand(0)));
            houseCard2.setText("X");

            playerCard1.setText(String.format("%d", game.getPlayerCardsInHand(0)));
            playerCard2.setText(String.format("%d", game.getPlayerCardsInHand(1)));
            mySumLabel.setText(game.getSum());
            houseSumLabel.setText("");
        }
        else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Bet must be a value over 0");
            a.showAndWait();
        }
    }

    public void passHandle(ActionEvent actionEvent) throws InterruptedException, IOException {

        houseCard2.setText(String.format("%d", game.getHouseCardsInHand(1)));
        hitMeButton.setDisable(true);
        passButton.setDisable(true);
        game.Pass(houseDraws);
        houseSumLabel.setText(game.getHouseSum());
        addRestartButton();
    }

    public void hitMeHandle(ActionEvent actionEvent) throws IOException {
        if (!game.Play()) {
            hitMeButton.setDisable(true);
            playerDraws.setText(game.lastDrawnCard());
            mySumLabel.setText(game.getSum());
            addRestartButton();
        }
        else {
            playerDraws.setText(game.lastDrawnCard());
            mySumLabel.setText(game.getSum());
        }
    }
}