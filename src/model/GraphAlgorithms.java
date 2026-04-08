package model;

import model.SocialNetwork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GraphAlgorithms {
    public static SocialNetwork buildNet(String netName, List<String[]> rels) {
        ArrayList<String> ppl = new ArrayList<>();
        HashMap<String, Integer> idx = new HashMap<>();

        for (String[] p : rels) {
            if (p.length < 2) {
                continue;
            }

            String a = p[0].trim();
            String b = p[1].trim();

            if (!a.isEmpty() && !idx.containsKey(a)) {
                idx.put(a, ppl.size());
                ppl.add(a);
            }

            if (!b.isEmpty() && !idx.containsKey(b)) {
                idx.put(b, ppl.size());
                ppl.add(b);
            }
        }

        int n = ppl.size();
        int[][] mat = new int[n][n];

        for (String[] p : rels) {
            if (p.length < 2) {
                continue;
            }

            String a = p[0].trim();
            String b = p[1].trim();

            if (a.isEmpty() || b.isEmpty()) {
                continue;
            }

            Integer r = idx.get(a);
            Integer c = idx.get(b);

            if (r != null && c != null) {
                mat[r][c] = 1;
            }
        }

        return new SocialNetwork(netName, ppl, idx, mat);
    }

    public static void showRel(SocialNetwork curNet) {
        int[][] mat = curNet.getAdjacencyMatrix();

        boolean ref = isRef(mat);
        boolean sym = isSym(mat);
        boolean trans = isTrans(mat);

        System.out.println("\nRelation Properties:");
        System.out.println("Reflexive  : " + (ref ? "Yes" : "No"));
        System.out.println("Symmetric  : " + (sym ? "Yes" : "No"));
        System.out.println("Transitive : " + (trans ? "Yes" : "No"));

        if (ref && sym && trans) {
            System.out.println("This relation is an equivalence relation.");
        } else {
            System.out.println("This relation is not an equivalence relation.");
        }
    }

    public static boolean isRef(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            if (mat[i][i] != 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSym(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j] == 1 && mat[j][i] != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isTrans(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j] == 1) {
                    for (int k = 0; k < mat.length; k++) {
                        if (mat[j][k] == 1 && mat[i][k] != 1) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static int[][] closure(int[][] mat) {
        int n = mat.length;
        int[][] reach = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                reach[i][j] = mat[i][j];
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


    public static List<List<String>> findGrps(ArrayList<String> ppl, int[][] mat) {
        boolean[] vis = new boolean[mat.length];
        List<List<String>> ans = new ArrayList<>();

        for (int i = 0; i < mat.length; i++) {
            if (!vis[i]) {
                List<String> grp = new ArrayList<>();
                dfs(i, mat, vis, ppl, grp);
                ans.add(grp);
            }
        }

        return ans;
    }

    public static void dfs(int cur, int[][] mat, boolean[] vis,
                            ArrayList<String> ppl, List<String> grp) {
        vis[cur] = true;
        grp.add(ppl.get(cur));

        for (int i = 0; i < mat.length; i++) {
            boolean con = mat[cur][i] == 1 || mat[i][cur] == 1;

            if (con && !vis[i]) {
                dfs(i, mat, vis, ppl, grp);
            }
        }
    }

    public static void printMat(ArrayList<String> ppl, int[][] mat, String title) {
        System.out.println();
        System.out.println(title);
        System.out.println();

        if (ppl.isEmpty()) {
            System.out.println("No people found in this network.");
            return;
        }

        System.out.printf("%-12s", "");
        for (String s : ppl) {
            System.out.printf("%-12s", s);
        }
        System.out.println();

        for (int i = 0; i < mat.length; i++) {
            System.out.printf("%-12s", ppl.get(i));
            for (int j = 0; j < mat.length; j++) {
                System.out.printf("%-12d", mat[i][j]);
            }
            System.out.println();
        }
    }



}
