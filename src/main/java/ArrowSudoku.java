import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ArrowSudoku extends ClassicSudoku {

    public static ArrowSudokuType GenerateSudoku(ArrowSudokuType sudoku){
        Pair<Boolean, List<List<Integer>>> result = Generate(sudoku.getGrid(), new int[]{0, 0});
        int[][] grid = new int[9][9];
        for(int row = 0; row < 9; row++){
            grid[row] = result.getSecond().get(row).stream().mapToInt(i->i).toArray();
        }
        ArrowSudokuType generatedGrid = new ArrowSudokuType(sudoku.getType(), grid);

        ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows = null;//AddArrows(new ArrowSudokuType(sudoku.getType(), grid, new ArrayList<>()));
        int[][] unfilledGrid = RemoveCells(generatedGrid);
        System.out.println(Arrays.deepToString(unfilledGrid));

        return new ArrowSudokuType(sudoku.getType(), unfilledGrid, arrows);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> AddArrows(ArrowSudokuType sudoku) {
        if(sudoku.getArrows().size() == 5){
            return null;
        }
        ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows = (ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>>) sudoku.getArrows().clone();

        int attemptsToDo = 1000;
        while(attemptsToDo-- > 0) {
            Pair<Integer, Integer> bulbPosition = new Pair<>(new Random().nextInt(9), new Random().nextInt(9));
            int totalValue = sudoku.getGrid()[bulbPosition.getFirst()][bulbPosition.getSecond()];

            ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();
            int cumSumValue = 0;
            Pair<Integer, Integer> mostRecentPoint = new Pair<>(bulbPosition);

            ArrayList<Pair<Integer, Integer>> possibilities = new ArrayList<>();

            while (possibilities.size() > 0 || points.isEmpty()) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int x = mostRecentPoint.getFirst() + dx;
                        int y = mostRecentPoint.getSecond() + dy;
                        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
                            possibilities.add(new Pair<>(x, y));
                        }
                    }
                }
                int randomIndex = new Random().nextInt(possibilities.size());
                Pair<Integer, Integer> point = possibilities.get(randomIndex);
                //System.out.println("Attempting: " + point);
                boolean foundEqual = false;
                for (Pair<Integer, Integer> cell : points) {
                    if (cell.equals(point)) {
                        foundEqual = true;
                        break;
                    }
                }
                if (foundEqual || point.equals(bulbPosition)) {
                    continue;
                }
                int val = sudoku.getGrid()[point.getFirst()][point.getSecond()];

                if (cumSumValue + val > totalValue) {
                    break;
                }
                cumSumValue += val;
                //System.out.println("Adding point: " + point);
                points.add(point);
                mostRecentPoint = point;
                if (cumSumValue == totalValue) {
                    break;
                }
            }
            if (cumSumValue != totalValue) {
                continue;
            }
            System.out.println("Adding arrow");
            arrows.add(new Pair<>(bulbPosition, points));
            //System.out.println("Size1: " + arrows.size());
            //System.out.println("Bulb: " + bulbPosition + " - " + sudoku.getGrid()[bulbPosition.getFirst()][bulbPosition.getSecond()]);
            //for(Pair<Integer, Integer> point : arrows.get(arrows.size()-1).getSecond()){
            //    System.out.println("Point: " + point + " - " + sudoku.getGrid()[point.getFirst()][point.getSecond()]);
            //}
            //System.out.println("Cumsum value = " + cumSumValue);
            //System.out.println("Bulb value = " + totalValue);
            break;
        }
        if(attemptsToDo == 0){
            //System.out.println("Attempts left == 0");
            return null;
        }

        ArrowSudokuType updatedSudoku = new ArrowSudokuType(sudoku.getType(), sudoku.getGrid(), arrows);
        //System.out.println("Size: " + arrows.size());
        int solutionsFound = SolveSudoku(updatedSudoku, 0);
        //System.out.println("Solutions Found: " + solutionsFound);
        if (solutionsFound == 1) {
            ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> result = AddArrows(updatedSudoku);
            if (result == null) {
                //System.out.println("continue");
                return updatedSudoku.getArrows();
            } else {
                //System.out.println("something");
                return result;
            }
        }
        //}
        System.out.println("null");
        return null;
    }

    public static int[][] RemoveCells(ArrowSudokuType sudoku) {
        List<List<List<Integer>>> possibilities = new ArrayList<>();
        for(int x = 0; x < 9; x++){
            possibilities.add(new ArrayList<>());
            for(int y = 0; y < 9; y++){
                possibilities.get(x).add(new ArrayList<>(Arrays.asList(x, y)));
            }
        }
        int[][] grid = Remove(sudoku, possibilities);
        //System.out.println("\n\nSolutions Found: " + SolveSudoku(new ArrowSudokuType(ClassicSudokuType.SudokuType.Classic, grid, sudoku.getArrows()), 0));
        return grid;
    }
    private static int[][] Remove(ArrowSudokuType sudoku, List<List<List<Integer>>> possibilities){
        int[][] grid;

        for (int attempt = 0; attempt < attemptsToDo; attempt++){
            grid = Arrays.stream(sudoku.getGrid()).map(int[]::clone).toArray(int[][]::new);
            //sudoku.PrintSudoku();

            int choiceRowIndex = new Random(System.currentTimeMillis()).nextInt(possibilities.size());
            List<List<Integer>> choiceRow = possibilities.get(choiceRowIndex);
            List<Integer> choice = choiceRow.get(new Random(System.currentTimeMillis()).nextInt(choiceRow.size()));
            grid[choice.get(0)][choice.get(1)] = 0;
            //System.out.println("[" + choice.get(0) + ", " + choice.get(1) + "]\n");

            int solutionsFound = SolveSudoku(new ArrowSudokuType(sudoku.getType(), Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new), sudoku.getArrows()), 0);
            //System.out.println("Solutions Found: " + solutionsFound);
            //System.out.println(Arrays.deepToString(grid));

            if (solutionsFound == 1) {
                possibilities.get(choiceRowIndex).remove(choiceRow.indexOf(choice));
                if (possibilities.get(choiceRowIndex).size() == 0) {
                    possibilities.remove(choiceRowIndex);
                }
                int[][] result = Remove(new ArrowSudokuType(sudoku.getType(), grid, sudoku.getArrows()), possibilities);

                if (result == null) {
                    //return grid;
                    return sudoku.getGrid();
                } else{
                    return result;
                }
            }
        }
        return null;
    }


    static int SolveSudoku(ArrowSudokuType sudoku, int solutionsFound) {
        //System.out.println("Solutions Found (Arrow): " + solutionsFound);
        //sudoku.PrintSudokuStats();
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

            if(!ValidGrid(sudoku)){
                continue;
            }
            solutionsFound = SolveSudoku(new ArrowSudokuType(sudoku.getType(), grid, sudoku.getArrows()), solutionsFound);
        }

        return solutionsFound;
    }

    static boolean ValidGrid(ArrowSudokuType sudoku){
        if(!ClassicSudoku.ValidGrid(sudoku.getGrid())){
            return false;
        }

        for(Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> arrow : sudoku.getArrows()){
            int bulbVal = sudoku.getGrid()[arrow.getFirst().getFirst()][arrow.getFirst().getSecond()];
            int sumVal = arrow.getSecond().stream().mapToInt(x -> sudoku.getGrid()[x.getFirst()][x.getSecond()]).sum();
            if(bulbVal != sumVal){
                return false;
            }
        }

        return true;
    }



}
