package storage;

import java.io.*;
import java.util.*;

public class NetworkFileHandler {

    private final String filePath;
    private static final String HEADER = "networkName,personA,personB";

    public NetworkFileHandler(String path) {
        filePath = path;
    }

    public void initialize() {
        File f = new File(filePath);

        try {
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!f.exists()) {
                f.createNewFile();

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                    bw.write(HEADER);
                    bw.newLine();

                    // default sample networks
                    bw.write("Default,Alice,Bob");
                    bw.newLine();
                    bw.write("Default,Bob,Charlie");
                    bw.newLine();
                    bw.write("Default,Charlie,Alice");
                    bw.newLine();
                    bw.write("Default,David,Emma");
                    bw.newLine();
                    bw.write("Default,Emma,David");
                    bw.newLine();
                    bw.write("Default,Frank,Frank");
                    bw.newLine();

                    bw.write("Office,John,Emma");
                    bw.newLine();
                    bw.write("Office,Emma,Rahul");
                    bw.newLine();
                    bw.write("Office,Rahul,Priya");
                    bw.newLine();

                    bw.write("Friends,Richa,Aisha");
                    bw.newLine();
                    bw.write("Friends,Aisha,Manan");
                    bw.newLine();
                    bw.write("Friends,Manan,Richa");
                    bw.newLine();
                }
            } else if (f.length() == 0) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                    bw.write(HEADER);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error while creating or initializing file: " + e.getMessage());
        }
    }

    public List<String> getAllNetworks() {
        Set<String> set = new LinkedHashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 3) {
                    continue;
                }

                String net = parts[0].trim();
                if (!net.isEmpty()) {
                    set.add(net);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading networks: " + e.getMessage());
        }

        return new ArrayList<>(set);
    }

    public boolean networkExists(String netName) {
        List<String> nets = getAllNetworks();

        for (String s : nets) {
            if (s.equalsIgnoreCase(netName.trim())) {
                return true;
            }
        }

        return false;
    }

    public List<String[]> loadRelationsByNetwork(String netName) {
        List<String[]> rels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 3) {
                    continue;
                }

                String net = parts[0].trim();
                String a = parts[1].trim();
                String b = parts[2].trim();

                if (net.equalsIgnoreCase(netName.trim())) {
                    rels.add(new String[]{a, b});
                }
            }
        } catch (IOException e) {
            System.out.println("Error while loading relations: " + e.getMessage());
        }

        return rels;
    }

    public void addRelation(String netName, String a, String b) {
        if (netName == null || netName.trim().isEmpty()) {
            System.out.println("Network name cannot be empty.");
            return;
        }

        if (a == null || a.trim().isEmpty() || b == null || b.trim().isEmpty()) {
            System.out.println("Person names cannot be empty.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(netName.trim() + "," + a.trim() + "," + b.trim());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error while adding relation: " + e.getMessage());
        }
    }

    public void createNetwork(String netName, List<String[]> rels) {
        if (netName == null || netName.trim().isEmpty()) {
            System.out.println("Network name cannot be empty.");
            return;
        }

        if (networkExists(netName)) {
            System.out.println("A network with this name already exists.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String[] pair : rels) {
                if (pair.length < 2) {
                    continue;
                }

                String a = pair[0].trim();
                String b = pair[1].trim();

                if (a.isEmpty() || b.isEmpty()) {
                    continue;
                }

                bw.write(netName.trim() + "," + a + "," + b);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while creating network: " + e.getMessage());
        }
    }

    public boolean relationAlreadyExists(String netName, String a, String b) {
        List<String[]> rels = loadRelationsByNetwork(netName);

        for (String[] pair : rels) {
            if (pair.length < 2) {
                continue;
            }

            String x = pair[0].trim();
            String y = pair[1].trim();

            if (x.equalsIgnoreCase(a.trim()) && y.equalsIgnoreCase(b.trim())) {
                return true;
            }
        }

        return false;
    }

    public List<String[]> readAllRows() {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 3) {
                    continue;
                }

                rows.add(new String[]{
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim()
                });
            }
        } catch (IOException e) {
            System.out.println("Error while reading rows: " + e.getMessage());
        }

        return rows;
    }

    public void overwriteAllRows(List<String[]> rows) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(HEADER);
            bw.newLine();

            for (String[] row : rows) {
                if (row.length < 3) {
                    continue;
                }

                bw.write(row[0].trim() + "," + row[1].trim() + "," + row[2].trim());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while overwriting file: " + e.getMessage());
        }
    }
}