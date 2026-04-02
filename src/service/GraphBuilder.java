package service;

import model.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class GraphBuilder {

    public Graph buildGraph(String networkName, List<String[]> relations) {
        LinkedHashSet<String> uniquePeople = new LinkedHashSet<>();

        for (String[] relation : relations) {
            if (relation.length < 2) {
                continue;
            }

            String personA = relation[0].trim();
            String personB = relation[1].trim();

            if (!personA.isEmpty()) {
                uniquePeople.add(personA);
            }
            if (!personB.isEmpty()) {
                uniquePeople.add(personB);
            }
        }

        ArrayList<String> people = new ArrayList<>(uniquePeople);
        HashMap<String, Integer> indexMap = new HashMap<>();

        for (int i = 0; i < people.size(); i++) {
            indexMap.put(people.get(i), i);
        }

        int n = people.size();
        int[][] adjacencyMatrix = new int[n][n];

        for (String[] relation : relations) {
            if (relation.length < 2) {
                continue;
            }

            String personA = relation[0].trim();
            String personB = relation[1].trim();

            if (personA.isEmpty() || personB.isEmpty()) {
                continue;
            }

            Integer row = indexMap.get(personA);
            Integer col = indexMap.get(personB);

            if (row != null && col != null) {
                adjacencyMatrix[row][col] = 1;
            }
        }

        return new Graph(networkName, people, indexMap, adjacencyMatrix);
    }
}