package thermo;

import classic.ClassicSudokuType;
import helper.Pair;

import java.util.ArrayList;

public class ThermoSudokuType extends ClassicSudokuType{
    private ArrayList<ArrayList<Pair<Integer, Integer>>> thermos = new ArrayList<>();

    public ThermoSudokuType(ClassicSudokuType.SudokuType type, int[][] grid) {
        super(type, grid);
    }

    public ThermoSudokuType() {
        super(SudokuType.Thermo);
    }

    public ThermoSudokuType(ClassicSudokuType sudoku) {
        super(sudoku);
    }

    public ThermoSudokuType(int[][] grid) {
        super(grid);
    }

    public ThermoSudokuType(ClassicSudokuType.SudokuType type, int[][] grid, ArrayList<ArrayList<Pair<Integer, Integer>>> thermos) {
        super(type, grid);
        this.thermos = thermos;
    }

    public ThermoSudokuType(ClassicSudokuType.SudokuType type, ArrayList<ArrayList<Pair<Integer, Integer>>> thermos) {
        super(type);
        this.thermos = thermos;
    }

    public ThermoSudokuType(ClassicSudokuType sudoku, ArrayList<ArrayList<Pair<Integer, Integer>>> thermos) {
        super(sudoku);
        this.thermos = thermos;
    }

    public ThermoSudokuType(int[][] grid, ArrayList<ArrayList<Pair<Integer, Integer>>> thermos) {
        super(grid);
        this.thermos = thermos;
    }


    public ThermoSudokuType(ClassicSudokuType.SudokuType type, int[][] unfilledGrid, int[][] filledGrid,
                            ArrayList<ArrayList<Pair<Integer, Integer>>> thermos) {
        super(type, unfilledGrid, filledGrid);
        this.thermos = thermos;
    }

    public ArrayList<ArrayList<Pair<Integer, Integer>>> getThermos(){
        return thermos;
    }

    public void PrintSudoku() {
        for(int row=0; row < 9; row++){
            if(row % 3 == 0){
                System.out.println(new String(new char[22]).replace("\0", "-"));
            }
            for(int col=0; col < 9; col++){
                if(col % 3 == 0){
                    System.out.print("|");
                }
                if(this.getGrid()[row][col] == 0){
                    System.out.print("  ");
                } else{
                    System.out.print(" " + this.getGrid()[row][col]);
                }
                if(col == 8){
                    System.out.print("|\n");
                }
            }
        }
        System.out.println(new String(new char[22]).replace("\0", "-"));
    }

    public void PrintSudokuStats(){
        super.PrintSudokuStats(this.getGrid());
        //super.PrintSudokuStats();
        if(this.thermos == null){
            System.out.println("Thermos: null");
            return;
        }
        System.out.println("Num of thermos: " + this.thermos.size());
        for (ArrayList<Pair<Integer, Integer>> thermo : this.thermos) {
            System.out.print("Points: ");
            for (Pair<Integer, Integer> point : thermo) {
                System.out.print(point.toString() + "   ");
            }
            System.out.print("\n");
        }

    }
}
