package de.uulm.mhci.circularselection.circularselection.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.uulm.mhci.circularselection.R;

public class ListItemLayout extends LinearLayout {

    private TextView mName;

    public ListItemLayout(Context context) {
        this(context, null);
    }

    public ListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListItemLayout(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // These are defined in the layout file for list items
        // (see next section)
        mName = (TextView) findViewById(R.id.name);
    }
}
