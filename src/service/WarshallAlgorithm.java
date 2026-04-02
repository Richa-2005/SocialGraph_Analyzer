package service;

public class WarshallAlgorithm {

    public int[][] computeTransitiveClosure(int[][] matrix) {
        int n = matrix.length;
        int[][] reach = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                reach[i][j] = matrix[i][j];
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (reach[i][j] == 0 && reach[i][k] == 1 && reach[k][j] == 1) {
                        reach[i][j] = 1;
                    }
                }
            }
        }

        return reach;
    }
}