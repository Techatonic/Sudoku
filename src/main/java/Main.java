public class Main {

    public static void main(String[] args) throws Exception {
        SudokuClassic sudoku = new SudokuClassic();
        sudoku.Generate();
        sudoku.PrintSudoku();
    }

}
