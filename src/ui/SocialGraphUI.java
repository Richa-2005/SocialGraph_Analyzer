package ui;

import storage.NetworkFileHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.SocialNetwork;
import model.GraphAlgorithms;


public class SocialGraphUI {

    private JFrame frame;
    private JComboBox<String> networkDropdown;
    private JTextArea outputArea;

    private NetworkFileHandler fileHandler;
    private SocialNetwork currentNetwork;

    private final String FILE_PATH = "data/networks.csv";

    public SocialGraphUI() {
        fileHandler = new NetworkFileHandler(FILE_PATH);
        fileHandler.initialize();

        initializeUI();
        loadNetworks();
    }

    private void initializeUI() {
        frame = new JFrame("Social Graph Analyzer");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // TOP PANEL
        JPanel topPanel = new JPanel();

        networkDropdown = new JComboBox<>();
        JButton loadButton = new JButton("Load Network");
        JButton refreshButton = new JButton("Refresh");

        topPanel.add(new JLabel("Select Network:"));
        topPanel.add(networkDropdown);
        topPanel.add(loadButton);
        topPanel.add(refreshButton);

        // CENTER OUTPUT
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel();

        JButton reportButton = new JButton("Show Full Report");
        JButton addRelationButton = new JButton("Add Relation");

        JButton matrixButton = new JButton("Adjacency Matrix");
        JButton closureButton = new JButton("Transitive Closure");
        JButton newNetworkButton = new JButton("Create New Network");

        bottomPanel.add(matrixButton);
        bottomPanel.add(closureButton);
        bottomPanel.add(newNetworkButton);

        bottomPanel.add(reportButton);
        bottomPanel.add(addRelationButton);



        // ADD TO FRAME
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ACTIONS
        loadButton.addActionListener(e -> loadSelectedNetwork());
        refreshButton.addActionListener(e -> loadNetworks());
        reportButton.addActionListener(e -> showFullReport());
        addRelationButton.addActionListener(e -> addRelation());

        matrixButton.addActionListener(e -> showMatrix());
        closureButton.addActionListener(e -> showClosure());
        newNetworkButton.addActionListener(e -> createNetworkGUI());

        frame.setVisible(true);
    }

    private void loadNetworks() {
        networkDropdown.removeAllItems();
        List<String> networks = fileHandler.getAllNetworks();

        for (String net : networks) {
            networkDropdown.addItem(net);
        }
    }

    private void loadSelectedNetwork() {
        String selected = (String) networkDropdown.getSelectedItem();
        if (selected == null) return;

        List<String[]> relations = fileHandler.loadRelationsByNetwork(selected);
        currentNetwork = GraphAlgorithms.buildNet(selected, relations);

        outputArea.setText("Loaded Network: " + selected + "\n");
    }

    private void showFullReport() {
        if (currentNetwork == null) {
            outputArea.setText("Please load a network first.");
            return;
        }

        int[][] matrix = currentNetwork.getAdjacencyMatrix();

        boolean ref = GraphAlgorithms.isRef(matrix);
        boolean sym = GraphAlgorithms.isSym(matrix);
        boolean trans = GraphAlgorithms.isTrans(matrix);

        int[][] closure = GraphAlgorithms.closure(matrix);
        

        List<List<String>> groups = GraphAlgorithms.findGrps(
                currentNetwork.getPeople(),
                matrix
        );
        outputArea.setText("");
        

        append(" FULL ANALYSIS \n");
        append("Network: " + currentNetwork.getNetworkName() + "\n\n");

        append("People:\n" + currentNetwork.getPeople() + "\n\n");

        append("Relation Properties:\n");
        append("Reflexive: " + (ref ? "Yes" : "No") + "\n");
        append("Symmetric: " + (sym ? "Yes" : "No") + "\n");
        append("Transitive: " + (trans ? "Yes" : "No") + "\n");

        if (ref && sym && trans) {
            append("This relation is an equivalence relation.\n\n");
        } else {
            append("This relation is not an equivalence relation.\n\n");
}

        append("Friendship Circles:\n");

        if (groups.isEmpty()) {
            append("No circles found.\n");
        } else {
            int i = 1;
            for (List<String> g : groups) {
                append("Circle " + i++ + ": " + g + "\n");
            }
        }

    }

