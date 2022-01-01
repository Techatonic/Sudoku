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
    public Sudoku(Sudoku sudoku){
        this.type = sudoku.type;
        this.grid = sudoku.grid;
    }

    public SudokuType getType() {
        return type;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setPosition(int row, int col, int val){
        grid[row][col] = val;
    }

    public void PrintSudoku() {
        for(int row=0; row < 9; row++){
            if(row % 3 == 0){
                System.out.println(new String(new char[13]).replace("\0", "-"));
            }
            for(int col=0; col < 9; col++){
                if(col % 3 == 0){
                    System.out.print("|");
                }
                if(grid[row][col] == 0){
                    System.out.print(" ");
                } else{
                    System.out.print(grid[row][col]);
                }
                if(col == 8){
                    System.out.print("|\n");
                }
            }
        }
        System.out.println(new String(new char[13]).replace("\0", "-"));
        //System.out.println("\n");
    }

    public void PrintSudokuStats(){
        int cellsGiven = 0;

        for(int[] row : grid){
            for(int cell : row){
                if(cell != 0){
                    cellsGiven++;
                }
            }
        }

        System.out.println("Number of cells given: " + cellsGiven);
    }
}
