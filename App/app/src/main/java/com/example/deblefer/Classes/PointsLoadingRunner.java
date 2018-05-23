package com.example.deblefer.Classes;

import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class PointsLoadingRunner implements Runnable {
    private TextView textView;
    private String[] text = new String[3];
    {
        text[0] = ":";
        text[1] = "::";
        text[2] = ":::";
    }

    public PointsLoadingRunner(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (true) {
                if(i == 999999)
                    i = 0;
                int finalI = i%3;
                Log.println(Log.ASSERT, "XD", "pointed!");
                textView.post(() -> textView.setText(text[finalI]));
                TimeUnit.MILLISECONDS.sleep(1000);
                i++;
            }
        } catch (Exception e) {
            textView.post(() -> textView.setText(""));
        }
    }
}
