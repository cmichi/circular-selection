package de.uulm.mhci.circularselection.circularselection.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.View;

import java.util.HashMap;

import de.uulm.mhci.circularselection.circularselection.CircularListView;
import de.uulm.mhci.circularselection.circularselection.controller.ListController;
import de.uulm.mhci.circularselection.circularselection.controller.RingController;
import de.uulm.mhci.circularselection.circularselection.model.ListModel;

public class RingView extends View {

    private float centerX;
    private float centerY;
    private float minCircle;
    private float maxCircle;
    public float stepAngle;
    private float rotation = 0;

    public float correctionDynamicChambers = 0.0f;

    /* colors (starting from centerX, centerY) */
    public static final int colorInnerCircle = 0xFF222222;
    public static final int colorRing = 0xFF444444;
    public static final int colorChamberSeparator = 0x20000000;
    public static final int colorChamberText = 0x77E7E7E7;
    public static final int colorChamberTextActive = 0xFFEEEEEE;
    public static final int colorChamberTextWouldGetSelected = 0xFFE7E7E7;

    /* the outer circle stroke is very hard to see on the Moto 360 */
    public static final int colorOuterCircleStroke = colorRing;

    private ListModel listModel;
    private ListController listController;
    private RingController ringController;

    public HashMap<String, DynamicChamber> dynamicChambers;

    public RingView(Context context, ListController listController) {
        super(context);
        this.listModel = listController.getModel();
        this.listController = listController;

        if (CircularListView.CHAMBER_SIZE == CircularListView.ChamberSize.DYNAMIC) {
            dynamicChambers = new HashMap<String, DynamicChamber>();
        }

        if (CircularListView.ALPHABET == CircularListView.Alphabet.BACKWARD) {
            if (CircularListView.CHAMBER_SIZE == CircularListView.ChamberSize.DYNAMIC) {
                rotation = 10.0f;
            } else {
                rotation = stepAngle;
            }
        }
    }

