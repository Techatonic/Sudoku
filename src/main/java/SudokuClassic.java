public class SudokuClassic {

    Sudoku sudoku;

    public void Generate(){
        sudoku = new Sudoku(Sudoku.SudokuType.Classic);
    }


    public void PrintSudoku() throws Exception {
        if(sudoku == null){
            throw new Exception("Sudoku is null");
        }
        for(int row=0; row < 9; row++){
            if(row % 3 == 0){
                System.out.println(new String(new char[13]).replace("\0", "-"));
            }
            for(int col=0; col < 9; col++){
                if(col % 3 == 0){
                    System.out.print("|");
                }
                if(sudoku.grid[row][col] == 0){
                    System.out.print(" ");
                } else{
                    System.out.print(sudoku.grid[row][col]);
                }
                if(col == 8){
                    System.out.print("|\n");
                }
            }
        }
        System.out.println(new String(new char[13]).replace("\0", "-"));
    }


}
