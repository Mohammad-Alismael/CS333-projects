package com.company.project3.Submit;

import java.util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Graph {
    private static boolean bfs(int[][] rGraph, int s, int t, int[] parent) {
        boolean[] visited = new boolean[rGraph.length];
        Queue<Integer> queue = new LinkedList<Integer>();
        parent[s] = -1;
        visited[s] = true;
        queue.add(s);
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int i = 0; i < rGraph.length; i++) {
                if (rGraph[v][i] > 0 && !visited[i]) {
                    queue.add(i);
                    visited[i] = true;
                    parent[i] = v;
                }
            }
        }
        return (visited[t]);
    }

    static ArrayList<Integer> minCut(int[][] graph, int source, int sink) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        int from, to;

        int[][] rGraph = fillResidualGraph(graph);

        int[] parent = new int[graph.length];

        while (bfs(rGraph, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (to = sink; to != source; to = parent[to]) {
                from = parent[to];
                pathFlow = Math.min(pathFlow, rGraph[from][to]);
            }

            for (to = sink; to != source; to = parent[to]) {
                from = parent[to];
                rGraph[from][to] = rGraph[from][to] - pathFlow;
                rGraph[to][from] = rGraph[to][from] + pathFlow;
            }
        }

        boolean[] isVisited = new boolean[graph.length];
        for (int i = 0; i < graph.length; i++) {
            if (graph[0][i] != 0) isVisited[i] = true;
        }

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (isVisited[i] && !isVisited[j] && graph[i][j] > 0) {
                    res.add(i);
                    res.add(j);
                }
            }
        }

        return res;
    }

    private static int[][] fillResidualGraph(int[][] graph) {
        int[][] rGraph = new int[graph.length][graph.length];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                rGraph[i][j] = graph[i][j];
            }
        }
        return rGraph;
    }
}
 class Mohammad_Alismael_S020240 {
    public static int numberOfProjects;
//    public static int numberOfProjects = 7;
    public static String[] projects;
//    public static String[] projects = new String[]{"A","B","C","D","E","F","G"};
    public static String[] weightsOfProjects;
//    public static String[] weightsOfProjects = new String[]{"10","-8", "2", "4" ,"-5", "3" ,"2"};
    public static String[] projectDependency;
//    public static String[] projectDependency = new String[]{"(A,B)", "(C,A)", "(C,B)", "(C,E)", "(D,B)","(D,F)","(G,C)"};
    public static int[][] grid;
//    public static int[][] grid = new int[numberOfProjects + 2][numberOfProjects + 2];
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        numberOfProjects = scanner.nextInt();
        grid = new int[numberOfProjects + 2][numberOfProjects + 2];
        scanner.nextLine();
        projects = scanner.nextLine().split(" ");
//        System.out.println(Arrays.toString(projects));
        weightsOfProjects = scanner.nextLine().split(" ");
//        System.out.println(Arrays.toString(weightsOfProjects));
        projectDependency = scanner.nextLine().split(" ");
//        System.out.println(Arrays.toString(projectDependency));

        String word;
        do {
            word = scanner.next();
        }while (!word.equals("Decide"));

        Object[] positiveIndexes = getPositiveIndexes(weightsOfProjects);
//        System.out.println(Arrays.toString(positiveIndexes));
        // filling positive
        for (int i = 0; i < positiveIndexes.length; i++) {
            grid[0][(int) positiveIndexes[i]+1] = Integer.parseInt(weightsOfProjects[(int) positiveIndexes[i]]);
        }
        Object[] negativeIndexes = getNegativeIndexes(weightsOfProjects);
//        System.out.println(Arrays.toString(negativeIndexes));
        // filling negative
        for (int i = 0; i < negativeIndexes.length; i++) {
            grid[(int) negativeIndexes[i]+ 1][grid.length - 1] = Integer.parseInt(weightsOfProjects[(int) negativeIndexes[i]]);
        }

        // dependency
        for (int i = 0; i < projectDependency.length; i++) {
            String sub = projectDependency[i];
            char[] output = sub.toCharArray();
            grid[indexOf(projects,Character.toString(output[1])) + 1][indexOf(projects,Character.toString(output[3])) + 1] = Integer.MAX_VALUE;
        }

        ArrayList res = Graph.minCut(grid, 0, grid.length - 1);;
        Set<String> set = new HashSet<>(res);
        res.clear();
        res.addAll(set);
        StringBuilder str = new StringBuilder();
        int total = 0;
        for (Object num: res) {
            int x = Integer.parseInt(num.toString());

            if (x >= 1 && x <= 7){
                str.append(projects[x - 1]).append(" ");
                total += indexOf(weightsOfProjects,projects[x- 1]);
            }
        }
        System.out.println("Venture projects: " + str);
        System.out.println("Maximum profit: " + total);

    }

    public void printGrid(int[][] grid){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print( grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static Object[] getPositiveIndexes(String[] projects) {
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < projects.length; i++) {
            if (Integer.parseInt(projects[i]) > 0 ) a.add(i);
        }
        return a.toArray();
    }

    private static Object[] getNegativeIndexes(String[] projects) {
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < projects.length; i++) {
            if (Integer.parseInt(projects[i]) < 0 ) a.add(i);
        }
        return a.toArray();
    }

    public static int indexOf(String[] container, String item) {
        for (int i = 0; i < container.length; i++) {
            if (container[i].equals(item)) return i;
        }
        return -1;
    }
}
