import arrow.ArrowSudoku;
import arrow.ArrowSudokuType;
import classic.ClassicSudokuType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import helper.TooManyOptionsException;
import org.apache.commons.cli.*;
import thermo.ThermoSudoku;
import thermo.ThermoSudokuType;
import killer.KillerSudoku;
import killer.KillerSudokuType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class Main {

    public static Map<ClassicSudokuType.SudokuType, String> collectionNameBySudokuType = Map.ofEntries(
            Map.entry(ClassicSudokuType.SudokuType.Classic, "classicsudokus"),
            Map.entry(ClassicSudokuType.SudokuType.Arrow, "arrowsudokus"),
            Map.entry(ClassicSudokuType.SudokuType.Thermo, "thermosudokus"),
            Map.entry(ClassicSudokuType.SudokuType.Killer, "killersudokus")
    );

    public static void main(String[] args) throws Exception {
        /*
        Projog projog = new Projog();
        projog.consultFile(new File("src/main/resources/killerSudoku"));

        QueryStatement s1 = projog.createStatement("sudoku(" +
                "[" +
                "    (15, [A1, B1])," +
                "    (17, [A2, A3, A4])," +
                "    (13, [A5, B5, B6])," +
                "    ( 6, [A6, A7])," +
                "    ( 9, [A8, B8])," +
                "    (17, [A9, B9])," +
                "    ( 3, [B2, B3])," +
                "    (23, [B4, C4, D4, E4, F4])," +
                "    (21, [B7, C7, C8, D8, E8])," +
                "    ( 7, [C1, C2])," +
                "    (12, [C3, D3])," +
                "    (14, [C5, D5])," +
                "    (17, [C6, D6])," +
                "    (22, [C9, D9, E9, F9, G9])," +
                "    ( 5, [D1, D2])," +
                "    (16, [D7, E7, E6])," +
                "    (18, [E1, E2, E3])," +
                "    ( 9, [E5, F5])," +
                "    ( 6, [F1, F2])," +
                "    (27, [F3, G3, G4, G5, G6])," +
                "    (10, [F6, F7])," +
                "    ( 9, [F8, G8])," +
                "    (10, [G1, G2])," +
                "    (23, [G7, H7, H8])," +
                "    (14, [H1, I1])," +
                "    ( 4, [H2, H3])," +
                "    (30, [H4, I4, I3, I2])," +
                "    ( 5, [H5, I5])," +
                "    ( 9, [H6, I6])," +
                "    ( 6, [H9, I9])," +
                "    ( 8, [I7, I8])" +
                "]," +
                "[" +
                "[ 7,  9,  6,  2, A5, A6, A7, A8, A9]," +
                "[B1, B2, B3, B4, B5, B6, B7, B8, B9]," +
                "[C1, C2, C3, C4, C5, C6, C7, C8, C9]," +
                "[D1, D2, D3, D4, D5, D6, D7, D8, D9]," +
                "[E1, E2, E3, E4, E5, E6, E7, E8, E9]," +
                "[F1, F2, F3, F4, F5, F6, F7, F8, F9]," +
                "[G1, G2, G3, G4, G5, G6, G7, G8, G9]," +
                "[H1, H2, H3, H4, H5, H6, H7, H8, H9]," +
                "[I1, I2, I3, I4, I5, I6, I7, I8, I9]]).");
        QueryResult r1 = s1.executeQuery();
        while(r1.next()){
            System.out.println("I3 = " + r1.getTerm("I3"));
        }
        for(String variable : r1.getVariableIds()) {
            System.out.println(variable + " = " + r1.getTerm(variable));
        }
        System.out.println("Done");

         */

        Options options = new Options();
        options.addOption("c", "classic", false, "Generate classic sudoku");
        options.addOption("a", "arrow",   false, "Generate arrow sudoku");
        options.addOption("t", "thermo",  false, "Generate thermo sudoku");
        options.addOption("k", "killer",  false, "Generate killer sudoku");

        char sudokuType = 'c';

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            int optionCount = (int) Stream
                    .of(cmd.hasOption("c"), cmd.hasOption("a"), cmd.hasOption("t"), cmd.hasOption("k"))
                    .filter(b -> b)
                    .count();
            if(optionCount > 1){
                throw new TooManyOptionsException("Too many command line options selected");
            }
            if(cmd.hasOption("a")){
                sudokuType = 'a';
            } else if(cmd.hasOption("t")){
                sudokuType = 't';
            } else if(cmd.hasOption("k")){
                sudokuType = 'k';
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        int count = 100;
        if(args.length > 1){
            try {
                count = Integer.parseInt(args[1]);
            } catch (Exception e){
                System.out.println("Command line arguments have not been entered correctly");
                return;
            }
        }
        for(int i = 0; i < count; i++){
            GenerateSudoku(sudokuType);
        }
    }

    static void GenerateSudoku(char sudokuType) throws Exception {
        switch (sudokuType) {
            case 'c' -> GenerateClassicSudoku();
            case 'a' -> GenerateArrowSudoku();
            case 't' -> GenerateThermoSudoku();
            case 'k' -> GenerateKillerSudoku();
            default -> {
            }
        }
    }


    static void GenerateClassicSudoku() throws Exception {
        ClassicSudokuType sudoku = new classic.ClassicSudokuType(classic.ClassicSudokuType.SudokuType.Classic);
        ClassicSudokuType unfilledSudoku = classic.ClassicSudoku.GenerateSudoku(sudoku);
        unfilledSudoku.PrintSudoku();
        unfilledSudoku.PrintSudokuStats();

        new SendClassicSudokuToDatabase(unfilledSudoku);
    }

    static void GenerateArrowSudoku(){
        final long startTime = System.currentTimeMillis();

        int maxNumOfGiven = 20;

        ArrowSudokuType emptySudoku = new ArrowSudokuType();
        ArrowSudokuType generatedSudoku = emptySudoku;
        int given = 35;
        while(given > maxNumOfGiven){
            generatedSudoku = ArrowSudoku.GenerateSudoku(emptySudoku);
            given = generatedSudoku.GetCellsGiven();
        }
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("\n");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double)(endTime - startTime)/1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");
    }

    static void GenerateThermoSudoku(){
        final long startTime = System.currentTimeMillis();

        int maxNumOfGiven = 80;

        ThermoSudokuType emptySudoku = new ThermoSudokuType();
        ThermoSudokuType generatedSudoku = emptySudoku;
        int given = 81;
        while(given > maxNumOfGiven){
            generatedSudoku = ThermoSudoku.GenerateSudoku(emptySudoku);
            given = generatedSudoku.GetCellsGiven();
        }
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("\n");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double)(endTime - startTime)/1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");
    }

    static void GenerateKillerSudoku() throws Exception {
        final long startTime = System.currentTimeMillis();

        KillerSudokuType emptySudoku = new KillerSudokuType();
        KillerSudokuType generatedSudoku;
        generatedSudoku = KillerSudoku.GenerateSudoku(emptySudoku);
        generatedSudoku.PrintSudoku(generatedSudoku.getFilledGrid());
        System.out.println("\n");
        generatedSudoku.PrintSudoku();
        generatedSudoku.PrintSudokuStats();

        final long endTime = System.currentTimeMillis();
        final double totalTime = (double) (endTime - startTime) / 1000;
        System.out.println("\nTotal execution time: " + totalTime + " seconds");

        saveSudoku(generatedSudoku);

//        new SendKillerSudokuToDatabase(generatedSudoku);
    }

    private static void saveSudoku(KillerSudokuType generatedSudoku) throws IOException {
        Gson gson = new Gson();

        //try (FileWriter file = new FileWriter("/home/danny/Documents/Uni/Year3/Diss/pythonProject/sudokus/" + UUID.randomUUID().toString() + ".json")){


        File file = File.createTempFile("killer_sudoku_", "", new File("/home/danny/Documents/Coding/Python/sudoku-python-csp/sudokus"));

        try (FileWriter file_writer = new FileWriter(file.getAbsolutePath() + ".json")){
            gson.toJson(generatedSudoku, file_writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            file.delete();
        } catch (Exception e){
            return;
        }
        System.out.println("Saved");
    }


}
