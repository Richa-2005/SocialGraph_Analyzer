package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    private String networkName;
    private ArrayList<String> people;
    private HashMap<String, Integer> indexMap;
    private int[][] adjacencyMatrix;

    public Graph(String networkName, ArrayList<String> people, HashMap<String, Integer> indexMap, int[][] adjacencyMatrix) {
        this.networkName = networkName;
        this.people = people;
        this.indexMap = indexMap;
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public String getNetworkName() {
        return networkName;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public HashMap<String, Integer> getIndexMap() {
        return indexMap;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }
}