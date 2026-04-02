package service;

import java.util.ArrayList;
import java.util.List;

public class FriendshipCircleFinder {

    public List<List<String>> findCircles(ArrayList<String> people, int[][] matrix) {
        int n = matrix.length;
        boolean[] visited = new boolean[n];
        List<List<String>> circles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<String> currentCircle = new ArrayList<>();
                dfs(i, matrix, visited, people, currentCircle);
                circles.add(currentCircle);
            }
        }

        return circles;
    }

    private void dfs(int current, int[][] matrix, boolean[] visited, ArrayList<String> people, List<String> currentCircle) {
        visited[current] = true;
        currentCircle.add(people.get(current));

        for (int neighbor = 0; neighbor < matrix.length; neighbor++) {
            boolean connected = (matrix[current][neighbor] == 1 || matrix[neighbor][current] == 1);

            if (connected && !visited[neighbor]) {
                dfs(neighbor, matrix, visited, people, currentCircle);
            }
        }
    }
}