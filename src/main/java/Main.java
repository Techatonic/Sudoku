public class Main {

    public static void main(String[] args) {
        // Test Classic Sudoku
        int[][] test = new int[][]{
                {9, 7, 8, 3, 6, 1, 5, 2, 4}, // Row 1
                {2, 0, 4, 9, 8, 5, 7, 6, 1},
                {1, 0, 0, 4, 7, 2, 8, 9, 3},
                {8, 2, 9, 1, 4, 3, 6, 7, 5},
                {6, 4, 7, 8, 0, 9, 1, 3, 2},
                {5, 1, 3, 6, 2, 7, 9, 4, 8},
                {7, 9, 2, 5, 3, 8, 0, 1, 6}, // [6,6] was a 4 - was most recently removed cell
                {4, 0, 1, 2, 9, 6, 3, 0, 7},
                {3, 8, 6, 7, 1, 4, 2, 5, 9} // Row 9
        };


        Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic);
        Sudoku unfilledSudoku = SudokuClassic.GenerateSudoku(sudoku);
        unfilledSudoku.PrintSudoku();
        unfilledSudoku.PrintSudokuStats();


        /*Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic, test);
        int result = SudokuClassic.SolveSudoku(sudoku, 0);
        System.out.println("Solutions Found: " + result);*/
    }

}
