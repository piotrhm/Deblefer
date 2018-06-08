package com.example.deblefer.Multiplayer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiplayerTable extends AppCompatActivity {

	public static final List<Card> cards = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_table);
		TextView textView = (TextView) findViewById(R.id.textViewTest);
		Random rand = new Random();
		textView.setText("Test: "+String.valueOf(rand.nextInt(1000)));
	}

}