package com.example.deblefer.Multiplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.deblefer.R;

public class MultiplayerGame extends AppCompatActivity {

    public static Activity activity_multiplayer_devices;
    public static int counter;
    Button test;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity_multiplayer_devices = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_game);

        test = (Button) findViewById(R.id.button);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView = (TextView) findViewById(R.id.testCounter);
        textView.setText(String.valueOf(counter++));
    }


}