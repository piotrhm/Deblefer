package com.example.deblefer.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.CardInDialog;
import com.example.deblefer.Classes.PointsThread;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.Dialogs.GetCardsDialog;
import com.example.deblefer.Statistics.HandPower;
import com.example.deblefer.Classes.PointsLoadingRunner;
import com.example.deblefer.Statistics.Statistics;
import com.example.deblefer.Statistics.StatisticsGeneratorBox;
import com.example.deblefer.Statistics.StatisticsSettings;
import com.example.deblefer.Adapters.StatisticsViewAdapter;
import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TexasModuleActivity extends AppCompatActivity {

    private Collection<Card> deck = Deck.getModifableDeckAsSet();
    private List<Card> table = new ArrayList<>();
    private List<Card> hand = new ArrayList<>();
    private List<ImageView> cardImages = new ArrayList<>();
    private int playersCount = 2;

    private List<CardInDialog> listOfCardsInDialog = CardInDialog.getDeckCardInDialog();
    private StatisticsViewAdapter statisticsAdapter = new StatisticsViewAdapter(new ArrayList<>(), this);

    private RecyclerView recyclerView;
    private TextView handPowerTextView;
    private TextView playersCountTextView;
    private TextView pointsTextView;
    private Button addPlayerButton;
    private Button minusPlayerButton;
    private Button passButton;
    private Button randomButton;
    private FloatingActionButton addCardButton;

    @SuppressLint("StaticFieldLeak")
    private class UpdateStatisticsAsync extends AsyncTask<Object[], Void, List<Statistics>>{
        PointsThread pointsThread;
        StatisticsGeneratorBox statisticsGeneratorBox;
        private boolean interrupted = false;
        @Override
        protected void onPreExecute() {
            pointsTextView.setVisibility(View.VISIBLE);
            pointsTextView.setOnClickListener(v -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TexasModuleActivity.this);
                alertBuilder.setMessage("Do you want to stop calculations?")
                        .setNegativeButton("No", (dialog, which) -> {})
                        .setPositiveButton("Yes", (dialog, which) -> statisticsGeneratorBox.interrupt());
                alertBuilder.show();
            });
            pointsThread = new PointsThread(new PointsLoadingRunner(pointsTextView));
            pointsThread.start();
            setClickableButtons(false);
            statisticsAdapter.clearItems();
        }

        @Override
        protected List<Statistics> doInBackground(Object[]... objects) {
            StatisticsSettings statisticsSettings = new StatisticsSettings(TexasModuleActivity.this);               statisticsGeneratorBox = new StatisticsGeneratorBox(hand, table, deck,
                    playersCount, statisticsSettings, recyclerView);
            try {
                return statisticsGeneratorBox.getStatistics();
            } catch (InterruptedException e) {
                interrupted = true;
                return new ArrayList<>();
            }
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        protected void onPostExecute(List<Statistics> statistics) {
            setClickableButtons(true);
            if(getUsedCardCount() == 7)
                addCardButton.setClickable(false);
            if(!interrupted){
                pointsThread.setFinalText("overall chance: "
                        + String.format("%.2f", StatisticsGeneratorBox.getChanceOfWinning(statistics)*100)+"%");
                pointsTextView.setOnClickListener(v -> {});
            }
            else{
                pointsThread.setFinalText("restart");
                pointsTextView.setOnClickListener(v -> {
                    statisticsAdapter.clearItems();
                    runUpdateStatistics();
                });
            }
            pointsThread.interrupt();
        }
    }

    @SuppressLint("SetTextI18n")
    private void runUpdateStatistics(){
        if(getUsedCardCount() != 0){
            randomButton.setText("undo");
            randomButton.setOnClickListener(v -> undo());
        }
        if (getUsedCardCount() == 2)
            setHandPower();
        else if (getUsedCardCount() <= 7 && getUsedCardCount() >= 5)
            new UpdateStatisticsAsync().execute();

        if (getUsedCardCount() == 7)
            addCardButton.setClickable(false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Deck.initializeCardsImagesIds(this);
        HandPower.initializePowers(this);
        initializeViews();
        initializeListeners();
        restart();
    }

    private void initializeViews(){

        setContentView(R.layout.nested_scroll_view);

        cardImages.add((ImageView) findViewById(R.id.cardImageView0));
        cardImages.add((ImageView) findViewById(R.id.cardImageView1));
        cardImages.add((ImageView)findViewById(R.id.cardImageView2));
        cardImages.add((ImageView)findViewById(R.id.cardImageView3));
        cardImages.add((ImageView)findViewById(R.id.cardImageView4));
        cardImages.add((ImageView)findViewById(R.id.cardImageView5));
        cardImages.add((ImageView)findViewById(R.id.cardImageView6));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(statisticsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TexasModuleActivity.this));

        addCardButton = findViewById(R.id.addCardButton);
        randomButton = findViewById(R.id.randomButton);
        passButton = findViewById(R.id.passButton);
        minusPlayerButton = findViewById(R.id.minusPlayerButton);
        addPlayerButton = findViewById(R.id.addPlayerButton);
        playersCountTextView = findViewById(R.id.playersCountTextView);
        handPowerTextView = findViewById(R.id.handPowerTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        pointsTextView.setVisibility(View.INVISIBLE);
    }

    private void initializeListeners(){
        addCardButton.setOnClickListener(v -> {
            int maxCardsCount = 1;
            if (getUsedCardCount() < 2)
                maxCardsCount = 2-getUsedCardCount();
            else if (getUsedCardCount() < 5)
                maxCardsCount = 5-getUsedCardCount();
            GetCardsDialog dialog = new GetCardsDialog(this, listOfCardsInDialog, maxCardsCount);
            dialog.getDialog().show();
        });
        passButton.setOnClickListener(v -> restart());
        addPlayerButton.setOnClickListener(v -> {
            if(playersCount >= 6)
                return;
            playersCount++;
            setPlayersCountTextView();
        });
        minusPlayerButton.setOnClickListener(v -> {
            if(playersCount <= 2)
                return;
            playersCount--;
            setPlayersCountTextView();
        });
        randomButton.setOnClickListener(v -> undo());
    }

    @SuppressLint("SetTextI18n")
    private void undo(){
        if(getUsedCardCount() == 0){
            randomButton.setText("random");
            pointsTextView.setText("");
            pointsTextView.setClickable(false);
            randomButton.setOnClickListener(v -> {
                randomButton.setClickable(false);
                randomize();
                randomButton.setText("undo");
                randomButton.setOnClickListener(v1 -> undo());
                randomButton.setClickable(false);
            });
            return;
        }

        setCardViewInactive(cardImages.get(getUsedCardCount()-1));
        statisticsAdapter.clearItems();
        addCardButton.setClickable(true);
        Card removed;

        if(getUsedCardCount() <= 2){
            removed = hand.get(hand.size()-1);
            hand.remove(hand.size()-1);
            handPowerTextView.setText("");
        }
        else{
            removed = table.get(table.size()-1);
            table.remove(table.size()-1);
        }

        deck.add(removed);

        for (CardInDialog cardInDialog : listOfCardsInDialog)
            if(cardInDialog.getCard().equals(removed))
                cardInDialog.setUsed(false);

        if(getUsedCardCount() >= 5){
            pointsTextView.setText("restart");
            pointsTextView.setOnClickListener(v -> runUpdateStatistics());
        }
        else{
            pointsTextView.setText("");
            pointsTextView.setClickable(false);
        }
    }

    private void randomize(){
        restart();
        for (int i=0;i<2;i++){
            Card card = getRandomCard(deck);
            hand.add(card);
            deck.remove(card);
            setCardViewActive(cardImages.get(i), card);
        }

        for (int i=0;i<3;i++){
            Card card = getRandomCard(deck);
            table.add(card);
            deck.remove(card);
            setCardViewActive(cardImages.get(i+2), card);
        }

        setHandPower();

        for (CardInDialog cardInDialog : listOfCardsInDialog){
            if(!deck.contains(cardInDialog.getCard()))
                cardInDialog.setUsed(true);
        }
        runUpdateStatistics();
    }

    private void restart(){
        deck = Deck.getModifableDeckAsSet();
        table = new ArrayList<>();
        hand = new ArrayList<>();

        listOfCardsInDialog = CardInDialog.getDeckCardInDialog();
        handPowerTextView.setVisibility(View.INVISIBLE);
        handPowerTextView.setText("");
        pointsTextView.setVisibility(View.INVISIBLE);
        pointsTextView.setText("");
        statisticsAdapter.clearItems();

        setPlayersCountTextView();
        setClickableButtons(true);

        for (ImageView cardView : cardImages)
            setCardViewInactive(cardView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setHandPower(){
        if(hand.size() < 2){
            handPowerTextView.setVisibility(View.INVISIBLE);
            return;
        }
        Double handPower = HandPower.getHandPower(hand.get(0), hand.get(1), this);
        handPowerTextView.setVisibility(View.VISIBLE);
        handPowerTextView.setText("hand power: " + String.format("%.1f", handPower) + "%");
    }

    private int getUsedCardCount(){
        return hand.size() + table.size();
    }

    private void setCardViewActive(ImageView cardView, Card card){
        cardView.setImageResource(Deck.getCardImageId(card));
    }

    private void setCardViewInactive(ImageView cardView){
        cardView.setImageResource(R.drawable.unused);
    }

    @SuppressLint("SetTextI18n")
    private void setPlayersCountTextView(){
        if(statisticsAdapter.getItemCount() > 0){
            pointsTextView.setText("restart");
            pointsTextView.setOnClickListener(v -> runUpdateStatistics());
        }
        playersCountTextView.setText("players:\n" + playersCount);
    }

    private <T> T getRandomCard(Collection<T> from) {
        Random rnd = new Random();
        int i = rnd.nextInt(from.size());
        return new ArrayList<>(Collections.unmodifiableCollection(from)).get(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_texas_module, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.multiplayer:
                this.startActivity(new Intent(this, LogIn.class));
                return true;*/
            case R.id.settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.exit:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void approveChosenCardsInDialog(){
        for(CardInDialog cardInDialog : listOfCardsInDialog){
            if(cardInDialog.isChosen()){
                addCardToProperSet(cardInDialog.getCard());
                cardInDialog.setUsed(true);
            }
            cardInDialog.setChosen(false);
        }
        runUpdateStatistics();
    }

    public void rejectChosenCardsInDialog(){
        for(CardInDialog cardInDialog : listOfCardsInDialog)
            cardInDialog.setChosen(false);
    }

    private void addCardToProperSet(Card card){
        if(getUsedCardCount() < 2)
            hand.add(card);
        else
            table.add(card);
        cardImages.get(getUsedCardCount()-1).setImageResource(Deck.getCardImageId(card));
//        Picasso.with(this).load(Deck.getCardImageId(card)).noFade().into(cardImages.get(getUsedCardCount()-1));
        deck.remove(card);
    }

    private void setClickableButtons(boolean clickable){
        addCardButton.setClickable(clickable);
        addPlayerButton.setClickable(clickable);
        minusPlayerButton.setClickable(clickable);
        passButton.setClickable(clickable);
        randomButton.setClickable(clickable);
    }
}
