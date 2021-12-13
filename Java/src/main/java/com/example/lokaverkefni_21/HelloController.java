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
    private Label usdPrice;
    @FXML
    private Label myBalance;
    @FXML
    private Slider betSlider;
    @FXML
    private Label valueToBetLabel, myCardsLabel, mySumLabel, winLoseLabel, houseCardsLabel, housePlaysLabel;
    @FXML
    private Button betButton;
    @FXML
    private Button passButton, hitMeButton;

    Game game;
    Scripts script = new Scripts();

    Alert a = new Alert(Alert.AlertType.NONE);


    private void ClearUI() {
        valueToBetLabel.setText("");
        myCardsLabel.setText("");
        mySumLabel.setText("");
        winLoseLabel.setText("");
        betButton.setDisable(false);
        houseCardsLabel.setText("");
        housePlaysLabel.setText("");

        betSlider.setValue(0);
        betSlider.setMax(MyCurrentBalance());
        usdPrice.setText(String.format("Current USD price: %.2f$ / 1M SMLY", GetUSDValueOfMillionSMLY()));
        myBalance.setText(String.format("My balance: %.2f SMLY", MyCurrentBalance()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        usdPrice.setText(String.format("Current USD price: %.2f$ / 1M SMLY", GetUSDValueOfMillionSMLY()));
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

    public void betHandle(ActionEvent actionEvent) {
        if (betSlider.getValue() > 0) {
            double betAmount = betSlider.getValue();
            game = new Game(betAmount);
            betButton.setDisable(true);
            hitMeButton.setDisable(false);
            passButton.setDisable(false);
        }
        else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Bet must be a value over 0");
            a.showAndWait();
        }
    }

    public void passHandle(ActionEvent actionEvent) throws InterruptedException, IOException {
        hitMeButton.setDisable(true);
        passButton.setDisable(true);
        game.Pass(houseCardsLabel);
        addRestartButton();
    }

    public void hitMeHandle(ActionEvent actionEvent) throws IOException {
        if (!game.Play()) {
            hitMeButton.setDisable(true);
            winLoseLabel.setText("You got " + game.getSum() + ", and lose!");
            addRestartButton();
        }
        StringBuilder cardsToLabel = new StringBuilder();
        for (int card:game.getPlayerCardsInHand()) cardsToLabel.append(card + ", ");
        myCardsLabel.setText(cardsToLabel.toString());
        mySumLabel.setText(game.getSum());
    }
}