import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SudokuClassic {

    public static Sudoku Generate(Sudoku sudoku){
        // Template
        return sudoku;
    }

    static int SolveSudoku(Sudoku sudoku, int solutionsFound){
        int[][] grid = sudoku.grid;
        Sudoku.SudokuType type = sudoku.type;

        int[] nextEmpty = null;
        for(int row=0; row < grid.length; row++){
             for(int col=0; col < grid[0].length; col++){
                 if(grid[row][col] == 0){
                     nextEmpty = new int[]{row, col};
                     break;
                 }
             }
             if(nextEmpty != null){
                 break;
             }
        }

        if(nextEmpty == null){
            solutionsFound++;
            return solutionsFound;
        }

        for(int attempt=1; attempt < 10; attempt++){
            sudoku.setPosition(nextEmpty[0], nextEmpty[1], attempt);
            if(!ValidGrid(sudoku)){
                continue;
            }
            return SolveSudoku(sudoku, solutionsFound);
        }

         return solutionsFound;
    }

    static boolean ValidGrid(Sudoku sudoku){
        // Checks valid rows
        for(int[] row : sudoku.grid){
            int[] newRow = Arrays.stream(row).filter(x -> x != 0).toArray();
            Set<Integer> setRow = Arrays.stream(newRow).boxed().collect(Collectors.toSet());
            if(row.length != setRow.size()){
                return false;
            }
        }
        // Checks valid columns
        for(int col = 0; col < sudoku.grid.length; col++){
            int[] column = new int[sudoku.grid.length];
            for(int i = 0; i<sudoku.grid.length; i++){
                column[i] = sudoku.grid[i][col];
            }
            int[] newCol = Arrays.stream(column).filter(x -> x != 0).toArray();
            Set<Integer> setCol = Arrays.stream(newCol).boxed().collect(Collectors.toSet());
            if(newCol.length != setCol.size()){
                return false;
            }
        }
        // Checks valid boxes
        for(int boxX = 0; boxX < 3; boxX++){
            for(int boxY = 0; boxY < 3; boxY++){
                int[] values = new int[sudoku.grid.length];
                for(int x=3*boxX; x < 3*boxX+3; x++){
                    for(int y=3*boxY; y < 3*boxY+3; y++){
                        values[3*(x%3)+(y%3)] = sudoku.grid[x][y];
                    }
                }
                int[] newVals = Arrays.stream(values).filter(x -> x != 0).toArray();
                Set<Integer> setVals = Arrays.stream(values).boxed().collect(Collectors.toSet());
                if(newVals.length != setVals.size()){
                    return false;
                }
            }
        }

        return true;
    }

}
