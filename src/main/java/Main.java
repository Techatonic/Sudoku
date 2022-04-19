import arrow.ArrowSudoku;
import classic.ClassicSudokuType;
import arrow.ArrowSudokuType;
import helper.Pair;
import thermo.ThermoSudoku;
import thermo.ThermoSudokuType;
import killer.KillerSudoku;
import killer.KillerSudokuType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Test Classic Sudoku
        /*
        int[][] test = new int[][]{
                {7, 6, 8, 4, 1, 3, 5, 9, 2}, // Row 1
                {4, 3, 1, 2, 9, 5, 8, 6, 7},
                {5, 9, 2, 8, 6, 7, 1, 4, 3},
                {8, 5, 0, 7, 2, 1, 0, 3, 9},
                {1, 7, 9, 6, 3, 4, 2, 8, 5},
                {3, 2, 0, 5, 8, 9, 0, 7, 1},
                {2, 4, 7, 9, 5, 6, 3, 1, 8},
                {6, 8, 3, 1, 7, 2, 9, 5, 4},
                {9, 1, 5, 3, 4, 8, 7, 2, 6} // Row 9
        };
        */

        /*
        classic.ClassicSudokuType sudoku = new classic.ClassicSudokuType(classic.ClassicSudokuType.SudokuType.Classic);
        classic.ClassicSudokuType unfilledSudoku = classic.ClassicSudoku.GenerateSudoku(sudoku);
        unfilledSudoku.PrintSudoku();
        unfilledSudoku.PrintSudokuStats();
        */

        /*
        Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic, test);
        //int result = SudokuClassic.SolveSudoku(sudoku, 0);
        //sudoku.PrintSudoku();

        Sudoku newSudoku = new Sudoku(Sudoku.SudokuType.Classic,SudokuClassic.RemoveCells(sudoku));

        newSudoku.PrintSudoku();
        newSudoku.PrintSudokuStats();
         */
        /*
        final long startTime = System.currentTimeMillis();

        int maxNumOfGiven = 20;
        
        ArrowSudokuType emptySudoku = new ArrowSudokuType(ClassicSudokuType.SudokuType.Arrow);
        ArrowSudokuType generatedSudoku = emptySudoku;
        int given = 35;
        while(given > maxNumOfGiven){    
            generatedSudoku = ArrowSudoku.GenerateSudoku(emptySudoku);
            given = generatedSudoku.GetCellsGiven();
        }
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("\n");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double)(endTime - startTime)/1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");

         */

        /*
        final long startTime = System.currentTimeMillis();

        int maxNumOfGiven = 80;

        ThermoSudokuType emptySudoku = new ThermoSudokuType(ClassicSudokuType.SudokuType.Thermo);
        ThermoSudokuType generatedSudoku = emptySudoku;
        int given = 81;
        while(given > maxNumOfGiven){
            generatedSudoku = ThermoSudoku.GenerateSudoku(emptySudoku);
            given = generatedSudoku.GetCellsGiven();
        }
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("\n");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double)(endTime - startTime)/1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");
         */

        final long startTime = System.currentTimeMillis();

        int maxNumOfGiven = 7;

        KillerSudokuType emptySudoku = new KillerSudokuType();
        KillerSudokuType generatedSudoku = emptySudoku;
        int given = 81;
        int attempt = 1;
        while (given > maxNumOfGiven) {
            System.out.println("Attempt #" + attempt++);
            generatedSudoku = KillerSudoku.GenerateSudoku(emptySudoku);
            given = generatedSudoku.GetCellsGiven();
        }
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("\n");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double) (endTime - startTime) / 1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");

        /*
        ArrayList<Pair<Integer, Integer>> cellsUsed = new ArrayList<>();
        cellsUsed.add(new Pair<>(0, 0));
        cellsUsed.add(new Pair<>(0, 1));
        cellsUsed.add(new Pair<>(0, 2));
        cellsUsed.add(new Pair<>(0, 3));
        cellsUsed.add(new Pair<>(0, 4));
        cellsUsed.add(new Pair<>(0, 5));
        cellsUsed.add(new Pair<>(0, 6));
        cellsUsed.add(new Pair<>(0, 7));
        cellsUsed.add(new Pair<>(0, 8));
        cellsUsed.add(new Pair<>(1, 0));
        cellsUsed.add(new Pair<>(1, 1));
        cellsUsed.add(new Pair<>(1, 2));
        cellsUsed.add(new Pair<>(1, 3));
        cellsUsed.add(new Pair<>(1, 4));
        cellsUsed.add(new Pair<>(1, 5));
        cellsUsed.add(new Pair<>(1, 6));
        cellsUsed.add(new Pair<>(1, 7));
        cellsUsed.add(new Pair<>(1, 8));
        cellsUsed.add(new Pair<>(2, 0));
        cellsUsed.add(new Pair<>(2, 2));
        cellsUsed.add(new Pair<>(2, 3));
        cellsUsed.add(new Pair<>(2, 6));
        cellsUsed.add(new Pair<>(2, 7));
        cellsUsed.add(new Pair<>(2, 8));
        cellsUsed.add(new Pair<>(3, 0));
        cellsUsed.add(new Pair<>(3, 1));
        cellsUsed.add(new Pair<>(4, 1));
        cellsUsed.add(new Pair<>(4, 2));


        Pair<Boolean, Pair<Integer, Integer>> result = KillerSudoku.CheckValidFromCutOff(cellsUsed, new Pair<>(5, 3), new Pair<>(5, 4));
        System.out.println(result);*/
    }
}
