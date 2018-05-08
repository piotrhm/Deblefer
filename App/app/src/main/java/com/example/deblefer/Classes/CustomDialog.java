package com.example.deblefer.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomDialog {

    public interface onGetCardDialogFinish extends Runnable{
        void addCards(Collection<Card> cards);
    }

    static int count = 0;
    Activity activity;

    String[] currentDeck;
    boolean[] clickedCard;


    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog showDialog(int cardsLimit, Collection<Card> toUse, onGetCardDialogFinish onFinishHandler) {
        // Where we track the selected items
        boolean[] clickedCardTmp = new boolean[52];

        List<Card> Items = new ArrayList<>();
        Items.addAll(toUse);
        Collections.sort(Items);
        List<String> Tmp = new ArrayList<>();
        for(int i = 0; i < Items.size(); i++) {
            Tmp.add(Items.get(i).toString());
        }
        currentDeck = Tmp.toArray(new String[Tmp.size()]);


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set the dialog title
        builder.setTitle("SELECT CARDS")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(currentDeck,
                        clickedCardTmp, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isMarked) {
                                count += isMarked ? 1 : -1;
                                clickedCardTmp[which] = isMarked;

                                if (count > cardsLimit) {
                                    Toast.makeText(activity, "You selected too many.", Toast.LENGTH_SHORT).show();
                                    clickedCardTmp[which] = false;
                                    count--;
                                    ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                                }
                            }
                        })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        clickedCard = clickedCardTmp;
                        count = 0;
                        onFinishHandler.addCards(getSelectedCard());
                        onFinishHandler.run();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public Collection<Card> getSelectedCard() {
        Collection<Card> toReturn = new ArrayList<>();

        List<String>toFind = new ArrayList<>();
        for(int i = 0; i < clickedCard.length; i++) {
            if(clickedCard[i])
                toFind.add(currentDeck[i]);
        }

        List<Card> Items = Deck.getModifableDeckAsList();
        for(int i = 0; i < toFind.size(); i++) {
            for(int j = 0; j < Items.size(); j++) {
                if (Items.get(j).toString().equals(toFind.get(i)))
                    toReturn.add(Items.get(j));
            }
        }

        return toReturn;
    }
}