    public void setRingController(RingController ringController) {
        this.ringController = ringController;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        centerX = getMeasuredWidth() / 2f;
        centerY = getMeasuredHeight() / 2f;

        super.onLayout(changed, l, t, r, b);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2f;

        Paint paint = new Paint();

        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        LinearGradient linearGradient = new LinearGradient(
                radius, 0, radius, radius, colorRing, colorRing, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawCircle(centerX, centerY, maxCircle * radius, paint);

        paint.setShader(null);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setColor(colorOuterCircleStroke);
        canvas.drawCircle(centerX, centerY, maxCircle * radius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorInnerCircle);
        canvas.drawCircle(centerX, centerY, minCircle * radius, paint);

        paint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect();

        if (CircularListView.CHAMBER_SIZE == CircularListView.ChamberSize.FIXED) {
            canvas.rotate(-rotation, centerX, centerY);
            int glyphCounter = 0;

            /* for each chamber */
            for (int i = 0, n = 360 / (int) stepAngle; i < n; i++) {
                if (glyphCounter < listModel.getKeyCount()) {
                    if (glyphCounter == listController.getSelectedKeyIndex()) {
                        paint.setColor(colorChamberTextActive);
                    } else {
                        paint.setColor(colorChamberText);

                        if (CircularListView.INTERACTION == CircularListView.Interaction.JUMP_BACK_RING) {
                            if (ringController.whichOneWouldGetSelected == glyphCounter) {
                                paint.setColor(colorChamberTextWouldGetSelected);
                            }
                        }
                    }
                    paint.setTextSize(18);

                    double radGlyph = Math.toRadians(((i * stepAngle) + 270 + rotation) % 360);
                    float txtCircle = minCircle + (maxCircle - minCircle) * 4 / 7;

                    int txtX = (int) (centerX + txtCircle * radius * Math.cos(radGlyph));
                    int txtY = (int) (centerY + txtCircle * radius * Math.sin(radGlyph));

                    int glyphCount = listModel.getKeys().size();
                    String glyph = listModel.getKeys().get(glyphCounter);
                    paint.getTextBounds(glyph, 0, glyph.length(), bounds);
                    txtY = txtY + bounds.height() / 2;

                    txtX = txtX - bounds.width() / 2;

                    canvas.drawText(glyph, txtX, txtY, paint);
                    glyphCounter++;
                }
            }

            /* separators */
            for (int i = 0, n = 360 / (int) stepAngle; i < n; i++) {
                double rad = Math.toRadians(((i * stepAngle) + 270 + rotation + stepAngle / 2) % 360);

                int startX = (int) (centerX + minCircle * radius * Math.cos(rad));
                int startY = (int) (centerY + minCircle * radius * Math.sin(rad));
                int stopX = (int) (centerX + maxCircle * radius * Math.cos(rad));
                int stopY = (int) (centerY + maxCircle * radius * Math.sin(rad));

                paint.setColor(colorChamberSeparator);
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        } else if (CircularListView.CHAMBER_SIZE == CircularListView.ChamberSize.DYNAMIC) {
            canvas.rotate(-rotation, centerX, centerY);

            float allChamberSizesUpTilHere = 0.0f;
            float minChamberSize = 7.0f;

            /* how much room is left to fill? */
            float freeRoom = 360.0f - (minChamberSize * listModel.getKeyCount());

            int glyphCounter = 0;
            float chamberSizeUpUntilHere = 0.0f;

            for (int i = 0; i < listModel.getKeyCount(); i++) {
                int valuesForThisKey = listModel.getValues(listModel.getKeys().get(i)).length;
                float chamberSize = minChamberSize + ((freeRoom / listModel.getAllValuesCount()) * valuesForThisKey);
                if (i == 0) correctionDynamicChambers = chamberSize; /// 2.0f;

                // draw the characters
                if (glyphCounter == listController.getSelectedKeyIndex()) {
                    paint.setColor(colorChamberTextActive);
                } else {
                    paint.setColor(colorChamberText);

                    if (CircularListView.INTERACTION == CircularListView.Interaction.JUMP_BACK_RING) {
                        if (ringController.whichOneWouldGetSelected == glyphCounter) {
                            paint.setColor(colorChamberTextWouldGetSelected);
                        }
                    }
                }
                paint.setTextSize(18);

                allChamberSizesUpTilHere += chamberSize;

                if (dynamicChambers.containsKey(listModel.getKeys().get(i)) == false) {
                    dynamicChambers.put(listModel.getKeys().get(i),
                            new DynamicChamber(i,
                                    listModel.getKeys().get(i),
                                    chamberSizeUpUntilHere,
                                    chamberSizeUpUntilHere + chamberSize,
                                    chamberSize));
                }

                double radGlyph = Math.toRadians((allChamberSizesUpTilHere + dynamicChambers.get(listModel.getKeys().get(i)).getGlyphDrawingCorrection(CircularListView.ALPHABET) + 270 + rotation - correctionDynamicChambers) % 360);
                float txtCircle = minCircle + (maxCircle - minCircle) * 4 / 7;
                int txtX = (int) (centerX + txtCircle * radius * Math.cos(radGlyph));
                int txtY = (int) (centerY + txtCircle * radius * Math.sin(radGlyph));
                String glyph = listModel.getKeys().get(glyphCounter);
                paint.getTextBounds(glyph, 0, glyph.length(), bounds);
                txtY = txtY + bounds.height() / 2;
                txtX = txtX - bounds.width() / 2;
                canvas.drawText(glyph, txtX, txtY, paint);

                // draw the separators
                double rad = Math.toRadians((chamberSizeUpUntilHere + 270.0f + rotation + (chamberSize / 2.0f) - correctionDynamicChambers) % 360.0f);

                int startX = (int) (centerX + minCircle * radius * Math.cos(rad));
                int startY = (int) (centerY + minCircle * radius * Math.sin(rad));
                int stopX = (int) (centerX + maxCircle * radius * Math.cos(rad));
                int stopY = (int) (centerY + maxCircle * radius * Math.sin(rad));

                paint.setColor(colorChamberSeparator);
                canvas.drawLine(startX, startY, stopX, stopY, paint);

                chamberSizeUpUntilHere += chamberSize;
                glyphCounter++;
            }
        }

        canvas.restore();
        super.onDraw(canvas);
    }

    /**
     * Define the step angle in degrees for which the
     * dial will call onRotate(int) event.
     *
     * @param angle : angle between each position
     */
    public void setStepAngle(float angle) {
        stepAngle = Math.abs(angle % 360);
    }

    /**
     * Define the draggable disc area with relative circle radius
     * based on min(width, height) dimension (0 = center, 1 = border).
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
     * Set the rotation angle.
     *
     * @param rot : rotation angle as float
     */
    public void setRot(float rot) {
        rotation = rot;
        this.invalidate();
    }

    /**
     * Returns the current rotation angle.
     *
     * @return returns the current rotation angle
     */
    public float getRot() {
        return rotation;
    }
}
