package delautil;

import java.util.Iterator;

/**
 * Created by mattia on 04/03/14.
 */
public class TemplateMatrix implements Iterable<TemplateGridCell> {
    private final TemplateGridCell[][] matrix;

    public static TemplateMatrix create(int rows, int columns){
        return new TemplateMatrix(rows, columns);
    }

    private TemplateMatrix(int rows, int columns){
        matrix = new TemplateGridCell[rows][columns];
    }

    TemplateGridCell getAt(int row, int column){
        return matrix[row][column];
    }

    public Iterator<TemplateGridCell> iterator(){
        return new Iterator<TemplateGridCell>() {
            int row = 0;
            int column = 0;

            @Override
            public boolean hasNext() {
                return row < matrix.length && column < matrix[row].length;
            }

            @Override
            public TemplateGridCell next() {
                TemplateGridCell result = getAt(row, column);
                findNextCoordinates();
                return result;
            }

            private void findNextCoordinates(){
                column++;
                for(; row < matrix.length; row++){
                    for(; column < matrix[row].length; column++){
                        if (getAt(row,column) != null){
                            return;
                        }
                    }
                    column = 0;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    void setComponent(TemplateGridCell templateGridCell, int row, int column){
        matrix[row][column] = templateGridCell;
    }

    TemplateGridCell findUpperGridComponent(int actualRow, int actualColumn){
        TemplateGridCell component = null;
        for (int i=actualRow-1; i>=0 && component == null; i--){
            component = matrix[i][actualColumn];
        }
        return component;
    }
}
