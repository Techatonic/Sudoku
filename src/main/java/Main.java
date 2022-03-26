import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Test Classic Sudoku
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

        /*
        ClassicSudokuType sudoku = new ClassicSudokuType(ClassicSudokuType.SudokuType.Classic);
        ClassicSudokuType unfilledSudoku = ClassicSudoku.GenerateSudoku(sudoku);
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
        
        final long startTime = System.currentTimeMillis();

        int maxNumOfGiven = 30;
        
        ArrowSudokuType emptySudoku = new ArrowSudokuType(ClassicSudokuType.SudokuType.Arrow);
        ArrowSudokuType generatedSudoku = emptySudoku;
        int given = 81;
        while(given > maxNumOfGiven){    
            generatedSudoku = ArrowSudoku.GenerateSudoku(emptySudoku);
            given = generatedSudoku.GetCellsGiven();
            //break; // Remove this
        }
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double)(endTime - startTime)/1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");

    }
}
