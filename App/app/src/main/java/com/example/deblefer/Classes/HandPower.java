package com.example.deblefer.Classes;

import android.content.Context;
import android.content.res.Resources;

import com.example.deblefer.R;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HandPower {
    private static Collection<Hand> hands = new HashSet<>();
    private static Map<String, Double> powers = null;
    static{
        for (Card card1 : Deck.getUnmodifableDeckAsList())
            for(Card card2 : Deck.getUnmodifableDeckAsList())
                hands.add(new Hand(card1, card2));

    }

    private static void initializePowers(Context context){
        if(powers != null)
            return;
        try {
            Resources res = context.getResources();
            InputStream inStream = res.openRawResource(R.raw.handspower);
            byte[] buffer = new byte[inStream.available()];
            ArrayList<String> s = new ArrayList<>();
            while (inStream.read(buffer) != -1)
                s.add(new String(buffer));
            List<String> list =  Arrays.asList(s.get(0).split("\\s"));
            powers = new HashMap<>();
            for (int i = 0; i < 2*169-1; i+=2) {
                powers.put(list.get(i), Double.parseDouble(list.get(i+1)));
            }
            //return powers.toString();
        } catch (Exception e) {
            throw new InitializationError();
        }
    }

    static class InitializationError extends RuntimeException{ }

    public static Double getHandPower(Card card1, Card card2, Context context){
        if(powers == null)
            initializePowers(context);
        return powers.get(new Hand(card1, card2).toString());
    }

    static public Collection<Hand> getUnmodifableHands(){
        return Collections.unmodifiableCollection(hands);
    }

    static public Map<String, Double> getPowers(){
        return powers;
    }

}
