import arrow.ArrowSudoku;
import classic.ClassicSudokuType;
import arrow.ArrowSudokuType;
import helper.Pair;
//import org.apache.commons.cli.*;
import thermo.ThermoSudoku;
import thermo.ThermoSudokuType;
import killer.KillerSudoku;
import killer.KillerSudokuType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {


        /*
        Options options = new Options();
        options.addOption("c", "classic", false, "Generate classic sudoku");
        options.addOption("a", "arrow",   false, "Generate arrow sudoku");
        options.addOption("t", "thermo",  false, "Generate thermo sudoku");
        options.addOption("k", "killer",  false, "Generate killer sudoku");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            int optionCount = (int) Stream
                    .of(cmd.hasOption("c"), cmd.hasOption("a"), cmd.hasOption("t"), cmd.hasOption("k"))
                    .filter(b -> b)
                    .count();
            if(optionCount > 1){
                throw new Exception();
            }
            if(cmd.hasOption("c")){
                GenerateClassicSudoku();
            } else if(cmd.hasOption("a")){
                GenerateArrowSudoku();
            } else if(cmd.hasOption("t")){
                GenerateThermoSudoku();
            } else if(cmd.hasOption("k")){
                GenerateKillerSudoku();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

         */

        GenerateKillerSudoku();
    }


    static void GenerateClassicSudoku(){
        classic.ClassicSudokuType sudoku = new classic.ClassicSudokuType(classic.ClassicSudokuType.SudokuType.Classic);
        classic.ClassicSudokuType unfilledSudoku = classic.ClassicSudoku.GenerateSudoku(sudoku);
        unfilledSudoku.PrintSudoku();
        unfilledSudoku.PrintSudokuStats();
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

    static void GenerateKillerSudoku(){
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
    }




}
