package com.company.project4;

// A Java program to print all subsets of a set
import java.util.ArrayList;
import java.util.Scanner;

class Mohammad_Alismael_S020240
{
    // Time complexity: O(n * (2^n)) as the outer loop runs for O(2^n) and the inner loop runs for O(n).
    public static ArrayList<ArrayList<Integer>> generateSubsets(int[] set) {
        int n = set.length;
        ArrayList<ArrayList<Integer>> sets = new ArrayList<>();
        for (int i = 0; i < Math.pow(2,n); i++) {
            ArrayList<Integer> set_ = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    set_.add(set[j]);
                }
            }
            sets.add(set_);
        }
        return sets;
    }

//    public static int nItems = 4;
    public static int nItems;
//    public static String[] weights = {"1","2","3","5"};
    public static String[] weights;
//    public static String[] correspondingProfits = {"10","40","20","60"};
    public static String[] correspondingProfits;
    public static int maxWeight;
    public static int minProfit;
    public static int[][] table;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        nItems = scanner.nextInt();
        table = new int[nItems][3];
        scanner.nextLine();
        weights = scanner.nextLine().split(" ");
        correspondingProfits = scanner.nextLine().split(" ");
        maxWeight = scanner.nextInt();
        minProfit = scanner.nextInt();
        // fill table with corresponding values
        for (int i = 0; i < nItems; i++) {
            table[i][0] = i;
            table[i][1] = Integer.parseInt(weights[i]);
            table[i][2] = Integer.parseInt(correspondingProfits[i]);
        }

        int[] set = createSet(table);
        ArrayList<ArrayList<Integer>> sets = generateSubsets(set);
        ArrayList<Integer> totalWeight = new ArrayList<Integer>();
        ArrayList<Integer> totalValue = new ArrayList<Integer>();
        for(ArrayList subset : sets){
            int weight = 0;
            int profit = 0;
            for(Object itemSubSet: subset){
                weight += table[(int) itemSubSet][1];
                profit += table[(int) itemSubSet][2];
            }
            if ( profit >= minProfit && weight <= maxWeight) {
                totalWeight.add(weight);
                totalValue.add(profit);
            }
        }
//            System.out.println("profit: " + profit);
//            System.out.println("weight: " + weight);
//            System.out.println("condition " + (profit > minProfit && weight < maxWeight));
//        System.out.println("totalWeight: " + totalWeight);
//        System.out.println("totalValue: " + totalValue);
//        System.out.println("doesItContainMaxWeight : " + doesItContainMaxWeight);
//        System.out.println("doesItContainMinProfit : " + doesItContainMinProfit);


        boolean doesItContainMaxWeight = totalWeight.contains(maxWeight);
        boolean doesItContainMinProfit = totalValue.contains(minProfit);

        if (doesItContainMaxWeight && doesItContainMinProfit)
            System.out.println("Yes");
        else
            System.out.println("No");


    }

    public static int[] createSet(int[][] table){
        int[] set = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            set[i] = table[i][0];
        }
        return set;
    }
}
