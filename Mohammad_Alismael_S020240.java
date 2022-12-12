package com.company.project2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Mohammad_Alismael_S020240 {
    public static String[] outlet;
    public static String[] lamps;
    public static int[][] dp;
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.print("enter number of lamps: ");
        int numberOfLamps = input.nextInt();
        outlet = new String[numberOfLamps + 1];
        lamps = new String[numberOfLamps + 1];
        outlet[0] = "";
        lamps[0] = "";
        dp = new int[numberOfLamps + 1][numberOfLamps + 1];

        input.nextLine();
        getLine(input, numberOfLamps, "enter outlets: ", outlet);
        getLine(input, numberOfLamps, "enter lamps: ", lamps);
        input.close();

        for (int i = 1; i < outlet.length; i++) { // Y
            for (int j = 1; j < lamps.length; j++) { // X
                dp[i][j] = fillDP(j,i);// x y
            }
        }

        System.out.println(String.format("the answer is : %d",dp[outlet.length - 1][lamps.length - 1]));
        System.out.println(findLamps());

    }

    private static void getLine(Scanner input,
                                int numberOfLamps,
                                String s,
                                String[] strArray) {
        System.out.print(s);
        String outlets = input.nextLine();
        String[] tmp = outlets.split(" ");
        for (int i = 0; i < numberOfLamps; i++) {
            strArray[i + 1] = tmp[i].toUpperCase();
        }
    }

    private static String findLamps() {
        int x = outlet.length - 1;
        int y = lamps.length - 1;
        ArrayList<String> res = new ArrayList<>();
        while (x != 0 && y != 0){
            if (dp[y][x] != dp[y][x - 1]){
                res.add(lamps[x] + " ");
                y--;
            }
            x--;

        }
        return constructString(res);
    }

    public static String constructString(ArrayList<String> container){
        Collections.reverse(container);
        StringBuilder a = new StringBuilder();
        for(String item: container){
            a.append(item);
        }
        return a.toString();
    }

    public static int fillDP(int x,int y){
        if (outlet[y].equals(lamps[x]))
            return 1 + dp[y-1][x-1];
        else
            return Math.max(dp[y][x-1],dp[y-1][x]);
    }
}
