package de.uulm.mhci.circularselection.circularselection.controller;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.uulm.mhci.circularselection.circularselection.CircularListView;
import de.uulm.mhci.circularselection.circularselection.model.ListModel;

public class ListController {

    private ListModel listModel;
    private RingController ringController;

    private final CircularListView circularListView;
    private ListView listView;

    public ListController(CircularListView circularListView, LinkedHashMap<String, ArrayList<String>> dataset) {
        this.circularListView = circularListView;
        this.listModel = new ListModel(dataset);
    }

    public void setRingController(RingController ringController) {
        this.ringController = ringController;
    }

    public int getSelectedKeyIndex() {
        return ringController.currentGlyphIndex;
    }

    public ListModel getModel() {
        return listModel;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

}
