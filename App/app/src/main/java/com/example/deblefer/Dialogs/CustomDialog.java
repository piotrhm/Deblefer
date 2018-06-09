package com.example.deblefer.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.SimpleAdapter;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class CustomDialog {
    public interface onGetCardDialogFinish extends Runnable {
        void addCards(Collection<Card> cards);
    }

    Activity activity;

    String[] currentDeck;
    boolean[] clickedCard = new boolean[52];


    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog showDialog(Collection<Card> toUse, onGetCardDialogFinish onFinishHandler) {

        // Image and text item data's key.
        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        List<Card> Items = new ArrayList<>();
        Items.addAll(toUse);
        int[] imageIdArr = new int[Items.size()];
        Collections.sort(Items);
        List<String> Tmp = new ArrayList<>();
        List<String> cardName = new ArrayList<>();
        int it = 0;
        for (int i = 0; i < Items.size(); i++) {
            Tmp.add(Items.get(i).toString());
            cardName.add(Items.get(i).toString2());
            imageIdArr[it] = Deck.getCardImageId(Items.get(i));
            it++;
        }
        currentDeck = Tmp.toArray(new String[Tmp.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set the dialog title
        builder.setTitle("SELECT CARDS");

        // Create SimpleAdapter list data.
        List<Map<String, Object>> dialogItemList = new ArrayList<>();
        for (int i = 0; i < currentDeck.length; i++) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put(CUSTOM_ADAPTER_IMAGE, imageIdArr[i]);
            itemMap.put(CUSTOM_ADAPTER_TEXT, cardName.get(i));

            dialogItemList.add(itemMap);
        }

        // Create SimpleAdapter object.
        SimpleAdapter simpleAdapter = new SimpleAdapter(activity, dialogItemList,
                R.layout.activity_alert_dialog_simple_adapter_row,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});

        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemIndex) {
                clickedCard[itemIndex] = true;
                onFinishHandler.addCards(getSelectedCard());
                onFinishHandler.run();
            }
        });

        return builder.create();
    }

    public Collection<Card> getSelectedCard() {
        Collection<Card> toReturn = new ArrayList<>();

        List<String> toFind = new ArrayList<>();
        for (int i = 0; i < clickedCard.length; i++) {
            if (clickedCard[i])
                toFind.add(currentDeck[i]);
        }

        List<Card> Items = Deck.getModifableDeckAsList();
        for (int i = 0; i < toFind.size(); i++) {
            for (int j = 0; j < Items.size(); j++) {
                if (Items.get(j).toString().equals(toFind.get(i)))
                    toReturn.add(Items.get(j));
            }
        }

        return toReturn;
    }
}