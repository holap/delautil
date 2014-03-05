package delautil.examples;

import delautil.DeclayGridBagLayoutAdapter;
import delautil.DeclayTemplateParser;
import delautil.TemplateMatrix;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mattia on 04/03/14.
 */
public class GridBagLayoutExample extends JPanel {
    JButton[] buttons = new JButton[10];
    String str1 =
            "   { 1} (#)   | {2} (#)                |         |{5} (#) \n" +
            "   { 4} (#)   :                        | {3} (#) |        \n" +
            "   { 9} (#)   :                        |         |        \n" +
            "     ^                                 |         |{8} (#) \n" +
            "   { 6} §200§ | {7} .> <1> ^1^ [RB100] :         | ^      ";

    // <1> weightx ^1^ weighty

    public GridBagLayoutExample(){
        Map<String, JComponent> map = new HashMap<String, JComponent>();

        for(int i=0; i<buttons.length; i++){
            JButton button = new JButton();
            button.setText(String.valueOf(i));
            buttons[i] = button;
            map.put(String.valueOf(i), button);
        }

        DeclayTemplateParser parser = new DeclayTemplateParser();
        TemplateMatrix gridComponents = parser.createComponentMatrix(str1);
        DeclayGridBagLayoutAdapter adapter = new DeclayGridBagLayoutAdapter(gridComponents);
        adapter.addLayoutAndComponents(this, map);
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        GridBagLayoutExample panel = new GridBagLayoutExample();
        frame.setContentPane(panel);
        frame.pack();
        frame.setSize(frame.getWidth() + 100, frame.getHeight() + 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
