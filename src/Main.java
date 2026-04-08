import model.SocialNetwork;
import storage.NetworkFileHandler;
import model.GraphAlgorithms;

import java.util.*;

public class Main {

    // CSV file path which stores data
    private static final String FILE_PATH = "data/networks.csv";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        NetworkFileHandler fh = new NetworkFileHandler(FILE_PATH);

        fh.initialize(); // create file if it does not exist
        System.out.println("SOCIAL GRAPH ANALYZER");

        SocialNetwork curNet = null;
        boolean run = true;

        while (run) {
            if (curNet == null) {
                List<String> nets = fh.getAllNetworks();

                if (nets.isEmpty()) {
                    System.out.println("No networks found.");
                    break;
                }

                System.out.println("\nAvailable Networks:");
                for (int i = 0; i < nets.size(); i++) {
                    System.out.println((i + 1) + ". " + nets.get(i));
                }

                // Main menu to either use existing network, create new one or exit
                System.out.println("\nStartup Menu");
                System.out.println("1. Load an existing network");
                System.out.println("2. Create a new network");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1:
                        System.out.print("Enter the number of the network to load: ");
                        int n = sc.nextInt();
                        sc.nextLine();

                        if (n < 1 || n > nets.size()) {
                            System.out.println("Invalid network number.");
                            break;
                        }

                        String pick = nets.get(n - 1);
                        curNet = loadNet(fh, pick);

                        if (curNet != null) {
                            System.out.println("Network loaded successfully: " + curNet.getNetworkName());
                        } else {
                            System.out.println("Could not load the selected network.");
                        }
                        break;

                    case 2:
                        curNet = createNet(fh);

                        if (curNet != null) {
                            System.out.println("New network created successfully: " + curNet.getNetworkName());
                        }
                        break;

                    case 3:
                        run = false;
                        System.out.println("Exiting Social Graph Analyzer.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }

            } else {
                System.out.println("\nCurrent Network: " + curNet.getNetworkName());

                // Secondary menu if a network is chosen
                System.out.println("\nMain Menu");
                System.out.println("1. View People in Network");
                System.out.println("2. View Adjacency Matrix");
                System.out.println("3. Check Relation Properties");
                System.out.println("4. Compute Transitive Closure");
                System.out.println("5. Show Friendship Circles");
                System.out.println("6. Add New Relation Pair");
                System.out.println("7. Switch Network");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1:
                        System.out.println("\nPeople in Network:");
                        System.out.println(curNet.getPeople());
                        break;

                    case 2:
                        GraphAlgorithms.printMat(
                                curNet.getPeople(),
                                curNet.getAdjacencyMatrix(),
                                "Adjacency Matrix:"
                        );
                        break;

                    case 3:
                        GraphAlgorithms.showRel(curNet);
                        break;

                    case 4:
                        int[][] reach = GraphAlgorithms.closure(curNet.getAdjacencyMatrix());

                        GraphAlgorithms.printMat(
                                curNet.getPeople(),
                                reach,
                                "Transitive Closure:"
                        );
                        break;

                    case 5:
                        List<List<String>> grps = GraphAlgorithms.findGrps(
                                curNet.getPeople(),
                                curNet.getAdjacencyMatrix()
                        );

                        System.out.println("\nFriendship Circles:");

                        if (grps.isEmpty()) {
                            System.out.println("No circles found.");
                            break;
                        }

                        for (int i = 0; i < grps.size(); i++) {
                            System.out.println("Circle " + (i + 1) + ": " + grps.get(i));
                        }
                        break;

                    case 6:
                        addRel(fh, curNet);
                        curNet = loadNet(fh, curNet.getNetworkName());
                        break;

                    case 7:
                        curNet = null;
                        System.out.println("Returning to startup menu...");
                        break;

                    case 8:
                        run = false;
                        System.out.println("Exiting Social Graph Analyzer.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select a number from the menu.");
                }
            }
        }

        sc.close();
    }

    private static SocialNetwork loadNet(NetworkFileHandler fh, String netName) {
        List<String[]> rels = fh.loadRelationsByNetwork(netName);

        if (rels.isEmpty()) {
            return null;
        }

        return GraphAlgorithms.buildNet(netName, rels);
    }

    private static SocialNetwork createNet(NetworkFileHandler fh) {
        System.out.print("Enter new network name: ");
        String netName = sc.nextLine().trim();

        while (netName.isEmpty() || netName.contains(",")) {
            if (netName.isEmpty()) {
                System.out.print("Network name cannot be empty. Enter again: ");
            } else {
                System.out.print("Commas are not allowed. Enter again: ");
            }
            netName = sc.nextLine().trim();
        }

        if (fh.networkExists(netName)) {
            System.out.println("A network with this name already exists.");
            return null;
        }

        System.out.print("Enter the number of relation pairs you want to add: ");
        int tot = sc.nextInt();
        sc.nextLine();

        List<String[]> rels = new ArrayList<>();

        for (int i = 1; i <= tot; i++) {
            System.out.println("Relation Pair " + i + ":");

            System.out.print("Enter first person: ");
            String a = sc.nextLine().trim();
            while (a.isEmpty()) {
                System.out.print("Name cannot be empty. Enter first person again: ");
                a = sc.nextLine().trim();
            }

            System.out.print("Enter second person: ");
            String b = sc.nextLine().trim();
            while (b.isEmpty()) {
                System.out.print("Name cannot be empty. Enter second person again: ");
                b = sc.nextLine().trim();
            }

            rels.add(new String[]{a, b});
        }

        fh.createNetwork(netName, rels);
        return loadNet(fh, netName);
    }

    private static void addRel(NetworkFileHandler fh, SocialNetwork curNet) {
        System.out.println("\nAdd New Relation Pair:");

        System.out.print("Enter first person: ");
        String a = sc.nextLine().trim();
        while (a.isEmpty()) {
            System.out.print("Name cannot be empty. Enter first person again: ");
            a = sc.nextLine().trim();
        }

        System.out.print("Enter second person: ");
        String b = sc.nextLine().trim();
        while (b.isEmpty()) {
            System.out.print("Name cannot be empty. Enter second person again: ");
            b = sc.nextLine().trim();
        }

        if (fh.relationAlreadyExists(curNet.getNetworkName(), a, b)) {
            System.out.println("This relation already exists in the network.");
            return;
        }

        fh.addRelation(curNet.getNetworkName(), a, b);
        System.out.println("Relation added successfully.");
    }
}