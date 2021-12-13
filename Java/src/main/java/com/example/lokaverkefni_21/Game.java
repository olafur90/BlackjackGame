package com.example.lokaverkefni_21;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {

    private boolean over21;

    private ArrayList<Integer> playerCardsInHand;
    private ArrayList<Integer> houseCardsInHand;

    private int playerSum = 0;
    private int houseSum;
    private boolean playerWins;
    private boolean tie;

    private double betAmount;

    private String gameOverMessage;


    public String getGameOverMessage() {
        return gameOverMessage;
    }

    public void ResetGame() {
        playerSum = 0;
        houseSum = 0;

        playerCardsInHand.clear();
        houseCardsInHand.clear();

        over21 = false;
        playerWins = false;

        gameOverMessage = "";
    }

    public Game(double betAmount) {
        this.betAmount = betAmount;
        houseSum = houseInit();
        playerCardsInHand = new ArrayList<>();
        houseCardsInHand = new ArrayList<>();
    }

    private int houseInit() {
        houseCardsInHand = new ArrayList<>();
        for (int i = 0; i < 2; i++) houseCardsInHand.add(DrawCard());
        for (int card:houseCardsInHand) houseSum += card;

        System.out.println("House sum: " + houseSum);
        return houseSum;
    }

    private int DrawCard() {
        int[] cards = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        Random random = new Random();
        return cards[random.nextInt(0, cards.length - 1)];
    }

    public void Pass(Label houseCardsLabel) throws InterruptedException, IOException {
        HousePlays(playerSum, houseCardsLabel);
    }

    public ArrayList<Integer> getPlayerCardsInHand() {
        return playerCardsInHand;
    }

    public ArrayList<Integer> getHouseCardsInHand() {
        return houseCardsInHand;
    }


    private void HousePlays(int playerScore, Label houseCardsLabel) throws InterruptedException, IOException {
        StringBuilder cardsToLabel = new StringBuilder();

        for (int card:houseCardsInHand) cardsToLabel.append(card).append(", ");
        houseCardsLabel.setText(cardsToLabel.toString());                    // AF HVERJU KOMA EKKI FYRSTU SPILIN?

        while (playerScore > houseSum && houseSum < 21) {
            int card = DrawCard();
            houseCardsInHand.add(card);
            houseSum += card;
            System.out.println("House draws: " + card);
            System.out.println("House sum: " + houseSum);
            cardsToLabel.append(card + ", ");
            houseCardsLabel.setText(cardsToLabel.toString());
            Thread.sleep(1000);
        }

        if (houseSum > playerSum && houseSum <= 21) playerWins = false;
        else if (houseSum > 21) playerWins = true;
        else if (houseSum == playerSum) {
            tie = true;
            playerWins = false;
        }
        else if (playerSum <= 21 && playerSum > houseSum) playerWins = true;

        GameOver();
    }

    public boolean Play() throws IOException {
        if(!over21) {
            HitMe();
            if (playerSum > 21) {
                over21 = true;
                playerWins = false;
                GameOver();
                return false;
            }
        }
        return true;
    }

    public void GameOver() throws IOException {
         if (playerWins) {
             SendForWinnings(betAmount);
             gameOverMessage = "Yay! You win!";
         }
         else if(tie) gameOverMessage = "Tie";

         else {
             // TODO Virkja þetta þegar ég vill fara að senda á bottann
             SendLostSMLY();
             gameOverMessage = String.format("You lose %d SMLY. %d will be sent to charity", (int)betAmount, (int)(betAmount / 10));
         }

         System.out.println(gameOverMessage);
         System.out.println(playerWins);

    }

    public void SendForWinnings(double betAmount) throws IOException {

        double amount = (double)((int)betAmount);
        String cmd = String.format("/home/olafur/Documents/Skóli/Rafmyntir/Lokaverkefni/Java/scripts/sendAmountWhenWon.sh %f", amount);
        System.out.println(amount);
        ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
        pb.start();
    }

    public void SendLostSMLY() throws IOException {
        int sendToCharity = (int)(betAmount / 10);
        int sendAmountToHouse = (int)betAmount - sendToCharity;
        double sendWithLoseTag = (double) sendAmountToHouse + 0.01;

        String cmd = String.format("/home/olafur/Documents/Skóli/Rafmyntir/Lokaverkefni/Java/scripts/sendAmountWhenLose.sh %f %d", sendWithLoseTag, sendToCharity);
        ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
        pb.start();
    }

    public void HitMe() {
        int card = DrawCard();
        playerCardsInHand.add(card);
        playerSum += card;
    }

    public String getSum() {
        return String.valueOf(playerSum);
    }
}