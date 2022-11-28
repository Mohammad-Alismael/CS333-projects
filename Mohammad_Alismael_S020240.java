package com.company.project1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MyTreeNode{
    private int[][] grid;
    public static final int windowLength = 4;
    private List<MyTreeNode> children = new ArrayList<MyTreeNode>();
    private MyTreeNode parent = null;
    private int symbol;
    private int bestColumnAt;
    private int score = 0;

    public MyTreeNode(int[][] grid) {
        this.grid = grid;
        setScore();
    }

    public MyTreeNode(int[][] grid,int symbol) {
        this.grid = grid;
        this.symbol = symbol;
        setScore();
    }

    public MyTreeNode(int[][] grid,int symbol, int bestColumnAt) {
        this.grid = grid;
        this.symbol = symbol;
        this.bestColumnAt = bestColumnAt;
        setScore();
    }

    public void addChild(MyTreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(int[][] grid) {
        MyTreeNode newChild = new MyTreeNode(grid);
        this.addChild(newChild);
    }

    public void addChildren(List<MyTreeNode> children) {
        for(MyTreeNode t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<MyTreeNode> getChildren() {
        return children;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    private void setParent(MyTreeNode parent) {
        this.parent = parent;
    }

    public MyTreeNode getParent() {
        return parent;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public int getBestColumnAt() {
        return bestColumnAt;
    }

    public void setBestColumnAt(int bestColumnAt) {
        this.bestColumnAt = bestColumnAt;
    }

    public int getScore() {
        return this.score;
    }

    private void setScore() {

        // center score
        int[] centerArray = getColumn(3);
        int[] count = count(centerArray);
        if (symbol == 1)
            this.score += count[0] * 3;
        if (symbol == 2)
            this.score += count[1] * 3;

        // horizontal
        for (int i = 0; i < 6; i++) {
            int[] rowArray = grid[i];
            for (int j = 0; j <  7 - 3; j++) {
                this.score += getScore_(rowArray, j);
            }
        }

        // vertical
        for (int i = 0; i < 7; i++) {
            int[] colArray = getColumn(i);
            for (int j = 0; j < 6 - 3; j++) {
                this.score += getScore_(colArray, j);
            }
        }

        // positive slop diagonal
        for (int i = 0; i < 6 - 3; i++) {// row
            for (int j = 0; j < 7 - 3; j++) {// column
                ArrayList<Integer> window = new ArrayList<Integer>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(grid[i+k][j+k]);
                }
                this.score += getCount(window);
            }
        }

        // negative slop diagonal
        for (int i = 0; i < 6 - 3; i++) {// row
            for (int j = 0; j < 7 - 3; j++) {// column
                ArrayList<Integer> window = new ArrayList<Integer>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(grid[i+3 - i][j+k]);
                }
                this.score += getCount(window);
            }
        }
    }

    private int getScore_(int[] rowArray, int j) {
        ArrayList<Integer> window = new ArrayList<Integer>();
        for (int k = j; k < j + windowLength; k++) {
            window.add(rowArray[k]);
        }
        return getCount(window);
    }

    private int getCount(ArrayList<Integer> window) {
        int score_ = 0;
        int[] counter = count(window);
        int selected = 0;
        int oppPiece = 1;
        if (symbol == 2) { // AI turn
            selected = counter[1];
        }
        if (symbol == 1) { // user turn
            selected = counter[0];
            oppPiece = 2;
        }
        if (selected == 4)
            score_ += 100;
        if (selected == 3 && counter[2] == 1)
            score_ += 5;
        if (selected == 2 && counter[2] == 2)
            score_ += 2;
        if (counter[oppPiece] == 3 && counter[2] == 1)
            score_ -= 800;
        return score_;
    }

    private static int[] count(ArrayList<Integer> window){
        int xCounter = 0;
        int yCounter = 0;
        int emptyCounter = 0;
        for(Integer place : window){
            if (place == 0)
                emptyCounter++;
            if (place == 1)
                xCounter++;
            if (place == 2)
                yCounter++;
        }
        return new int[]{xCounter, yCounter, emptyCounter}; // [3,4,1] x=>3 y=>4 1=> empty space
    }

    public static int[] count(int[] window){
        int xCounter = 0;
        int yCounter = 0;
        int emptyCounter = 0;
        for(Integer place : window){
            if (place == 0){
                emptyCounter++;
            }
            if (place == 1)
                xCounter++;
            if (place == 2)
                yCounter++;
        }
        return new int[]{xCounter, yCounter, emptyCounter}; // [3,4,1] x=>3 y=>4 1=> empty space
    }

    public int[] getColumn(int index){
        int[] column = new int[6];
        for(int i=0; i<column.length; i++){
            column[i] = this.grid[i][index];
        }
        return column;
    }
}

public class Mohammad_Alismael_S020240 {
    public static int[][] grid = new int[6][7];// Y X
    public static int xSymbol = 1;
    public static int ySymbol = 2;
    public static int depthLevel;
    public static void main(String[] args){

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the search depth:");
        depthLevel = input.nextInt();
        while (!isTerminalNode()) {
            try {
                int pickedColumn = getPickedColumn(input, "X");
                fillGrid(pickedColumn, xSymbol);
                printGrid(grid);
                System.out.println();
                MyTreeNode root = new MyTreeNode(grid,xSymbol);
                depth(root,depthLevel);
                int minimaxValue = minimax(root,depthLevel,true);
                fillGrid(minimaxValue +1, ySymbol);
                printGrid(grid);
                System.out.println();
            }catch (Exception e){
                System.out.println(e.getMessage());
                return;
            }
        }
    }
    public static int[] fillGrid(int pickedColumn, int symbol) throws Exception {
        int[] returned = new int[2];// [1,3] x = i, y= 3
        int[] selectedColumn = getColumn(pickedColumn -1);
        if (selectedColumn[0] != 0){
            throw new Exception("you lost bc the column is full");
        }
        for (int i = 5; i >= 0; i--) {
            if (selectedColumn[i] == 0){
                grid[i][pickedColumn-1] = symbol;
                returned[0] = (pickedColumn-1);// x
                returned[1] = i;// y
                return returned;
            }

        }
        return returned;
    }
    public static int getPickedColumn(Scanner input, String symbol) throws Exception {
        System.out.println(String.format("pick a move (a number between 1-7) you are %s:",symbol));
        int pickedColumn = input.nextInt();
        if (pickedColumn < 0 || pickedColumn > 7){
            throw new Exception("you lost bc of illegal move");
        }
        return pickedColumn;
    }
    public static int minimax(MyTreeNode root,int depth, boolean maximizingPlayer) throws Exception {

        if (depthLevel == 0 || isTerminalNode()) {
            if (isTerminalNode()) {
                if (winningMove(grid, ySymbol))
                    throw new Exception("O has won the game!");
                 else if (winningMove(grid, xSymbol))
                    throw new Exception("X has won the game!");
                 else
                    return 0;
                }
            else {
                return root.getScore();
            }
        }

        if (maximizingPlayer){
            int value = Integer.MIN_VALUE;
            int column = randomValidLocationColumn();
            for(MyTreeNode node : root.getChildren()){
                int new_val = minimax(node, depth- 1, false);
                if (new_val > value){
                    value = new_val;
                    column = node.getBestColumnAt();
                }
            }
            return column;
        }else {// for minimizing player
            int value = Integer.MAX_VALUE;
            int column = randomValidLocationColumn();
            for(MyTreeNode node : root.getChildren()){
                int new_val = minimax(node, depth- 1, true);
                if (new_val < value){
                    value = new_val;
                    column = node.getBestColumnAt();
                }
            }
            return column;
        }

    }
    public static int randomValidLocationColumn(){
        int max = getValidLocations().size();
        int min = 0;
        int range = max - min +1;
        return (int) (Math.random() * range ) + min;
    }

    public static boolean isTerminalNode(){
        // if user or programs win or no room left to place a piece
        return winningMove(grid,xSymbol) || winningMove(grid,ySymbol) || getValidLocations().size() == 0;
    }
    public static ArrayList<Integer> getValidLocations(){
        ArrayList<Integer> validLocations = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            if (isValidLocation(i)){
                validLocations.add(i);
            }
        }
        return validLocations;
    }

    public static boolean isValidLocation(int i) {
        int [] column = getColumn(i);
        int[] count = MyTreeNode.count(column);
        return count[2] != 0;// checking the empty slots
    }
    public static int[] getColumn(int index){
        int[] column = new int[6];
        for(int i=0; i<column.length; i++){
            column[i] = grid[i][index];
        }
        return column;
    }



    public static boolean winningMove(int[][] grid, int symbol){
        // horizontal win
        for (int i = 0; i < 7 - 3; i++) {
            for (int j = 0; j < 6; j++) {
                if (grid[j][i] == symbol && grid[j][i+1] == symbol && grid[j][i+2] ==symbol && grid[j][i+3] == symbol)
                    return true;
            }
        }
        // vertical win
        for (int i = 0; i < 7 ; i++) {
            for (int j = 0; j < 6 - 3; j++) {
                if (grid[j][i] == symbol && grid[j+1][i] == symbol && grid[j+2][i] ==symbol && grid[j+3][i] == symbol)
                    return true;
            }
        }
        // positive slope win
        for (int i = 0; i < 7 -3; i++) {
            for (int j = 0; j < 6 - 3; j++) {
                if (grid[j][i] == symbol && grid[j+1][i+1] == symbol && grid[j+2][i+2] ==symbol && grid[j+3][i+3] == symbol)
                    return true;
            }
        }
        // negative slop win
        for (int i = 0; i < 7 -3; i++) {
            for (int j = 3; j < 6 ; j++) {
                if (grid[j][i] == symbol && grid[j-1][i+1] == symbol && grid[j-2][i+2] ==symbol && grid[j-3][i+3] == symbol)
                    return true;
            }
        }

        return false;
    }

    public static void printGrid(int[][] grid) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (grid[i][j] == 0)
                    System.out.print("# ");
                if (grid[i][j] == 1)
                    System.out.print("X ");
                if (grid[i][j] == 2)
                    System.out.print("O ");
            }
            System.out.println();
        }
    }


    public static void depth(MyTreeNode root, int n){
        if (n <= 0) return; // Nothing to do
        addChildren(root);
        for (MyTreeNode child : root.getChildren()) {
            depth(child, n - 1);
        }
    }


    public static ArrayList a = new ArrayList();
    public static void depthScore(MyTreeNode root, int n){
        if (n <= 0) return; // Nothing to do
        for (int i = 0; i < 7; i++) {
            a.add(root.getChildren().get(i).getBestColumnAt());
        }

        for (MyTreeNode child : root.getChildren()) {
            depthScore(child, n - 1);
        }
    }
    public static int[] getColumn(int index,int[][] grid){
        int[] column = new int[6];
        for(int i=0; i<column.length; i++){
            column[i] = grid[i][index];
        }
        return column;
    }
    public static int[][] copyArray(int[][] matrix){
        int [][] grid = new int[matrix.length][];
        for(int i = 0; i < matrix.length; i++)
        {
            grid[i] = new int[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++)
            {
                grid[i][j] = matrix[i][j];
            }
        }
        return grid;
    }
    public static int[][] fillGridWithCopy(int pickedColumn, int symbol, int[][] grid){
        int[][] possibleGrid = copyArray(grid);
        int[] selectedColumn = getColumn(pickedColumn,possibleGrid);

        for (int i = 5; i >= 0; i--) {
            if (selectedColumn[i] == 0){
                possibleGrid[i][pickedColumn] = symbol;
                return possibleGrid;
            }

        }
        return possibleGrid;
    }

    public static int symbolStart = 2;
    private static void addChildren(MyTreeNode root) {
        List<MyTreeNode> children = new ArrayList<MyTreeNode>();
        int[][] parentGrid = root.getGrid();
        for (int i = 0; i < 7; i++) {
            int[][] generatedGrid = fillGridWithCopy(i,symbolStart,parentGrid);
            MyTreeNode possible = new MyTreeNode(generatedGrid,symbolStart,i);
            children.add(possible);
        }

        if (symbolStart == 2){
            symbolStart = 1;
        }else {
            symbolStart = 2;
        }
        root.addChildren(children);
    }
}
