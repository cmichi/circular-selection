package de.uulm.mhci.circularselection.circularselection.view;

import de.uulm.mhci.circularselection.circularselection.CircularListView;

public class DynamicChamber {

    public float start;
    public float stop;
    public float size;
    public int glyphId;
    public String glyph;

    public DynamicChamber(int glyphId, String glyph, float start, float stop, float size) {
        this.glyphId = glyphId;
        this.glyph = glyph;
        this.start = start;
        this.stop = stop;
        this.size = size;
    }

    public int getGlyphId() {
        return glyphId;
    }

    public float getStartAngle() {
        return start;
    }

    public float getStopAngle() {
        return stop;
    }

    /**
     * This method returns drawing corrections for the glyph within a button. This is necessary
     * so that the characters are properly centered within each button. A much better, and much more
     * elegant solution would be to properly calculate the positions based on the size of a
     * character and the size of the button (which is how I did it for the fixed-sized buttons).
     * For the dynamic-sized buttons this seems to be a much harder problem and I didn't get it
     * calculated right for all cases. Thus this hacky function.
     * <p/>
     * This function has to be adapted if a new list besides datasetMedium/datasetLarge is used!
     *
     * @param alphabet the direction of the alphabet (FORWARD or BACKWARD)
     * @return returns the drawing correction, so that the character is properly centered within the button
     */
    public float getGlyphDrawingCorrection(CircularListView.Alphabet alphabet) {
        if (alphabet == CircularListView.Alphabet.FORWARD) {
            switch (glyph.charAt(0)) {
                case 'Z':
                    return -1f;
                case 'S':
                    return -2.5f;
                case 'R':
                    return 4f;
                case 'B':
                    return -3f;
                case 'P':
                    return -4f;
                case 'J':
                    return 2f;
                case 'G':
                    return -2f;
                case 'T':
                    return -2f;
                case 'Ä':
                    return 0f;
                case 'A':
                    return 1f;
                case 'L':
                    return 2f;
                case 'Ö':
                    return 2f;
                case 'H':
                    return 1f;
                case 'N':
                    return -2f;
                case 'F':
                    return 2f;
                case 'M':
                    return -1f;
                default:
                    return 0.0f;
            }
        } else if (alphabet == CircularListView.Alphabet.BACKWARD) {
            switch (glyph.charAt(0)) {
                case 'S':
                    return -4f;
                case 'T':
                    return 3f;
                case 'K':
                    return -1.5f;
                case 'C':
                    return 2f;
                case 'Ö':
                    return -0.5f;
                case 'B':
                    return -1.0f;
                case 'H':
                    return 1.5f;
                case 'L':
                    return 1.0f;
                case 'G':
                    return -1.0f;
                case 'U':
                    return 2.0f;
                case 'M':
                    return -2.0f;
                case 'O':
                    return 2.0f;
                case 'N':
                    return 2.0f;
                case 'F':
                    return -1f;
                case 'J':
                    return 0.5f;
                case 'A':
                    return -1.5f;
                case 'P':
                    return -1f;
                case 'R':
                    return 1f;
                case 'Q':
                    return 1f;
                case 'Z':
                    return 1f;
                default:
                    return 0.0f;
            }
        }
        return 0.0f;
    }
}