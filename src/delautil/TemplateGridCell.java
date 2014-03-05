package delautil;

import java.awt.*;

/**
 * Created by mattia on 28/02/14.
 */
public class TemplateGridCell {

    public enum FILL {
        BOTH,
        VERTICAL,
        HORIZONTAL,
        NONE;
    }

    public enum ALIGN {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,

        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,

        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT;
    }
    protected String componentKey;
    protected int column = 0;
    protected int row = 0;
    protected int columnSpan = 1;
    protected int rowSpan = 1;
    protected FILL fill;
    protected ALIGN anchor;
    protected int paddingHorizontal;
    protected int paddingVertical;
    protected Insets internalPadding;

    @Override
    public String toString() {
        return "Row:" + row + " Col: " + column + "  " + componentKey;
    }
}
