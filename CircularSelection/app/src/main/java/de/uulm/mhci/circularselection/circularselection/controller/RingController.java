package de.uulm.mhci.circularselection.circularselection.controller;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import de.uulm.mhci.circularselection.R;
import de.uulm.mhci.circularselection.circularselection.CircularListView;
import de.uulm.mhci.circularselection.circularselection.model.ListModel;
import de.uulm.mhci.circularselection.circularselection.view.DynamicChamber;
import de.uulm.mhci.circularselection.circularselection.view.RingView;

public class RingController implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnLayoutChangeListener,
        View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float minCircle;
    private float maxCircle;
    protected RingView ringView;
    public String[] currentListValues;

    private GestureDetectorCompat mDetector;

    public int currentGlyphIndex = 0;
    public String currentGlyph = "";

    private int direction = 0;
    private float distDir = 0;

    private float startAngle;
    private boolean isDragging;

    private ListModel listModel;
    private ListController listController;

    private ListView listView;
    private ArrayAdapter listAdapter;
    private CircularListView circularListView;

    public int whichOneWouldGetSelected = -1;

    public RingController(CircularListView circularListView, Context context, RingView ringView, ListController listController, ListModel listModel, DisplayMetrics displayMetrics, ArrayAdapter listAdapter) {
        this.ringView = ringView;
        this.listModel = listModel;
        this.listController = listController;
        this.listAdapter = listAdapter;
        this.circularListView = circularListView;

        listController.setRingController(this);

        if (circularListView.ALPHABET == CircularListView.Alphabet.BACKWARD) {
            currentGlyphIndex = listController.getModel().getKeyCount() - 1;

            if (CircularListView.ALPHABET == CircularListView.Alphabet.BACKWARD) {
                if (CircularListView.CHAMBER_SIZE == CircularListView.ChamberSize.DYNAMIC) {
                    ringView.setRot(10.0f);
                    ringView.setRotation(10.0f);
                } else {
                    ringView.setRot(ringView.stepAngle);
                    ringView.setRotation(ringView.stepAngle);
                }
            }
        } else {
            currentGlyphIndex = 0;
        }

        currentGlyph = listModel.getKeys().get(currentGlyphIndex);
        mDetector = new GestureDetectorCompat(context, this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        mDetector.onTouchEvent(event);

        if (circularListView.INTERACTION == CircularListView.Interaction.FIXED_RING) {
            currentGlyphIndex = getGlyphAt(Math.abs(360 - touchAngle(touchX, touchY)) % 360);
            if (CircularListView.ALPHABET == CircularListView.Alphabet.BACKWARD) {
                currentGlyphIndex--;
                if (currentGlyphIndex < 0) currentGlyphIndex = listModel.getKeys().size() - 1;
            } else {
            }
            currentGlyph = listModel.getKeys().get(currentGlyphIndex);

            updateListView();

            circularListView.invalidate();
            listView.invalidate();
            ringView.invalidate();
        } else if (circularListView.INTERACTION == CircularListView.Interaction.MOVABLE_RING || circularListView.INTERACTION == CircularListView.Interaction.JUMP_BACK_RING) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    startAngle = touchAngle(touchX, touchY);
                    isDragging = isInRingArea(touchX, touchY);

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDragging && isInRingArea(touchX, touchY)) {
                        float touchAngle = touchAngle(touchX, touchY);
                        float deltaAngle = (360 + touchAngle - startAngle + 180) % 360 - 180;

                        if (direction == 0) {
                            direction = (int) Math.signum(deltaAngle);
                        }

                        ringView.setRotation((ringView.getRotation() + deltaAngle) % 360.0f);
                        ringView.setRot((ringView.getRot() + deltaAngle) % 360.0f);

                        if (circularListView.INTERACTION == CircularListView.Interaction.MOVABLE_RING) {
                            if (getGlyphAt(ringView.getRotation()) != currentGlyphIndex) {
                                float chamberAngle = 360.0f / listModel.getKeyCount();

                                int newCurrentGlyphIndex = getGlyphAt(ringView.getRotation());
                                if (newCurrentGlyphIndex != currentGlyphIndex) {
                                    currentGlyphIndex = newCurrentGlyphIndex;

                                    currentGlyph = listModel.getKeys().get(currentGlyphIndex);
                                    updateListView();
                                }
                            }
                        }

                        if (circularListView.INTERACTION == CircularListView.Interaction.JUMP_BACK_RING) {
                            whichOneWouldGetSelected = getGlyphAt(ringView.getRotation());
                        }

                        startAngle += deltaAngle;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (circularListView.INTERACTION == CircularListView.Interaction.JUMP_BACK_RING) {
                        if (getGlyphAt(ringView.getRotation()) != currentGlyphIndex) {
                            currentGlyphIndex = getGlyphAt(ringView.getRotation());
                            currentGlyph = listModel.getKeys().get(currentGlyphIndex);
                            updateListView();
                        }
                    }

                case MotionEvent.ACTION_CANCEL:
                    isDragging = false;
                    direction = 0;
                    distDir = 0;

                    if (circularListView.INTERACTION == CircularListView.Interaction.JUMP_BACK_RING) {
                        // reset ring to zero
                        ringView.setRot(0);
                        ringView.setRotation(0);
                        ringView.invalidate();

                    }
                    break;
            }
        }
        return true;
    }

    private void updateListView() {
        String[] values = listModel.getValues(currentGlyph);
        Arrays.sort(values);
        currentListValues = values;

        ArrayAdapter adapter = new ArrayAdapter(listAdapter.getContext(),
                R.layout.circularselection_list_item, values);

        listView.setAdapter(adapter);
        listView.smoothScrollToPosition(0);
        listView.invalidate();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        centerX = v.getMeasuredWidth() / 2f;
        centerY = v.getMeasuredHeight() / 2f;
    }

    /**
     * Define the draggable disc area with relative circle radius
     * based on min(width, height) dimension (0 = center, 1 = border)
     *
     * @param radius1 : internal or external circle radius
     * @param radius2 : internal or external circle radius
     */
    public void setDiscArea(float radius1, float radius2) {
        radius1 = Math.max(0, Math.min(1, radius1));
        radius2 = Math.max(0, Math.min(1, radius2));
        minCircle = Math.min(radius1, radius2);
        maxCircle = Math.max(radius1, radius2);
    }

    /**
     * Check if touch event is located in the ring area
     *
     * @param touchX : X position of the finger in this view
     * @param touchY : Y position of the finger in this view
     */
    private boolean isInRingArea(float touchX, float touchY) {
        float innerRadius = 220;

        float dX2 = (float) Math.pow(centerX - touchX, 2);
        float dY2 = (float) Math.pow(centerY - touchY, 2);
        float distToCenter = (float) Math.sqrt(dX2 + dY2);
        float baseDist = Math.min(centerX, centerY);

        if (distToCenter >= 110) return true;
        else return false;
    }

    /**
     * Compute a touch angle in degrees from center
     * North = 0, East = 90, West = -90, South = +/-180
     *
     * @param touchX : X position of the finger in this view
     * @param touchY : Y position of the finger in this view
     * @return angle
     */
    private float touchAngle(float touchX, float touchY) {
        float dX = touchX - centerX;
        float dY = centerY - touchY;
        return (float) (270 - Math.toDegrees(Math.atan2(dY, dX))) % 360 - 180;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        updateViews();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
    }

    /**
     * Updates the view after it was rotated
     */
    public void updateViews() {
        direction = 0;
        distDir = 0;
        ringView.invalidate();
    }


    /**
     * Returns the index of a chamber at a certain position in the ring.
     *
     * @param degree : Which degree is asked?
     */
    public int getGlyphAt(float degree) {
        if (circularListView.CHAMBER_SIZE == CircularListView.ChamberSize.FIXED) {
            float chamberAngle = 360.0f / listModel.getKeyCount();

            // initialDegree is not 0, since we want the glyph orthogonal (at the top)
            float initialDegree = chamberAngle - (chamberAngle / 2.0f);

            degree = degree - initialDegree;
            if (degree < 0.0f) degree = 360.0f + degree;

            float howManyChambers = degree / chamberAngle;
            int rotatedChambers = 0;

            if (howManyChambers < 0f) {
                rotatedChambers = (int) howManyChambers;
            } else {
                rotatedChambers = (int) howManyChambers;
            }

            rotatedChambers = rotatedChambers % listModel.getKeyCount();

            if (rotatedChambers > 0) {  // in the direction of a, b, correctionDynamicChambers, ...
                rotatedChambers++;
                return listModel.getKeyCount() - rotatedChambers;
            } else if (rotatedChambers < 0) {
                rotatedChambers++;
                return Math.abs(rotatedChambers);
            }
            if (rotatedChambers == 0) {
                return listModel.getKeyCount() - 1; // = "a"
            }
        } else if (circularListView.CHAMBER_SIZE == CircularListView.ChamberSize.DYNAMIC) {
            Iterator<Map.Entry<String, DynamicChamber>> it = ringView.dynamicChambers.entrySet().iterator();

            degree = degree - ringView.correctionDynamicChambers;

            if (degree < 0) degree = Math.abs(degree);
            else degree = 360 - degree;

            while (it.hasNext()) {
                Map.Entry<String, DynamicChamber> entry = it.next();
                if (degree >= entry.getValue().getStartAngle() && degree <= entry.getValue().getStopAngle()) {
                    return entry.getValue().getGlyphId();
                }
            }
        }

        // in the case of an error we return the currentGlyphIndex and output the debug to stderr
        Log.e("error", "in getGlyphAt(" + degree + ")");
        return currentGlyphIndex;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
        updateListView();
    }

    public String getCurrentGlyph() {
        return currentGlyph;
    }
}