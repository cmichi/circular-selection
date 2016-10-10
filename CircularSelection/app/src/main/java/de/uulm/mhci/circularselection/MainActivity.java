package de.uulm.mhci.circularselection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.uulm.mhci.circularselection.circularselection.CircularListView;
import de.uulm.mhci.circularselection.circularselection.CircularListView.Alphabet;
import de.uulm.mhci.circularselection.circularselection.CircularListView.ChamberSize;
import de.uulm.mhci.circularselection.circularselection.CircularListView.Interaction;
import de.uulm.mhci.circularselection.circularselection.interfaces.CircularListViewListener;

public class MainActivity extends Activity
        implements CircularListViewListener, AdapterView.OnItemClickListener {

    private CircularListView circularListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Alphabet alphabetDirection;
        alphabetDirection = Alphabet.BACKWARD;
        alphabetDirection = Alphabet.FORWARD;

        Interaction interactionTechnique;
        interactionTechnique = Interaction.JUMP_BACK_RING;
        interactionTechnique = Interaction.FIXED_RING;
        interactionTechnique = Interaction.MOVABLE_RING;

        ChamberSize chamberSize;
        chamberSize = ChamberSize.DYNAMIC;
        chamberSize = ChamberSize.FIXED;

        ArrayList<String> items = new ArrayList<String>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("list.txt"));
            BufferedReader reader = new BufferedReader(is);
            String line;

            /* first line will be skipped */
            while ((line = reader.readLine()) != null) {
                items.add(line);
            }
        } catch (Exception e) {
            Log.e("Exception!", e.toString());
            System.exit(1);
        }

        circularListView = new CircularListView(this, items, chamberSize,
                interactionTechnique, alphabetDirection);
        circularListView.setClickListener(this);
        setContentView(circularListView);
    }

    public void onClick(String s) {
        Log.i("Circular Selection", "clicked! " + s);
    }

    @Override
    protected void onResume() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String totalItem = circularListView.getTotalItem(position);
        Log.i("Circular Selection", "click at position: " + position + ", item: " + totalItem);
    }
}