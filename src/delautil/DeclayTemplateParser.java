package delautil;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mattia on 28/02/14.
 */
public class DeclayTemplateParser {

    private Map<TemplateGridCell.FILL, String> mapFillStringSyntax = new EnumMap<>(TemplateGridCell.FILL.class);
    {
        mapFillStringSyntax.put(TemplateGridCell.FILL.BOTH, "(#)");
        mapFillStringSyntax.put(TemplateGridCell.FILL.VERTICAL, "(§)");
        mapFillStringSyntax.put(TemplateGridCell.FILL.HORIZONTAL, "(=)");
        mapFillStringSyntax.put(TemplateGridCell.FILL.NONE, "()");
    }

    private Map<TemplateGridCell.ALIGN, String> mapAnchorSyntax = new EnumMap<>(TemplateGridCell.ALIGN.class);
    {
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.TOP_LEFT, "<°");
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.TOP_CENTER, "°°");
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.TOP_RIGHT, "°>");

        mapAnchorSyntax.put(TemplateGridCell.ALIGN.MIDDLE_LEFT, "<-");
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.MIDDLE_CENTER, "><");
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.MIDDLE_RIGHT, "->");

        mapAnchorSyntax.put(TemplateGridCell.ALIGN.BOTTOM_LEFT, "<.");
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.BOTTOM_CENTER, "..");
        mapAnchorSyntax.put(TemplateGridCell.ALIGN.BOTTOM_RIGHT, ".>");
    }

    public final TemplateGridCell.ALIGN defaultAnchor = TemplateGridCell.ALIGN.MIDDLE_CENTER;
    public final TemplateGridCell.FILL defaultFill = TemplateGridCell.FILL.NONE;
    public final Insets defaultInsets = new Insets(0,0,0,0);

//    private final Map<String, JComponent> mapOfComponents;
    TemplateMatrix componentMatrix;