    private void addRelation() {
        if (currentNetwork == null) {
            outputArea.setText("Load a network first.");
            return;
        }

        String a = JOptionPane.showInputDialog(frame, "Enter first person:");
        String b = JOptionPane.showInputDialog(frame, "Enter second person:");

        if (a == null || b == null || a.trim().isEmpty() || b.trim().isEmpty()) {
            return;
        }

        if (fileHandler.relationAlreadyExists(currentNetwork.getNetworkName(), a, b)) {
            outputArea.setText("Relation already exists.");
            return;
        }

        fileHandler.addRelation(currentNetwork.getNetworkName(), a, b);

        outputArea.setText("Relation added: " + a + " -> " + b);

        // reload updated network
        loadSelectedNetwork();
    }

    private void append(String text) {
        outputArea.append(text);
    }
        

    
private void showMatrix() {
    if (currentNetwork == null) {
        outputArea.setText("Please load a network first.");
        return;
    }

    StringBuilder sb = new StringBuilder();
    ArrayList<String> people = currentNetwork.getPeople();
    int[][] mat = currentNetwork.getAdjacencyMatrix();

    sb.append("Adjacency Matrix:\n\n");

    sb.append(String.format("%-14s", ""));
    for (String p : people) {
        sb.append(String.format("%-14s", p));
    }
    sb.append("\n");

    for (int i = 0; i < mat.length; i++) {
        sb.append(String.format("%-14s", people.get(i)));
        for (int j = 0; j < mat.length; j++) {
            sb.append(String.format("%-14d", mat[i][j]));
        }
        sb.append("\n");
    }

    outputArea.setText(sb.toString());
}
private void showClosure() {
    if (currentNetwork == null) {
        outputArea.setText("Please load a network first.");
        return;
    }

    StringBuilder sb = new StringBuilder();
    ArrayList<String> people = currentNetwork.getPeople();
    int[][] closureMat = GraphAlgorithms.closure(currentNetwork.getAdjacencyMatrix());

    sb.append("Transitive Closure:\n\n");

    sb.append(String.format("%-14s", ""));
    for (String p : people) {
        sb.append(String.format("%-14s", p));
    }
    sb.append("\n");

    for (int i = 0; i < closureMat.length; i++) {
        sb.append(String.format("%-14s", people.get(i)));
        for (int j = 0; j < closureMat.length; j++) {
            sb.append(String.format("%-14d", closureMat[i][j]));
        }
        sb.append("\n");
    }

    outputArea.setText(sb.toString());
}
private void createNetworkGUI() {
    String netName = JOptionPane.showInputDialog(frame, "Enter new network name:");

    if (netName == null) return;
    netName = netName.trim();

    if (netName.isEmpty() || netName.contains(",")) {
        outputArea.setText("Invalid network name.");
        return;
    }

    if (fileHandler.networkExists(netName)) {
        outputArea.setText("A network with this name already exists.");
        return;
    }

    String countStr = JOptionPane.showInputDialog(frame, "Enter number of relation pairs:");
    if (countStr == null) return;

    int tot;
    try {
        tot = Integer.parseInt(countStr.trim());
    } catch (NumberFormatException e) {
        outputArea.setText("Please enter a valid number.");
        return;
    }

    List<String[]> rels = new ArrayList<>();

    for (int i = 1; i <= tot; i++) {
        String a = JOptionPane.showInputDialog(frame, "Relation Pair " + i + " - Enter first person:");
        if (a == null) return;
        a = a.trim();

        String b = JOptionPane.showInputDialog(frame, "Relation Pair " + i + " - Enter second person:");
        if (b == null) return;
        b = b.trim();

        if (a.isEmpty() || b.isEmpty()) {
            outputArea.setText("Names cannot be empty.");
            return;
        }

        rels.add(new String[]{a, b});
    }

    fileHandler.createNetwork(netName, rels);
    loadNetworks();
    outputArea.setText("New network created: " + netName);
}
}