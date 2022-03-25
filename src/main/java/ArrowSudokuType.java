import java.util.ArrayList;

public class ArrowSudokuType extends ClassicSudokuType{

    private ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows = new ArrayList<>();

    public ArrowSudokuType(SudokuType type, int[][] grid) {
        super(type, grid);
    }

    public ArrowSudokuType(SudokuType type) {
        super(type);
    }

    public ArrowSudokuType(ClassicSudokuType sudoku) {
        super(sudoku);
    }

    public ArrowSudokuType(int[][] grid) {
        super(grid);
    }

    public ArrowSudokuType(SudokuType type, int[][] grid, ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows) {
        super(type, grid);
        this.arrows = arrows;
    }

    public ArrowSudokuType(SudokuType type, ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows) {
        super(type);
        this.arrows = arrows;
    }

    public ArrowSudokuType(ClassicSudokuType sudoku, ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows) {
        super(sudoku);
        this.arrows = arrows;
    }

    public ArrowSudokuType(int[][] grid, ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows) {
        super(grid);
        this.arrows = arrows;
    }


    public ArrowSudokuType(ClassicSudokuType.SudokuType type, int[][] unfilledGrid, int[][] filledGrid, 
            ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> arrows) {
        super(type, unfilledGrid, filledGrid);
        this.arrows = arrows;
    }

    public ArrayList<Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>>> getArrows(){
        return arrows;
    }

    public void PrintSudoku() {
        for(int row=0; row < 9; row++){
            if(row % 3 == 0){
                System.out.println(new String(new char[22]).replace("\0", "-"));
            }
            for(int col=0; col < 9; col++){
                if(col % 3 == 0){
                    System.out.print("|");
                }
                if(this.getGrid()[row][col] == 0){
                    System.out.print("  ");
                } else{
                    System.out.print(" " + this.getGrid()[row][col]);
                }
                if(col == 8){
                    System.out.print("|\n");
                }
            }
        }
        System.out.println(new String(new char[22]).replace("\0", "-"));
        //System.out.println("\n");
    }

    public void PrintSudokuStats(){
        super.PrintSudokuStats(this.getGrid());
        //super.PrintSudokuStats();
        if(this.arrows == null){
            System.out.println("Arrows: null");
            return;
        }
        System.out.println("Num of arrows: " + this.arrows.size());
        for (Pair<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> arrow : this.arrows) {
            Pair<Integer, Integer> bulbPosition = arrow.getFirst();
            System.out.println("Bulb: " + bulbPosition.toString());
            System.out.print("Points: ");

            ArrayList<Pair<Integer, Integer>> points = arrow.getSecond();
            for (Pair<Integer, Integer> point : points) {
                System.out.print(point.toString() + "   ");
            }
            System.out.print("\n");
        }

    }


}
