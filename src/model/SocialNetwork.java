package model;

import java.util.ArrayList;
import java.util.HashMap;

public class SocialNetwork {
    private String networkName;
    private ArrayList<String> people;
    private HashMap<String, Integer> index;
    private int[][] adjMatrix;

    public SocialNetwork(String name, ArrayList<String> ppl, HashMap<String, Integer> idx, int[][] mat) {
        networkName = name;
        people = ppl;
        index = idx;
        adjMatrix = mat;
    }

    public String getNetworkName() {
        return networkName;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public HashMap<String, Integer> getIndexMap() {
        return index;
    }

    public int[][] getAdjacencyMatrix() {
        return adjMatrix;
    }
}