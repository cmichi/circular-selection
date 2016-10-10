package de.uulm.mhci.circularselection.circularselection.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListModel {

    private LinkedHashMap<String, ArrayList<String>> dataset;

    public ListModel(LinkedHashMap<String, ArrayList<String>> dataset) {
        this.dataset = dataset;
    }

    public ArrayList<String> getKeys() {
        ArrayList<String> list = new ArrayList<String>(dataset.keySet());
        return list;
    }

    public int getKeyCount() {
        return dataset.size();
    }

    public String[] getValues(String key) {
        List<String> list = dataset.get(key);
        String[] arr = new String[list.size()];
        list.toArray(arr);
        return arr;
    }

    public float getAllValuesCount() {
        int count = 0;
        Iterator<Map.Entry<String, ArrayList<String>>> it = dataset.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = it.next();
            count += entry.getValue().size();
        }
        return count;
    }
}
