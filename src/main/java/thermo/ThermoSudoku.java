package thermo;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import classic.ClassicSudoku;
import classic.ClassicSudokuType;
import helper.Pair;

public class ThermoSudoku extends ClassicSudoku {

    static int numThermos = 5;
    static int attemptsToDo = 3;

    public static ThermoSudokuType GenerateSudoku(ThermoSudokuType sudoku){
        Pair<Boolean, List<List<Integer>>> result = Generate(sudoku.getGrid(), new int[]{0, 0});
        int[][] grid = new int[9][9];
        for(int row = 0; row < 9; row++){
            grid[row] = result.getSecond().get(row).stream().mapToInt(i->i).toArray();
        }
        ThermoSudokuType generatedGrid = new ThermoSudokuType(sudoku.getType(), grid);

        ArrayList<ArrayList<Pair<Integer, Integer>>> thermos = AddThermos(new ThermoSudokuType(sudoku.getType(), grid, new ArrayList<>()));
        System.out.println("Remove Cells:\n");
        generatedGrid = new ThermoSudokuType(sudoku.getType(), generatedGrid.getGrid(), thermos);
        int[][] unfilledGrid = RemoveCells(generatedGrid);

        return new ThermoSudokuType(sudoku.getType(), unfilledGrid, grid, thermos);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<ArrayList<Pair<Integer, Integer>>> AddThermos(ThermoSudokuType sudoku) {
        if(sudoku.getThermos().size() == numThermos){
            return null;
        }
        ArrayList<ArrayList<Pair<Integer, Integer>>> thermos = (ArrayList<ArrayList<Pair<Integer, Integer>>>) sudoku.getThermos().clone();

        int attemptsToDo = 1000;
        while(attemptsToDo-- > 0) {
            Pair<Integer, Integer> startPosition = new Pair<>(new Random().nextInt(9), new Random().nextInt(9));
            int currVal = sudoku.getGrid()[startPosition.getFirst()][startPosition.getSecond()];

            ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();
            points.add(startPosition);
            Pair<Integer, Integer> mostRecentPoint = new Pair<>(startPosition);

            ArrayList<Pair<Integer, Integer>> possibilities = new ArrayList<>();

            while (true) {
                possibilities.clear();
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int x = mostRecentPoint.getFirst() + dx;
                        int y = mostRecentPoint.getSecond() + dy;
                        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
                            possibilities.add(new Pair<>(x, y));
                        }
                    }
                }
                if(possibilities.size() == 0){
                    break;
                }
                int randomIndex = new Random().nextInt(possibilities.size());
                Pair<Integer, Integer> point = possibilities.get(randomIndex);

                int val = sudoku.getGrid()[point.getFirst()][point.getSecond()];

                if(val <= currVal){
                    break;
                }
                points.add(point);
                mostRecentPoint = new Pair<>(point);
                currVal = val;
                if (currVal == 9) {
                    break;
                }
            }

            if(points.size() == 1){
                continue;
            }
            thermos.add(points);
            break;
        }
        if(attemptsToDo == 0){
            return null;
        }

        ThermoSudokuType updatedSudoku = new ThermoSudokuType(sudoku.getType(), sudoku.getGrid(), thermos);
        int solutionsFound = SolveSudoku(updatedSudoku, 0);
        if (solutionsFound == 1) {
            ArrayList<ArrayList<Pair<Integer, Integer>>> result = AddThermos(updatedSudoku);
            if (result == null) {
                return updatedSudoku.getThermos();
            } else {
                return result;
            }
        }
        return null;
    }

    public static int[][] RemoveCells(ThermoSudokuType sudoku) {
        List<List<List<Integer>>> possibilities = new ArrayList<>();
        for(int x = 0; x < 9; x++){
            possibilities.add(new ArrayList<>());
            for(int y = 0; y < 9; y++){
                possibilities.get(x).add(new ArrayList<>(Arrays.asList(x, y)));
            }
        }
        return Remove(sudoku, possibilities);
    }
    private static int[][] Remove(ThermoSudokuType sudoku, List<List<List<Integer>>> possibilities){
        int[][] grid;
        for (int attempt = 0; attempt < attemptsToDo; attempt++){
            grid = Arrays.stream(sudoku.getGrid()).map(int[]::clone).toArray(int[][]::new);

            int choiceRowIndex = new Random(System.currentTimeMillis()).nextInt(possibilities.size());
            List<List<Integer>> choiceRow = possibilities.get(choiceRowIndex);
            List<Integer> choice = choiceRow.get(new Random(System.currentTimeMillis()).nextInt(choiceRow.size()));
            grid[choice.get(0)][choice.get(1)] = 0;
            //System.out.println("[" + choice.get(0) + ", " + choice.get(1) + "]\n");

            int solutionsFound = SolveSudoku(new ThermoSudokuType(sudoku.getType(), Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new), sudoku.getThermos()), 0);
            if (solutionsFound == 1) {
                possibilities.get(choiceRowIndex).remove(choiceRow.indexOf(choice));
                if (possibilities.get(choiceRowIndex).size() == 0) {
                    possibilities.remove(choiceRowIndex);
                }
                int[][] result = Remove(new ThermoSudokuType(sudoku.getType(), grid, sudoku.getThermos()), possibilities);

                if (result == null) {
                    return sudoku.getGrid();
                } else{
                    return result;
                }
            }
        }
        return null;
    }


    static int SolveSudoku(ThermoSudokuType sudoku, int solutionsFound) {
        int[][] grid = Arrays.stream(sudoku.getGrid()).map(int[]::clone).toArray(int[][]::new);

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
            grid[nextEmpty[0]][nextEmpty[1]] = attempt;

            if(!ValidGrid(new ThermoSudokuType(sudoku.getType(), grid, sudoku.getThermos()))){
                continue;
            }
            solutionsFound = SolveSudoku(new ThermoSudokuType(sudoku.getType(), grid, sudoku.getThermos()), solutionsFound);
        }

        return solutionsFound;
    }

    static boolean ValidGrid(ThermoSudokuType sudoku){
        if(!ClassicSudoku.ValidGrid(sudoku.getGrid())){
            return false;
        }
        for(ArrayList<Pair<Integer, Integer>> thermo : sudoku.getThermos()){
            int curr = 0;
            for(Pair<Integer, Integer> point : thermo){
                if(curr == 9){
                    return false;
                }
                int val = sudoku.getGrid()[point.getFirst()][point.getSecond()];
                if(val == 0){
                    curr++;
                    continue;
                }
                if(val <= curr){
                    return false;
                }
                curr = val;
            }
        }
        return true;
    }



}
