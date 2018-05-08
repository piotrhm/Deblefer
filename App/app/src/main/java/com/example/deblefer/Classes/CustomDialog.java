package com.example.deblefer.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog {
    static int count = 0;
    Activity activity;

    String[] currentDeck;
    boolean[] clickedCard;


    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog showDialog(int limit) {
        // Where we track the selected items
        boolean[] clickedCardTmp = new boolean[52];

        List<Card> Items = Deck.getModifableDeckAsList();
        List<String> Tmp = new ArrayList<>();
        for(int i = 0; i < Items.size(); i++)
            Tmp.add(Items.get(i).toString());
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

                                if (count > limit) {
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
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public void getSelectedCard() {

    }
}