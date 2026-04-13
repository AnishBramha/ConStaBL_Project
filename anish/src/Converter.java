import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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


public class Converter extends JFrame {

    private SCXMLExecutor scxmlEngine;
    private JLabel stateDisplayLabel;
    private JPanel buttonPanel;

    public Converter(File xmlFile) {

        setTitle("SCXML Tester: " + xmlFile.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        setupUI();

        try {

            initEngine(xmlFile);
            buildDynamicButtons(xmlFile);
            updateStateDisplay();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Error loading SCXML:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void setupUI() {

        stateDisplayLabel = new JLabel("Loading states...", SwingConstants.CENTER);
        stateDisplayLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        stateDisplayLabel.setOpaque(true);
        stateDisplayLabel.setBackground(new Color(40, 44, 52));
        stateDisplayLabel.setForeground(new Color(152, 195, 121));
        add(stateDisplayLabel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.DARK_GRAY);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void initEngine(File xmlFile) throws Exception {

        SCXML scxml = SCXMLParser.parse(xmlFile.toURI().toURL(), new SimpleErrorHandler());
        scxmlEngine = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
        scxmlEngine.setStateMachine(scxml);
        scxmlEngine.setRootContext(new JexlContext());
        scxmlEngine.go();
    }


    private void buildDynamicButtons(File xmlFile) throws Exception {

        Set<String> uniqueEvents = new HashSet<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().parse(xmlFile);
        
        NodeList transitions = doc.getElementsByTagNameNS("*", "transition");

        for (int i = 0; i < transitions.getLength(); i++) {

            Element transition = (Element) transitions.item(i);

            if (transition.hasAttribute("event")) {

                uniqueEvents.add(transition.getAttribute("event"));
            }
        }

        for (String eventName : uniqueEvents) {

            JButton btn = new JButton("Fire: " + eventName);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.addActionListener(e -> fireEvent(eventName));
            buttonPanel.add(btn);
        }
    }


    private void fireEvent(String eventName) {

        try {

            TriggerEvent event = new TriggerEvent(eventName, TriggerEvent.SIGNAL_EVENT);
            scxmlEngine.triggerEvent(event);
            updateStateDisplay();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void updateStateDisplay() {

        var currentStates = scxmlEngine.getCurrentStatus().getStates();
        
        StringBuilder stateNames = new StringBuilder("<html><center>Active States:<br/>");

        for (Object obj : currentStates) {

            State state = (State) obj;
            stateNames.append("<font color='white'>[ ").append(state.getId()).append(" ]</font><br/>");
        }
        stateNames.append("</center></html>");

        stateDisplayLabel.setText(stateNames.toString());
    }


    public static void main(String[] args) {

        try { 

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {}

        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setDialogTitle("Select an SCXML file to test");
        
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fileChooser.getSelectedFile();
            new Converter(selectedFile);

        } else {

            System.err.println("No file selected. Exiting.");
            System.exit(EXIT_ON_CLOSE);
        }
    }
}





