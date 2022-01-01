public class Main {

    public static void main(String[] args) {
        // Test Classic Sudoku
        //int[][] test = new int[][]{{1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9},
        //        {1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9},
        //        {1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}};
        Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic);
        Sudoku unfilledSudoku = SudokuClassic.GenerateSudoku(sudoku);
        unfilledSudoku.PrintSudoku();
        unfilledSudoku.PrintSudokuStats();
    }

}
