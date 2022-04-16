package killer;

import classic.ClassicSudoku;
import helper.Pair;
import killer.KillerSudokuType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KillerSudoku extends ClassicSudoku {

    static int numCages = 10;
    static int attemptsToDo = 1;
    static int maxCageSize = 4;

    public static KillerSudokuType GenerateSudoku(KillerSudokuType sudoku){
        Pair<Boolean, List<List<Integer>>> result = Generate(sudoku.getGrid(), new int[]{0, 0});
        int[][] grid = new int[9][9];
        for(int row = 0; row < 9; row++){
            grid[row] = result.getSecond().get(row).stream().mapToInt(i->i).toArray();
        }
        KillerSudokuType generatedGrid = new KillerSudokuType(sudoku.getType(), grid);

        ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> cages = AddCages(new KillerSudokuType(sudoku.getType(), grid, new ArrayList<>()), new ArrayList<>());

        //new KillerSudokuType(sudoku.getType(), generatedGrid.getGrid(), cages).PrintSudokuStats();

        System.out.println("Remove Cells:\n");
        generatedGrid = new KillerSudokuType(sudoku.getType(), generatedGrid.getGrid(), cages);

        int[][] unfilledGrid = RemoveCells(generatedGrid);

        return new KillerSudokuType(sudoku.getType(), unfilledGrid, grid, cages);


    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> AddCages(KillerSudokuType sudoku, ArrayList<Pair<Integer, Integer>> cellsUsed) {
        if(sudoku.getCages().size() == numCages){
            return null;
        }
        ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> cages = (ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>>) sudoku.getCages().clone();

        int attemptsToDo = 1000;
        while(attemptsToDo-- > 0) {
            Pair<Integer, Integer> startPosition = new Pair<>(new Random().nextInt(9), new Random().nextInt(9));
            if(cellsUsed.contains(startPosition)){
                attemptsToDo++;
                continue;
            }

            ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();
            points.add(startPosition);
            Pair<Integer, Integer> mostRecentPoint = new Pair<>(startPosition);

            ArrayList<Pair<Integer, Integer>> possibilities = new ArrayList<>();

            int counter = 0;
            while (++counter < 10000) {
                System.out.println(cellsUsed);
                possibilities.clear();
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int x = mostRecentPoint.getFirst() + dx;
                        int y = mostRecentPoint.getSecond() + dy;
                        if (x >= 0 && x <= 8 && y >= 0 && y <= 8 && (dx==0 || dy==0)) {
                            possibilities.add(new Pair<>(x, y));
                        }
                    }
                }
                if(possibilities.size() == 0){
                    break;
                }
                int randomIndex = new Random().nextInt(possibilities.size());
                Pair<Integer, Integer> point = possibilities.get(randomIndex);

                if(cellsUsed.contains(point) || points.contains(point)){
                    System.out.println("Contains");
                    continue;
                }

                points.add(point);
                cellsUsed.add(point);
                mostRecentPoint = new Pair<>(point);
                if (points.size() == maxCageSize) {
                    break;
                }
            }

            if(points.size() == 1){
                continue;
            }
            int totalSum = points.stream().mapToInt(x -> sudoku.getGrid()[x.getFirst()][x.getSecond()]).sum();
            cages.add(new Pair<>(totalSum, points));
            break;


        }
        if(attemptsToDo == 0){
            return null;
        }

        KillerSudokuType updatedSudoku = new KillerSudokuType(sudoku.getType(), sudoku.getGrid(), cages);
        int solutionsFound = SolveSudoku(updatedSudoku, 0);
        if (solutionsFound == 1) {
            ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> result = AddCages(updatedSudoku, cellsUsed);
            if (result == null) {
                return updatedSudoku.getCages();
            } else {
                return result;
            }
        }
        return null;
    }

    public static int[][] RemoveCells(KillerSudokuType sudoku) {
        List<List<List<Integer>>> possibilities = new ArrayList<>();
        for(int x = 0; x < 9; x++){
            possibilities.add(new ArrayList<>());
            for(int y = 0; y < 9; y++){
                possibilities.get(x).add(new ArrayList<>(Arrays.asList(x, y)));
            }
        }
        return Remove(sudoku, possibilities);
    }
    private static int[][] Remove(KillerSudokuType sudoku, List<List<List<Integer>>> possibilities){
        int[][] grid;
        for (int attempt = 0; attempt < attemptsToDo; attempt++){
            grid = Arrays.stream(sudoku.getGrid()).map(int[]::clone).toArray(int[][]::new);

            int choiceRowIndex = new Random(System.currentTimeMillis()).nextInt(possibilities.size());
            List<List<Integer>> choiceRow = possibilities.get(choiceRowIndex);
            List<Integer> choice = choiceRow.get(new Random(System.currentTimeMillis()).nextInt(choiceRow.size()));
            grid[choice.get(0)][choice.get(1)] = 0;
            //System.out.println("[" + choice.get(0) + ", " + choice.get(1) + "]\n");

            int solutionsFound = SolveSudoku(new KillerSudokuType(sudoku.getType(), Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new), sudoku.getCages()), 0);
            if (solutionsFound == 1) {
                possibilities.get(choiceRowIndex).remove(choiceRow.indexOf(choice));
                if (possibilities.get(choiceRowIndex).size() == 0) {
                    possibilities.remove(choiceRowIndex);
                }
                int[][] result = Remove(new KillerSudokuType(sudoku.getType(), grid, sudoku.getCages()), possibilities);

                if (result == null) {
                    return sudoku.getGrid();
                } else{
                    return result;
                }
            }
        }
        return null;
    }


    static int SolveSudoku(KillerSudokuType sudoku, int solutionsFound) {
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

            if(!ValidGrid(new KillerSudokuType(sudoku.getType(), grid, sudoku.getCages()))){
                continue;
            }
            solutionsFound = SolveSudoku(new KillerSudokuType(sudoku.getType(), grid, sudoku.getCages()), solutionsFound);
        }

        return solutionsFound;
    }

    static boolean ValidGrid(KillerSudokuType sudoku){
        if(!ClassicSudoku.ValidGrid(sudoku.getGrid())){
            return false;
        }
        for(Pair<Integer, ArrayList<Pair<Integer, Integer>>> cage : sudoku.getCages()){
            int sumVal = cage.getSecond().stream().mapToInt(x -> sudoku.getGrid()[x.getFirst()][x.getSecond()]).sum();
            List<Pair<Integer, Integer>> newSum = cage.getSecond().stream().filter(x -> sudoku.getGrid()[x.getFirst()][x.getSecond()] != 0).toList();

            if(newSum.size() == cage.getSecond().size()){
                if(sumVal != cage.getFirst()){
                    return false;
                }
            } else{
                if(sumVal >= cage.getFirst()){
                    return false;
                }
                if(sumVal + 9*(cage.getSecond().size() - newSum.size()) < cage.getFirst()){
                    return false;
                }
            }

        }
        return true;
    }



}
