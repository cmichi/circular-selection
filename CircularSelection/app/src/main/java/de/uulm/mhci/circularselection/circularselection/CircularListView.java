package de.uulm.mhci.circularselection.circularselection;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uulm.mhci.circularselection.MainActivity;
import de.uulm.mhci.circularselection.R;
import de.uulm.mhci.circularselection.circularselection.controller.ListController;
import de.uulm.mhci.circularselection.circularselection.controller.RingController;
import de.uulm.mhci.circularselection.circularselection.interfaces.CircularListViewListener;
import de.uulm.mhci.circularselection.circularselection.view.RingView;

public class CircularListView extends RelativeLayout {

    private LinkedHashMap<String, ArrayList<String>> dataset;
    private CircularListViewListener clickListener;

    public enum ChamberSize {FIXED, DYNAMIC}

    public static ChamberSize CHAMBER_SIZE;

    public enum Alphabet {FORWARD, BACKWARD}

    public static Alphabet ALPHABET;

    public enum Interaction {FIXED_RING, JUMP_BACK_RING, MOVABLE_RING}

    public static Interaction INTERACTION;

    private RingView ringView;
    public RingController ringController;

    private ListView listView;

    public CircularListView(Context context, ArrayList<String> list, ChamberSize chamberSize, Interaction interaction, Alphabet alphabet) {
        super(context);

        CHAMBER_SIZE = chamberSize;
        ALPHABET = alphabet;
        INTERACTION = interaction;

        dataset(list);

        ListController listController = new ListController(this, dataset);

        float stepAngle = 360.0f / listController.getModel().getKeyCount();
        ringView = new RingView(getContext(), listController);
        ringView.setStepAngle(stepAngle);
        ringView.setDiscArea(.60f, 1.0f);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        String[] itemvalues = listController
                .getModel()
                .getValues(
                        listController
                                .getModel()
                                .getKeys()
                                .get(listController.getModel().getKeyCount() - 1)
                );

        for (int i = 0; i < itemvalues.length; i++) {
            if (itemvalues[i].length() > 14) itemvalues[i] = itemvalues[i].substring(0, 14) + "…";
        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                R.layout.circularselection_list_item, itemvalues);

        ringController = new RingController(this, getContext(), ringView, listController,
                listController.getModel(), displayMetrics, adapter);
        ringController.setDiscArea(.70f, 1.0f);
        setOnTouchListener(ringController);
        ringView.setRingController(ringController);

        ringView.addOnLayoutChangeListener(ringController);

        addView(ringView, new RelativeLayout.LayoutParams(0, 0) {
            {
                width = MATCH_PARENT;
                height = MATCH_PARENT;
                addRule(RelativeLayout.CENTER_IN_PARENT);
            }
        });

        addView(listView = new ListView(getContext()) {
            {
                setBackgroundColor(0xFF00FF00);
                setDividerHeight(0);
                setScrollbarFadingEnabled(false);

                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setOval(0, 0, getWidth(), getHeight());
                    }
                });

                setClipToOutline(true);

            }
        }, new RelativeLayout.LayoutParams(200, 200) {
            {
                addRule(RelativeLayout.CENTER_IN_PARENT);
            }
        });
        listView.setOnItemClickListener((MainActivity) context);

        /* clip the list view using a mask, this way it will appear as being placed
           in the inner area of the ring. */
        Drawable circle = getResources().getDrawable(R.drawable.circle_mask);
        listView.setBackground(circle);
        listView.setScrollbarFadingEnabled(false);
        listView.setScrollBarFadeDuration(0);
        listView.setPadding(30, 30, 30, 30);
        listView.setScrollContainer(true);

        listView.setClipToOutline(true);
        System.out.println("clip to outline: " + listView.getClipToOutline());

        listView.setAdapter(adapter);

        ringController.setListView(listView);

        listController.setListView(listView);

        ringController.updateViews();
    }

    private void dataset(ArrayList<String> tmp) {
        this.dataset = new LinkedHashMap<String, ArrayList<String>>();
        if (ALPHABET == CircularListView.Alphabet.BACKWARD) {
            /* reverse alphabet */
            Collections.sort(tmp);
            Collections.reverse(tmp);
        } else {
            /* do not reverse alphabet */
            Collections.sort(tmp);
        }

        for (String name : tmp) {
            String keyCharacter = "" + name.charAt(0);

            if (!dataset.containsKey(keyCharacter)) {
                dataset.put(keyCharacter, new ArrayList<String>());
            }

            dataset.get(keyCharacter).add(name);
        }


        for (Map.Entry<String, ArrayList<String>> entry : dataset.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            if (value.size() == 0) {
                dataset.remove(key);
            }
        }
    }

    public void setClickListener(MainActivity clickListener) {
        this.clickListener = clickListener;
    }

    public CircularListViewListener getClickListener() {
        return this.clickListener;
    }

    /**
     * @param pos Position of an item witihin a ListView, placed in the inner side of the ring.
     * @return returns the total position of this item (meaning not only in the currently selected
     * glyph, but rather overall)
     */
    public String getTotalItem(int pos) {
        String currentGlyph = ringController.getCurrentGlyph();
        return ringController.currentListValues[pos];
    }
}