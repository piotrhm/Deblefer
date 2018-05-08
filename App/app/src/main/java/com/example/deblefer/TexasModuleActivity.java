package com.example.deblefer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.deblefer.Classes.Card;
import com.example.deblefer.Classes.Deck;
import com.example.deblefer.Classes.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TexasModuleActivity extends AppCompatActivity {

    private Collection<Card> deck = Deck.getModifableDeckAsSet();
    private List<Card> table = new ArrayList<>();
    private List<Card> hand = new ArrayList<>();

    private Game game = new Game(2);
    private List<ImageView> cardImages = new ArrayList<>();
    private Button addButton = findViewById(R.id.addCardButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texas_module);
        cardImages.add((ImageView) findViewById(R.id.cardImageView0));
        cardImages.add((ImageView) findViewById(R.id.cardImageView1));
        cardImages.add((ImageView)findViewById(R.id.cardImageView2));
        cardImages.add((ImageView)findViewById(R.id.cardImageView3));
        cardImages.add((ImageView)findViewById(R.id.cardImageView4));
        cardImages.add((ImageView)findViewById(R.id.cardImageView5));
        cardImages.add((ImageView)findViewById(R.id.cardImageView6));
        addButton.setOnClickListener((View v) -> {
            Card card = getCardFromDialog();
            if(card == null)
                return;
            setViewActive(cardImages.get(getUsedCardCount()), card);
            deck.remove(card);
            if(getUsedCardCount() < 2)
                hand.add(card);
            else
                table.add(card);
            if(getUsedCardCount() == 2){
                // PREFLOP
            }
            else if(getUsedCardCount() == 5){
                // FLOP
            }
            else if(getUsedCardCount() == 6){
                // TURN
            }
            if(getUsedCardCount() == 7){
                // RIVER
                addButton.setClickable(false);
            }
        });
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

    private Card getCardFromDialog(){
        return new Card(Card.Rank.DEUCE, Card.Suit.HEARTS);
    }

    private void setViewUnused(ImageView cardView){
        cardView.setImageResource(R.drawable.unused);
    }

    private void setViewActive(ImageView cardView, Card card){
        cardView.setImageResource(Deck.getCardImageId(card));
    }

}
