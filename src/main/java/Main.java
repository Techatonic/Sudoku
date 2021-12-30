public class Main {

    public static void main(String[] args) throws Exception {
        // Test Classic Sudoku
        Sudoku sudoku = new Sudoku(Sudoku.SudokuType.Classic);
        Sudoku unfilledSudoku = SudokuClassic.Generate(sudoku);
        unfilledSudoku.PrintSudoku();
    }

}
