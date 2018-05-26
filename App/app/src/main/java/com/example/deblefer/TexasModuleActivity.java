package com.example.deblefer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deblefer.Classes.Card;
import com.example.deblefer.Classes.CustomDialog;
import com.example.deblefer.Classes.Deck;
import com.example.deblefer.Classes.HandPower;
import com.example.deblefer.Classes.PointsLoadingRunner;
import com.example.deblefer.Classes.Statistics;
import com.example.deblefer.Classes.StatisticsGenerator;
import com.example.deblefer.Classes.StatisticsSettings;
import com.example.deblefer.Classes.TestStatisticsViewAdapter;

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

    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private TestStatisticsViewAdapter recyclerAdapter = new TestStatisticsViewAdapter(new ArrayList<>());
    private TextView handPowerTextView;
    private TextView playersCountTextView;
    private Button addPlayerButton;
    private Button minusPlayerButton;
    private Button passButton;
    private Button randomButton;
    private TextView pointsTextView;

    private class UpdateStatisticsAsync extends AsyncTask<Object[], Void, List<Statistics>>{
        Thread pointsThread;

        @Override
        protected void onPreExecute() {
            pointsTextView.setVisibility(View.VISIBLE);
            pointsThread = new Thread(new PointsLoadingRunner(pointsTextView));
            pointsThread.start();
            setClickableButtons(false);
            recyclerAdapter.clearItems();
        }

        @Override
        protected List<Statistics> doInBackground(Object[]... objects) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TexasModuleActivity.this);
            boolean draws = prefs.getBoolean("pref_drawsCheckBox", false);
            double minChanceOfGetting = ((double)Integer.valueOf(prefs.getString("pref_chanceOfGetting_limit","1")))/100;
            StatisticsSettings statisticsSettings = new StatisticsSettings(minChanceOfGetting,draws);
            return StatisticsGenerator.getStatistics(hand, table, deck, playersCount, statisticsSettings, recyclerView);
        }

        @Override
        protected void onPostExecute(List<Statistics> statistics) {
            setClickableButtons(true);
            if(getUsedCardCount() == 7)
                addButton.setClickable(false);
            pointsThread.interrupt();
            pointsTextView.setVisibility(View.INVISIBLE);
        }
    }

    class onDialogFinishHandler implements CustomDialog.onGetCardDialogFinish{
        Collection<Card> addedCards = null;

        @Override
        public void addCards(Collection<Card> addedCards){
            this.addedCards = addedCards;
        }

        @Override
        public void run() {

            Card card = addedCards.iterator().next();

            if (card == null)
                return;

            TexasModuleActivity.this.setCardViewActive(cardImages.get(TexasModuleActivity.this.getUsedCardCount()), card);
            deck.remove(card);

            if (TexasModuleActivity.this.getUsedCardCount() < 2)
                hand.add(card);
            else
                table.add(card);

            if (TexasModuleActivity.this.getUsedCardCount() == 2) {
                setHandPower();
            }
            else if (TexasModuleActivity.this.getUsedCardCount() == 5) {
                new UpdateStatisticsAsync().execute();
            }
            else if (TexasModuleActivity.this.getUsedCardCount() == 6) {
                new UpdateStatisticsAsync().execute();
            }

            if (TexasModuleActivity.this.getUsedCardCount() == 7) {
                new UpdateStatisticsAsync().execute();
                addButton.setClickable(false);
            }
        }

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

    private void initializeListeners(){
        addButton.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(TexasModuleActivity.this);
            AlertDialog alertDialog = dialog.showDialog(deck, new onDialogFinishHandler());
            alertDialog.show();
        });
        passButton.setOnClickListener(v -> {
            restart();
        });
        addPlayerButton.setOnClickListener(v -> {
            playersCount++;
            setPlayersCountTextView();
        });
        minusPlayerButton.setOnClickListener(v -> {
            playersCount--;
            setPlayersCountTextView();
        });
        randomButton.setOnClickListener(v -> {
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

            Log.println(Log.ASSERT, "XD", hand.toString());
            Log.println(Log.ASSERT, "XD", table.toString());
            Log.println(Log.ASSERT, "XD", Integer.toString(deck.size()) + " " + deck.toString());

            new UpdateStatisticsAsync().execute();

        });
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
//      recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TexasModuleActivity.this));

        addButton = findViewById(R.id.addCardButton);
        randomButton = findViewById(R.id.randomButton);
        passButton = findViewById(R.id.passButton);
        minusPlayerButton = findViewById(R.id.minusPlayerButton);
        addPlayerButton = findViewById(R.id.addPlayerButton);
        playersCountTextView = findViewById(R.id.playersCountTextView);
        handPowerTextView = findViewById(R.id.handPowerTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        pointsTextView.setVisibility(View.INVISIBLE);
    }

    private void restart(){
        deck = Deck.getModifableDeckAsSet();
        table = new ArrayList<>();
        hand = new ArrayList<>();

        setPlayersCountTextView();
        handPowerTextView.setVisibility(View.INVISIBLE);
        recyclerAdapter.clearItems();

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
        handPowerTextView.setText("HAND POWER: " + String.format("%.1f", handPower) + "%");
    }

    private int getUsedCardCount(){
        return hand.size() + table.size();
    }

    private void setCardViewActive(ImageView cardView, Card card){
//        cardView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide));
        cardView.setImageResource(Deck.getCardImageId(card));
    }

    private void setCardViewInactive(ImageView cardView){
        cardView.setImageResource(R.drawable.unused);
    }

    private void setPlayersCountTextView(){
        playersCountTextView.setText("players:\n" + playersCount);
    }

    private <T> T getRandomCard(Collection<T> from) {
        Random rnd = new Random();
        int i = rnd.nextInt(from.size());
        return new ArrayList<T>(Collections.unmodifiableCollection(from)).get(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_texas_module, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.multiplayer:
                this.startActivity(new Intent(this,Multiplayer.class));
                return true;
            case R.id.settings:
                this.startActivity(new Intent(this,SettingsActivity.class));
                return true;
            case R.id.exit:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void setClickableButtons(boolean clickable){
        addButton.setClickable(clickable);
        addPlayerButton.setClickable(clickable);
        minusPlayerButton.setClickable(clickable);
        passButton.setClickable(clickable);
        randomButton.setClickable(clickable);
    }

}
