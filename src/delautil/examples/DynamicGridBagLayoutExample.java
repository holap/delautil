package delautil.examples;

import delautil.DeclayGridBagLayoutAdapter;
import delautil.DeclayTemplateParser;
import delautil.TemplateMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by mattia on 04/03/14.
 */
public class DynamicGridBagLayoutExample extends JPanel {

    DeclayTemplateParser parserResults = new DeclayTemplateParser();
    HashMap<String, JComponent> mapResults = createMap();
    JPanel panelResults = new JPanel();
    JTextArea textArea = new JTextArea();
    JButton btnRefresh = new JButton("Refresh");


    String str1 =
            "   {btn1} (#)   | {btn2} \n" +
            "   {lbl1} (#)   :        \n" +
            "   {txt1} (#)   |        \n" +
            "     ^          | {txt2} ";

    // <1> weightx ^1^ weighty

    String mainLayout =
            "{textArea} (#) | {refreshBtn} >< \n" +
            "{results}  (#) :                 ";

    public DynamicGridBagLayoutExample(){
        refreshPanelResult(str1);

        textArea.setEditable(true);
        textArea.setText(str1);
        textArea.setSize(300,300);
        Font font = new Font("Monospaced", Font.PLAIN, 14);
        textArea.setFont(font);

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                refreshPanelResult(textArea.getText());
            }
        });

        HashMap<String, JComponent> mapMain = new HashMap<>();
        mapMain.put("textArea", textArea);
        mapMain.put("refreshBtn", btnRefresh);
        mapMain.put("results", panelResults);

        DeclayTemplateParser parserMain = new DeclayTemplateParser();
        TemplateMatrix matrixMain = parserMain.createComponentMatrix(mainLayout);
        DeclayGridBagLayoutAdapter adapter = new DeclayGridBagLayoutAdapter(matrixMain);
        adapter.addLayoutAndComponents(this, mapMain);
    }

    private void refreshPanelResult(String template){
        panelResults.removeAll();
        TemplateMatrix matrixResults = parserResults.createComponentMatrix(template);
        DeclayGridBagLayoutAdapter adapterResults = new DeclayGridBagLayoutAdapter(matrixResults);
        adapterResults.addLayoutAndComponents(panelResults, mapResults);
        panelResults.revalidate();
    }

    private HashMap<String, JComponent> createMap(){
        HashMap<String, JComponent> map = new HashMap<String, JComponent>();
        for(int i=0; i<10; i++){
            JButton button = new JButton();
            button.setText(String.valueOf(i));
            map.put("btn" + String.valueOf(i), button);
        }

        for(int i=0; i<10; i++){
            JLabel label = new JLabel();
            label.setText(String.valueOf(i));
            map.put("lbl" + String.valueOf(i), label);
        }

        for(int i=0; i<10; i++){
            JTextField textField = new JTextField();
            textField.setText(String.valueOf(i));
            textField.setEditable(false);
            map.put("txt" + String.valueOf(i), textField);
        }
        return map;
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        DynamicGridBagLayoutExample panel = new DynamicGridBagLayoutExample();
        frame.setContentPane(panel);
        frame.pack();
        frame.setSize(frame.getWidth() + 100, frame.getHeight() + 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
