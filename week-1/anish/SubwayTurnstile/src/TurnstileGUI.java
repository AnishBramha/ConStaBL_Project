import javax.swing.*;
import java.awt.*;
import java.io.File;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;

public class TurnstileGUI extends JPanel {

    private SCXMLExecutor scxmlEngine;
    private String currentState = "locked"; 

    public TurnstileGUI() {

        try {

            File xmlFile = new File("turnstile.scxml");
            
            SCXML scxml = SCXMLParser.parse(xmlFile.toURI().toURL(), new SimpleErrorHandler());

            scxmlEngine = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
            scxmlEngine.setStateMachine(scxml);
            scxmlEngine.setRootContext(new JexlContext());
            
            scxmlEngine.go(); 

        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }

    public void fireEventToEngine(String eventName) {

        try {

            TriggerEvent event = new TriggerEvent(eventName, TriggerEvent.SIGNAL_EVENT);
            scxmlEngine.triggerEvent(event);
            
            State activeState = (State) scxmlEngine.getCurrentStatus().getStates().iterator().next();
            currentState = activeState.getId();
            
            repaint(); 

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (currentState.equals("locked")) {

            g2d.setColor(Color.RED);

        } else {

            g2d.setColor(Color.GREEN);
        }

        g2d.fillRoundRect(100, 50, 200, 150, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString(currentState.toUpperCase(), 145, 135);
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("SCXML 0.9 + Java Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        TurnstileGUI turnstilePanel = new TurnstileGUI();
        frame.add(turnstilePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton coinBtn = new JButton("Insert Coin");
        JButton pushBtn = new JButton("Push");

        coinBtn.addActionListener(e -> turnstilePanel.fireEventToEngine("coin"));
        pushBtn.addActionListener(e -> turnstilePanel.fireEventToEngine("push"));

        buttonPanel.add(coinBtn);
        buttonPanel.add(pushBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



