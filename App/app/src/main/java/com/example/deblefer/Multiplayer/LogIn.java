package com.example.deblefer.Multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.deblefer.R;

public class LogIn extends AppCompatActivity {

	static String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_login);
	}


	public void startGame(View v) {
		try {
			DBhandler dbh = new DBhandler(this, null, null, 1);
			name = ((EditText) findViewById(R.id.nameBox)).getText().toString();
			if (name.equals("") ) {
				Toast t = Toast.makeText(getApplicationContext(), "Insert your name", Toast.LENGTH_LONG);
				t.show();
			}
			else {
				UserData userData = new UserData(name,0, 0);
				dbh.insert(userData);
				startActivity(new Intent(LogIn.this, BluetoothChat.class));
			}
		} catch (Exception e) {
			System.out.print(e);
		}


	}


}
