package com.example.deblefer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.deblefer.Classes.Deck;

import java.util.ArrayList;
import java.util.List;

public class TexasModuleActivity extends AppCompatActivity {

    //List<ImageButton> imageButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texas_module);
        /*imageButtons.add((ImageButton) findViewById(R.id.imageButton0));
        imageButtons.add((ImageButton) findViewById(R.id.imageButton1));
        imageButtons.add((ImageButton)findViewById(R.id.imageButton2));
        imageButtons.add((ImageButton)findViewById(R.id.imageButton3));
        imageButtons.add((ImageButton)findViewById(R.id.imageButton4));
        imageButtons.add((ImageButton)findViewById(R.id.imageButton5));
        imageButtons.add((ImageButton)findViewById(R.id.imageButton6));*//*
        imageButtons.get(0).setOnClickListener(v -> System.out.print("hehe"));*/
        //Deck.initializeCardsImagesIds(this);
        //imageButtons.get(0).setImageResource(Deck.getCardImageId(Deck.getModifableDeck().iterator().next()));
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

}
