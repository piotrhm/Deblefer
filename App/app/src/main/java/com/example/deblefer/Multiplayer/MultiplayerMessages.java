package com.example.deblefer.Multiplayer;

import android.app.Application;

public class MultiplayerMessages extends Application {

    public void onCreate() {
        super.onCreate();
    }

    public static BluetoothChatService mChatService;
    public static String inComingMessage;

    public static void clearAll(boolean closeConnection) {
        if (closeConnection && mChatService != null) {
            mChatService.stop();
            mChatService = null;
        }
        inComingMessage = null;

        System.gc();
    }
}