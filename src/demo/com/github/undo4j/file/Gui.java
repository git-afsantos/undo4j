package com.github.undo4j.file;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Gui
 * 
 * @author afs
 * @version 2013
*/

public final class Gui implements UpdateListener {
    // instance variables
    private volatile boolean install = false;
    private boolean signal = false;
    private JFrame frame;
    private JProgressBar progress;
    private JButton startButton;
    private JTextArea text;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class Gui. */
    public Gui() {}


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public synchronized boolean waitForSignal() throws InterruptedException {
        signal = false;
        while (!signal) { wait(); }
        return install;
    }


    /** */
    public void start() {
        SwingUtilities.invokeLater(new Initializer());
    }


    /** */
    @Override
    public void update(final double percent, final String msg) {
        SwingUtilities.invokeLater(new Updater((int) (100 * percent), msg));
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private synchronized void sendSignal(boolean bool) {
        signal = true;
        install = bool;
        notifyAll();
    }


    /** */
    private void initializeFrame() {
        frame = new JFrame("Installer");
        frame.addWindowListener(new CloseSignalListener());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(newContentPane());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** */
    private JComponent newContentPane() {
        JScrollPane scroll = new JScrollPane(text);

        JPanel buttonWrapper = new JPanel(new FlowLayout());
        buttonWrapper.add(startButton);

        JPanel barWrapper = new JPanel(new FlowLayout());
        barWrapper.add(progress);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonWrapper, BorderLayout.PAGE_START);
        panel.add(barWrapper, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.PAGE_END);
        return panel;
    }

    /** */
    private void initializeProgressBar() {
        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
    }

    /** */
    private void initializeButton() {
        startButton = new JButton("Start");
        startButton.addActionListener(new StartAction());
    }

    /** */
    private void initializeTextArea() {
        text = new JTextArea(6, 32);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setMargin(new Insets(0, 5, 0, 5));
    }



    /**************************************************************************
     * Nested Classes
    **************************************************************************/

    /** */
    private final class CloseSignalListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            Gui.this.sendSignal(false);
        }
    }


    /** */
    private final class Updater implements Runnable {
        private final int value;
        private final String message;

        Updater(int val, String msg) {
            value = val;
            message = msg;
        }

        @Override
        public void run() {
            Gui.this.progress.setValue(value);
            Gui.this.text.append("\n");
            Gui.this.text.append(message);
        }
    }


    /** */
    private final class Initializer implements Runnable {
        @Override
        public void run() {
            Gui.this.initializeTextArea();
            Gui.this.initializeButton();
            Gui.this.initializeProgressBar();
            Gui.this.initializeFrame();
        }
    }


    /** */
    private final class StartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Gui.this.startButton.setEnabled(false);
            Gui.this.text.setText("Installation progress:");
            Gui.this.sendSignal(true);
        }
    }
}