//    public DeclayTemplateParser(Map<String, JComponent> mapOfComponents){
//        this.mapOfComponents = mapOfComponents;
//    }

    public TemplateMatrix createComponentMatrix(String layoutDefinition){

        Map<Integer, List<String>> rowsDefinitions = createRowsDefinitions(layoutDefinition);
        int columnNumber = calculateColumnNumber(rowsDefinitions);
        int rowsNumber =  rowsDefinitions.size();
        componentMatrix = TemplateMatrix.create(rowsNumber, columnNumber);

        for (Map.Entry<Integer, List<String>> entry : rowsDefinitions.entrySet()){
            int row = entry.getKey();
            List<String> columns = entry.getValue();
            addComponentsToMatrix(columns, row);
        }

        return componentMatrix;
    }

    private void addComponentsToMatrix(List<String> columns, int row){
        int column = 0;
        for (String colFormatString : columns){

            String colComponentKey = extractComponentKey(colFormatString);
            int columnSpan = colFormatString.split(":").length;

            if (colComponentKey != null){
//                JComponent component = mapOfComponents.get(colComponentKey);
//                if (component != null){
                  TemplateGridCell matrixEntry = createGridComponent(row, column, columnSpan, colFormatString, colComponentKey);
                  componentMatrix.setComponent(matrixEntry, row, column);
//                }
            }
            else if (colFormatString.contains(" ^ ")){
                TemplateGridCell upper = componentMatrix.findUpperGridComponent(row, column);
                if (upper != null){
                    upper.rowSpan++;
                    columnSpan = upper.columnSpan;
                }
            }

            column += columnSpan;
        }

    }

    private TemplateGridCell createGridComponent(int row, int column, int columnSpan, String definition, String componentKey){
        TemplateGridCell templateGridCell = new TemplateGridCell();
        templateGridCell.row = row;
        templateGridCell.column = column;
        templateGridCell.componentKey = componentKey;
        templateGridCell.columnSpan = columnSpan;
        templateGridCell.fill = extractComponentFill(definition);
        templateGridCell.anchor = extractComponentAnchor(definition);
        templateGridCell.paddingHorizontal = extractPaddingExternalHorizontal(definition);
        templateGridCell.paddingVertical = extractPaddingExternalVertical(definition);
        templateGridCell.internalPadding = extractPaddingInternal(definition);
        return templateGridCell;
    }

    private Map<Integer, List<String>> createRowsDefinitions(String layoutDefinition){
        Map<Integer, List<String>> matrix = new TreeMap<Integer, List<String>>();

        String rows[] = layoutDefinition.split("\n");

        int currentRow = 0;
        for (String strRos : rows){
            if (!strRos.isEmpty() && !strRos.startsWith("@")){
                String[] columns = strRos.split("\\|");
                List<String> listOfColumns = Arrays.asList(columns);
                matrix.put(currentRow, listOfColumns);
                currentRow++;
            }
        }

        return matrix;
    }

    private int calculateColumnNumber(Map<Integer, List<String>> matrix){
        int columnNumber = 0;
        for(List<String> columns : matrix.values()){
            int colsOfCurrentRow = 0;
            for(String colDef : columns){
                colsOfCurrentRow += colDef.split(":").length;
            }
            if (columnNumber < colsOfCurrentRow)
                columnNumber = colsOfCurrentRow;
        }

        return  columnNumber;
    }

    public String extractComponentKey(String columnDefinition){
        String regex =  ".*\\{\\s*(.+?)\\s*\\}.*";
        return extractRegExGroupOrNull(columnDefinition, regex);
    }

    public TemplateGridCell.FILL extractComponentFill(String columnDefinition){

        for(TemplateGridCell.FILL value : mapFillStringSyntax.keySet()) {
            String code = mapFillStringSyntax.get(value);
            if (columnDefinition.contains(code)) {
                return value;
            }
        }

        return defaultFill;
    }

    public TemplateGridCell.ALIGN extractComponentAnchor(String columnDefinition){

        for(TemplateGridCell.ALIGN align : mapAnchorSyntax.keySet()) {
            String code = mapAnchorSyntax.get(align);
            if (columnDefinition.contains(code)) {
                return align;
            }
        }

        return defaultAnchor;
    }

    public int extractPaddingExternalHorizontal(String columnDefinition){
        String regex = ".*\\+(\\d+)\\+.*";
        String result = extractRegExGroupOrNull(columnDefinition, regex);
        if (result == null){
            return 0;
        }
        return Integer.valueOf(result);
    }

    public int extractPaddingExternalVertical(String columnDefinition){
        String regex = ".*§(\\d+)§.*";
        String result = extractRegExGroupOrNull(columnDefinition, regex);
        if (result == null){
            return 0;
        }
        return Integer.valueOf(result);
    }

    public String extractRegExGroupOrNull(String source, String regex){
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(source);
        matcher.find();
        try
        {
            return matcher.group(1);
        }
        catch (Exception ex){
            return null;
        }
    }

    protected Insets extractPaddingInternal(String columnDefinition){
        // Following regex matches all valid input but is not able to discard
        // every invalid input
        // Examples of valid input (separated by ;):
        // [100];[R100];[100,200];[100,200,100,200];[LR200];[LR100,TB200]
        String regex = ".*\\[((([T,L,B,R]{0,4}\\d+),{0,1}){1,4})\\].*";
        String paddingDefinition = extractRegExGroupOrNull(columnDefinition, regex);

        if (paddingDefinition == null){
            return defaultInsets;
        }

        Insets insets = extractPaddingInternalByNamedPropertyOrNull(paddingDefinition);

        if (insets == null){
            insets = extractPaddingInternalByPositionalPropertyOrDefault(paddingDefinition);
        }

        return insets;
    }

    Insets extractPaddingInternalByNamedPropertyOrNull(String paddingDefinition){
        if (!paddingDefinition.matches("(([T,L,B,R]{1,4}\\d+),{0,1}){1,4}"))
            return null;

        int top = 0;
        int left = 0;
        int bottom = 0;
        int right = 0;

        String regexToExtractSizeValue = "[R,L,B,T]{1,4}(\\d+)";
        String regexToExtractSizeName = "([R,L,B,T]{1,4})\\d+";

        String[] paddingArray = paddingDefinition.split(",");

        for(String sideDefinition : paddingArray){
            String sizeString = extractRegExGroupOrNull(sideDefinition, regexToExtractSizeValue);
            int size = Integer.valueOf(sizeString);
            String side = extractRegExGroupOrNull(sideDefinition, regexToExtractSizeName);

            if (side.contains("R")){
                right = size;
            }

            if (side.contains("L")){
                left = size;
            }

            if (side.contains("T")){
                top = size;
            }

            if (side.contains("B")){
                bottom = size;
            }
        }

        return new Insets(top, left, bottom, right);
    }

    Insets extractPaddingInternalByPositionalPropertyOrDefault(String paddingDefinition){
        int top = 0;
        int left = 0;
        int bottom = 0;
        int right = 0;

        String[] paddingArray = paddingDefinition.split(",");

        if (paddingArray.length == 1) {
            // all sides with same value
            int size = Integer.valueOf(paddingArray[0]);
            top = size;
            left = size;
            bottom = size;
            right = size;
        }
        else if (paddingArray.length == 2) {
            // all sides with same value
            int verticalSize = Integer.valueOf(paddingArray[0]);
            int horizontalSize = Integer.valueOf(paddingArray[1]);
            top = verticalSize;
            bottom = verticalSize;
            left = horizontalSize;
            right = horizontalSize;
        }
        else if (paddingArray.length == 4){
            top = Integer.valueOf(paddingArray[0]);
            left = Integer.valueOf(paddingArray[1]);
            bottom = Integer.valueOf(paddingArray[2]);
            right = Integer.valueOf(paddingArray[3]);
        }

        return new Insets(top, left, bottom, right);
    }
}
