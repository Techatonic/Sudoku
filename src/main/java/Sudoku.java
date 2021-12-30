


public class Sudoku {
    public enum SudokuType{
        Classic,
        Arrow
    }


    SudokuType type;
    int[][] grid;

    public Sudoku(SudokuType type, int[][] grid){
        this.type = type;
        this.grid = grid;
    }
    public Sudoku(SudokuType type){
        this.type = type;
        this.grid = new int[9][9];
    }

    public SudokuType getType() {
        return type;
    }

    public int[][] getGrid() {
        return grid;
    }
}
