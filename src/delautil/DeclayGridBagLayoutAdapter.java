package delautil;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Map;

/**
 * Created by mattia on 23/02/14.
 */
public final class DeclayGridBagLayoutAdapter {

    private static final Map<TemplateGridCell.FILL, Integer> mapFillCellToGridBagLayoutValues = new EnumMap<>(TemplateGridCell.FILL.class);
    static {
        mapFillCellToGridBagLayoutValues.put(TemplateGridCell.FILL.BOTH, GridBagConstraints.BOTH);
        mapFillCellToGridBagLayoutValues.put(TemplateGridCell.FILL.VERTICAL, GridBagConstraints.VERTICAL);
        mapFillCellToGridBagLayoutValues.put(TemplateGridCell.FILL.HORIZONTAL, GridBagConstraints.HORIZONTAL);
        mapFillCellToGridBagLayoutValues.put(TemplateGridCell.FILL.NONE, GridBagConstraints.NONE);
    }

    private static final Map<TemplateGridCell.ALIGN, Integer> mapAnchorToGridBagLayoutValues = new EnumMap<>(TemplateGridCell.ALIGN.class);
    static {
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.TOP_LEFT, GridBagConstraints.FIRST_LINE_START);
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.TOP_CENTER, GridBagConstraints.PAGE_START);
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.TOP_RIGHT, GridBagConstraints.FIRST_LINE_END);

        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.MIDDLE_LEFT, GridBagConstraints.LINE_START);
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.MIDDLE_CENTER, GridBagConstraints.CENTER);
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.MIDDLE_RIGHT, GridBagConstraints.LINE_END);

        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.BOTTOM_LEFT, GridBagConstraints.LAST_LINE_START);
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.BOTTOM_CENTER, GridBagConstraints.PAGE_END);
        mapAnchorToGridBagLayoutValues.put(TemplateGridCell.ALIGN.BOTTOM_RIGHT, GridBagConstraints.LAST_LINE_END);
    }

    private int getFillValue(TemplateGridCell.FILL fillValue){
        return mapFillCellToGridBagLayoutValues.get(fillValue);
    }

    private int getAnchorValue(TemplateGridCell.ALIGN anchorValue){
        return mapAnchorToGridBagLayoutValues.get(anchorValue);
    }

    TemplateMatrix templateMatrix;

    public DeclayGridBagLayoutAdapter(TemplateMatrix templateMatrix){
        this.templateMatrix = templateMatrix;
    }

    public void addLayoutAndComponents(JComponent container, Map<String, JComponent> components){
        container.setLayout(new GridBagLayout());

        for (TemplateGridCell cell : templateMatrix){
            JComponent component = components.get(cell.componentKey);

            if (component != null){
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = getFillValue(cell.fill);
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.gridx = cell.column;
                constraints.gridy = cell.row;
                constraints.gridwidth = cell.columnSpan;
                constraints.gridheight = cell.rowSpan;
                constraints.anchor = getAnchorValue(cell.anchor);
                constraints.ipadx = cell.paddingHorizontal;
                constraints.ipady = cell.paddingVertical;
                constraints.insets = cell.internalPadding;
                container.add(component, constraints);
            }
        }
    }




}
