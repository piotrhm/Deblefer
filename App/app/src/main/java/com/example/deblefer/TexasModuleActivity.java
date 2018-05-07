package com.example.deblefer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.deblefer.Classes.Card;
import com.example.deblefer.Classes.Deck;
import com.example.deblefer.Classes.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class TexasModuleActivity extends AppCompatActivity {

    private List<ImageButton> cardButtons = new ArrayList<>();
    private Collection<Card> deck = Deck.getModifableDeckAsSet();
    private List<Card> used = new ArrayList<>();

    private Game game = new Game(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texas_module);
        cardButtons.add((ImageButton) findViewById(R.id.imageButton0));
        cardButtons.add((ImageButton) findViewById(R.id.imageButton1));
        cardButtons.add((ImageButton)findViewById(R.id.imageButton2));
        cardButtons.add((ImageButton)findViewById(R.id.imageButton3));
        cardButtons.add((ImageButton)findViewById(R.id.imageButton4));
        cardButtons.add((ImageButton)findViewById(R.id.imageButton5));
        cardButtons.add((ImageButton)findViewById(R.id.imageButton6));
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
        return null;
    }


}
