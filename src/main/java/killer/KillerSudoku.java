package killer;

import classic.ClassicSudoku;
import helper.Pair;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.*;

public class KillerSudoku extends ClassicSudoku {

    static int attemptsToDo = 3;
    static int maxCageSize = 5;

    public static KillerSudokuType GenerateSudoku(KillerSudokuType sudoku){
        Pair<Boolean, List<List<Integer>>> result = Generate(sudoku.getGrid(), new int[]{0, 0});
        int[][] grid = new int[9][9];
        for(int row = 0; row < 9; row++){
            grid[row] = result.getSecond().get(row).stream().mapToInt(i->i).toArray();
        }
        KillerSudokuType generatedGrid = new KillerSudokuType(sudoku.getType(), grid);

        ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> cages = AddCages(new KillerSudokuType(sudoku.getType(), grid, new ArrayList<>()), new ArrayList<>());

        System.out.println("Remove Cells:\n");
        generatedGrid = new KillerSudokuType(sudoku.getType(), generatedGrid.getGrid(), cages);

        int[][] unfilledGrid = RemoveCells(generatedGrid);

        return new KillerSudokuType(sudoku.getType(), unfilledGrid, grid, cages);


    }
    @SuppressWarnings("unchecked")
    public static Pair<Boolean, Pair<Integer, Integer>> CheckValidFromCutOff(ArrayList<Pair<Integer, Integer>> cellsUsed, ArrayList<Pair<Integer, Integer>> points, Pair<Integer, Integer> newPoint){
        ArrayList<Pair<Integer, Integer>> used = (ArrayList<Pair<Integer, Integer>>) cellsUsed.clone();
        used.addAll(points);
        used.add(newPoint);


        int maxRow = Math.min(used.stream().mapToInt(Pair::getFirst).max().orElse(0) + 2, 9);

        //System.out.println("\n\n" + cellsUsed);
        //System.out.println(maxY);

        for(int row = 0; row < maxRow; row++){
            for(int column = 0; column < 9; column++){
                Pair<Integer, Integer> pos = new Pair<>(row, column);
                if(used.contains(pos)){
                    continue;
                }
                // Check if all surrounded cells have been used
                boolean allUsed = true;
                for(int drow = -1; drow <= 1; drow++){
                    for(int dcolumn = -1; dcolumn <=1; dcolumn++){
                        if(row+drow < 0 || row+drow > 8 || column+dcolumn < 0 || column+dcolumn > 8 || (drow != 0 && dcolumn != 0) || (drow == 0 && dcolumn == 0)){
                            continue;
                        }
                        if(!used.contains(new Pair<>(row+drow, column+dcolumn))){
                            //System.out.println("For " + pos + " , " + new Pair<>(row+drow, column+dcolumn) + " is available");
                            allUsed = false;
                            break;
                        }
                    }
                    if(!allUsed){
                        break;
                    }
                }

                if(allUsed){
                    return new Pair<>(false, pos);
                }
            }
        }
        return new Pair<>(true, null);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> AddCages(KillerSudokuType sudoku, ArrayList<Pair<Integer, Integer>> cellsUsed) {
        ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> cages = (ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>>) sudoku.getCages().clone();
        //System.out.println("\n\nNew Cage");
        //System.out.println(cellsUsed);

        //cellsUsed.sort(Comparator.comparing((Pair<Integer, Integer> pair)->pair.getFirst()).thenComparing(Pair::getSecond));

        //System.out.println(cellsUsed);

        // Find next free cell for start position
        Pair<Integer, Integer> startPosition = null;
        for(int x = 0; x < 9; x++){
            boolean found = false;
            for(int y = 0; y < 9; y++){
                if(!cellsUsed.contains(new Pair<>(x, y))) {
                    found = true;
                    startPosition = new Pair<>(x, y);
                    break;
                }
            }
            if(found){
                break;
            }
        }

        if(startPosition == null){
            return null;
        }
        //System.out.println("Start Position: " + startPosition);

        int attemptsToDo = 100;
        while(attemptsToDo-- > 0) {
            //System.out.println("\nAttempts Left: " + attemptsToDo);

            ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();
            ArrayList<Integer> values = new ArrayList<>();
            points.add(startPosition);
            values.add(sudoku.getGrid()[startPosition.getFirst()][startPosition.getSecond()]);
            Pair<Integer, Integer> mostRecentPoint = new Pair<>(startPosition);

            ArrayList<Pair<Integer, Integer>> possibilities = new ArrayList<>();

            // To be used when we have a trapped point that needs to be picked next
            Pair<Integer, Integer> forcedPoint = null;

            int counter = 1000;
            while (--counter > 0) {
                //System.out.println("\n New point");
                Pair<Integer, Integer> point = null;
                if(forcedPoint != null){
                    point = new Pair<>(forcedPoint);
                    //System.out.println("Forced: " + point);
                } else {
                    possibilities.clear();
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int x = mostRecentPoint.getFirst() + dx;
                            int y = mostRecentPoint.getSecond() + dy;
                            if (x >= 0 && x <= 8 && y >= 0 && y <= 8 && (dx == 0 || dy == 0) && !(dx == 0 && dy == 0) && (!cellsUsed.contains(new Pair<>(x, y))) && (!points.contains(new Pair<>(x, y))) && (!values.contains(sudoku.getGrid()[x][y]))) {
                                possibilities.add(new Pair<>(x, y));
                            }
                        }
                    }
                    if (possibilities.size() == 0) {
                        break;
                    }
                    int randomIndex = new Random().nextInt(possibilities.size());
                    point = possibilities.get(randomIndex);
                }

                /*if(cellsUsed.contains(point) || points.contains(point) || values.contains(sudoku.getGrid()[point.getFirst()][point.getSecond()])){
                    if(){

                    }
                    System.out.println("Continuing #1");
                    continue;
                }*/

                Pair<Boolean, Pair<Integer, Integer>> checkCutOff = CheckValidFromCutOff(cellsUsed, points, point);
                if(!checkCutOff.getFirst()){
                    //System.out.println("Fails adding: " + point + " - with starting pos: " + startPosition + " - with current points: " + points +". Failed point: " + checkCutOff.getSecond());
                    //System.out.println(cellsUsed);
                    //System.out.println();
                    forcedPoint = checkCutOff.getSecond();
                } else{
                    forcedPoint = null;
                }


                //System.out.println("Point: " + point);
                points.add(point);
                values.add(sudoku.getGrid()[point.getFirst()][point.getSecond()]);
                mostRecentPoint = new Pair<>(point);

                if(forcedPoint != null){
                    continue;
                }

                int randomNum = new Random().nextInt(10);
                if(randomNum <= 3 && points.size() == 2){
                    break;
                } else if(randomNum > 3 && randomNum <= 7 && points.size() == 3){
                    break;
                }else if(randomNum > 7 && points.size() > 1){
                    break;
                }else if(points.size() >= maxCageSize){
                    break;
                }

            }
            if(counter == 0){
                int value = sudoku.getGrid()[startPosition.getFirst()][startPosition.getSecond()];
                Pair<Integer, Integer>[] cells = new Pair[]{new Pair<Integer, Integer>(startPosition)};
                ArrayList<Pair<Integer, Integer>> cellsList = (ArrayList<Pair<Integer, Integer>>) List.of(cells);
                cages.add(new Pair<>(value, cellsList));
                cellsUsed.addAll(cellsList);
                break;
            }

            if(points.size() == 1){
                continue;
            }
            int totalSum = points.stream().mapToInt(x -> sudoku.getGrid()[x.getFirst()][x.getSecond()]).sum();
            cellsUsed.addAll(points);
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
        float startTime = System.currentTimeMillis();
        return Remove(sudoku, possibilities, 1, startTime);
    }
    private static int[][] Remove(KillerSudokuType sudoku, List<List<List<Integer>>> possibilities, int depth, float startTime){
        if(possibilities.size() == 0){
            return null;
        }
        float timeTakenSoFar = System.currentTimeMillis() - startTime;
        System.out.println("Depth: " + depth + " - " + timeTakenSoFar);

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
                int[][] result = Remove(new KillerSudokuType(sudoku.getType(), grid, sudoku.getCages()), possibilities, depth+1, startTime);

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
