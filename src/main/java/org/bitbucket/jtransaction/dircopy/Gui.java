package org.bitbucket.jtransaction.dircopy;

/**
 * Gui
 * 
 * @author afs
 * @version 2013
*/

public final class Gui implements Runnable, UpdateListener, ActionListener {
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
    public synchronized void sendSignal(boolean bool) {
        signal = true;
        install = bool;
        notifyAll();
    }


    /** */
    public void start() {
        SwingUtilities.invokeLater(this);
    }


    /** */
    @Override
    public void run() {
        initializeTextArea();
        initializeButton();
        initializeProgressBar();
        initializeFrame(newContentPane());
    }


    /** */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startButton.setEnabled(false);
            text.setText("Installation progress:");
            sendSignal(true);
        }
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
    private void initializeFrame(JPanel contents) {
        frame = new JFrame("Installer");
        frame.addWindowListener(new CloseSignalListener());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(contents);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** */
    private JPanel newContentPane() {
        JScrollPane scroll = new JScrollPane(text);
        scroll.setPreferredSize(new Dimension(350, 100));
        scroll.setMaximumSize(new Dimension(350, 100));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(380, 180));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(startButton);
        panel.add(Box.createVerticalGlue());
        panel.add(progress);
        panel.add(Box.createVerticalGlue());
        panel.add(scroll);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    /** */
    private void initializeProgressBar() {
        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setPreferredSize(new Dimension(220, 20));
        progress.setMaximumSize(new Dimension(220, 20));
        progress.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /** */
    private void initializeButton() {
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /** */
    private void initializeTextArea() {
        text = new JTextArea();
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
        /** */
        CloseSignalListener() { super(); }

        /** */
        @Override
        public void windowClosing(WindowEvent e) {
            Gui.this.sendSignal(false);
        }
    }


    /** */
    private final class Updater implements Runnable {
        private final int value;
        private final String message;

        /** */
        Updater(int val, String msg) {
            value = val;
            message = msg;
        }

        /** */
        @Override
        public void run() {
            Gui.this.progress.setValue(value);
            Gui.this.text.append("\n");
            Gui.this.text.append(message);
        }
    }
}
