package com.bankserver;

import javax.swing.*;

public class View {

    private JFrame frame;

    private javax.swing.JTextArea textField;


    public View(){

        frame = new JFrame();
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textField = new javax.swing.JTextArea();
        textField.setEnabled(false);

        textField.setText("Online Banking is online");

        JPanel panel = new JPanel();

        panel.add(textField);

        frame.add(panel);
        frame.setVisible(true);
    }
}
