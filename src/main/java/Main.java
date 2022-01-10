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


        Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic);
        Sudoku unfilledSudoku = ClassicSudoku.GenerateSudoku(sudoku);
        unfilledSudoku.PrintSudoku();
        unfilledSudoku.PrintSudokuStats();


        /*
        Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic, test);
        //int result = SudokuClassic.SolveSudoku(sudoku, 0);
        //sudoku.PrintSudoku();

        Sudoku newSudoku = new Sudoku(Sudoku.SudokuType.Classic,SudokuClassic.RemoveCells(sudoku));

        newSudoku.PrintSudoku();
        newSudoku.PrintSudokuStats();
         */
    }

}
