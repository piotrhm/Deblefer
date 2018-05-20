package com.example.deblefer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
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
import com.example.deblefer.Classes.Game;
import com.example.deblefer.Classes.IconData;
import com.example.deblefer.Classes.StatisticViewAdapter;
import com.example.deblefer.Classes.Statistics;
import com.example.deblefer.Classes.StatisticsGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TexasModuleActivity extends AppCompatActivity {

    private Collection<Card> deck = Deck.getModifableDeckAsSet();
    private List<Card> table = new ArrayList<>();
    private List<Card> hand = new ArrayList<>();
    private Game game = new Game(2);
    private List<ImageView> cardImages = new ArrayList<>();
    private int playersCount = 2;

    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private StatisticViewAdapter adapter;
    private TextView handPowerTextView;
    private TextView playersCountTextView;
    private Button addPlayerButton;
    private Button minusPlayerButton;
    private Button passButton;
    private Button randomButton;

    class onDialogFinishHandler implements CustomDialog.onGetCardDialogFinish{
        Collection<Card> addedCards = null;

        @Override
        public void addCards(Collection<Card> addedCards){
            this.addedCards = addedCards;
        }

        @Override
        public void run() {
            Card card = addedCards.iterator().next();
            adapter = new StatisticViewAdapter(null);

            if (card == null)
                return;

            TexasModuleActivity.this.setCardViewActive(cardImages.get(TexasModuleActivity.this.getUsedCardCount()), card);
            deck.remove(card);

            if (TexasModuleActivity.this.getUsedCardCount() < 2)
                hand.add(card);
            else
                table.add(card);

            if (TexasModuleActivity.this.getUsedCardCount() == 2) {
                // PREFLOP
            } else if (TexasModuleActivity.this.getUsedCardCount() == 5) {
                updateStatsView(StatisticsGenerator.getStatistics(hand, table, deck));
                //setStats(StatisticsGenerator.getStatistics(hand, table, deck));
            } else if (TexasModuleActivity.this.getUsedCardCount() == 6) {
                //updateStatsView(Arrays.asList(deck.toArray()));
                //setStats(StatisticsGenerator.getStatistics(hand, table, deck));
            }
            if (TexasModuleActivity.this.getUsedCardCount() == 7) {
                addButton.setClickable(false);
                //setStats(StatisticsGenerator.getStatistics(hand, table, deck));
            }
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Deck.initializeCardsImagesIds(this);
        setContentView(R.layout.nested_scroll_view);
        cardImages.add((ImageView) findViewById(R.id.cardImageView0));
        cardImages.add((ImageView) findViewById(R.id.cardImageView1));
        cardImages.add((ImageView)findViewById(R.id.cardImageView2));
        cardImages.add((ImageView)findViewById(R.id.cardImageView3));
        cardImages.add((ImageView)findViewById(R.id.cardImageView4));
        cardImages.add((ImageView)findViewById(R.id.cardImageView5));
        cardImages.add((ImageView)findViewById(R.id.cardImageView6));
        addButton = findViewById(R.id.addCardButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        randomButton = findViewById(R.id.randomButton);
        passButton = findViewById(R.id.passButton);
        minusPlayerButton = findViewById(R.id.minusPlayerButton);
        addPlayerButton = findViewById(R.id.addPlayerButton);
        playersCountTextView = findViewById(R.id.playersCountTextView);
        handPowerTextView = findViewById(R.id.handPowerTextView);

        addButton.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(TexasModuleActivity.this);
            AlertDialog alertDialog = dialog.showDialog(1, deck, new onDialogFinishHandler());
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

            for (Card card : hand)
                Log.println(Log.ASSERT, "XD", card.toString());

            for (Card card : table)
                Log.println(Log.ASSERT, "XD", card.toString());
            Log.println(Log.ASSERT, "XD", Integer.toString(deck.size()));


            updateStatsView(StatisticsGenerator.getStatistics(hand, table, deck));

        });

        restart();
    }

    private void restart(){
        deck = Deck.getModifableDeckAsSet();
        table = new ArrayList<>();
        hand = new ArrayList<>();

        handPowerTextView.setVisibility(View.INVISIBLE);
        for (ImageView cardView : cardImages)
            setCardViewInactive(cardView);

//        updateStatsView(new ArrayList<>());
    }

    private int getUsedCardCount(){
        return hand.size() + table.size();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCardViewActive(ImageView cardView, Card card){
        cardView.setImageResource(Deck.getCardImageId(card));
    }

    private void setCardViewInactive(ImageView cardView){
        cardView.setImageResource(R.drawable.unused);
    }

    private void updateStatsView(List<Statistics> listOfStats){
        Log.println(Log.ASSERT, "XD", listOfStats.toString());

        if(listOfStats.size() == 0) {
            recyclerView.setAdapter(null);
            return;
        }

        IconData[] data = new IconData[listOfStats.size()];
        for(int i = 0; i < listOfStats.size(); i++) {
            data[i] = new IconData(listOfStats.get(i).toString(),R.drawable.unused);
        }

        adapter.updateData(data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TexasModuleActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void setPlayersCountTextView(){
        playersCountTextView.setText("players:" + playersCount);
    }

    private <T> T getRandomCard(Collection<T> from) {
        Random rnd = new Random();
        int i = rnd.nextInt(from.size());
        return new ArrayList<T>(Collections.unmodifiableCollection(from)).get(i);
    }

}
