import arrow.ArrowSudoku;
import arrow.ArrowSudokuType;
import classic.ClassicSudokuType;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.protobuf.Api;
import com.sun.jersey.api.client.Client;
import helper.TooManyOptionsException;
import org.apache.commons.cli.*;

import thermo.ThermoSudoku;
import thermo.ThermoSudokuType;
import killer.KillerSudoku;
import killer.KillerSudokuType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        /*
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
         */

        Options options = new Options();
        options.addOption("c", "classic", false, "Generate classic sudoku");
        options.addOption("a", "arrow", false, "Generate arrow sudoku");
        options.addOption("t", "thermo", false, "Generate thermo sudoku");
        options.addOption("k", "killer", false, "Generate killer sudoku");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            int optionCount = (int) Stream
                    .of(cmd.hasOption("c"), cmd.hasOption("a"), cmd.hasOption("t"), cmd.hasOption("k"))
                    .filter(b -> b)
                    .count();
            if (optionCount > 1) {
                throw new TooManyOptionsException("Too many command line options selected");
            }
            if (cmd.hasOption("c") || optionCount == 0) {
                GenerateClassicSudoku();
            } else if (cmd.hasOption("a")) {
                GenerateArrowSudoku();
            } else if (cmd.hasOption("t")) {
                GenerateThermoSudoku();
            } else if (cmd.hasOption("k")) {
                GenerateKillerSudoku();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    static void GenerateClassicSudoku(){
        ClassicSudokuType sudoku = new classic.ClassicSudokuType(classic.ClassicSudokuType.SudokuType.Classic);
        ClassicSudokuType unfilledSudoku = classic.ClassicSudoku.GenerateSudoku(sudoku);
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

        SendToDatabase(generatedSudoku);
    }

    private static void SendToDatabase(KillerSudokuType sudoku) throws Exception {
        /*String credentialsPath = "/home/danny/IdeaProjects/Sudoku/serviceAccouontKey.json";
        Client client = Client.create();
        FirebaseSdk sdk = new FirebaseSdk(
                "https://sudoku-27fa4-default-rtdb.europe-west1.firebasedatabase.app/",
                credentialsPath,
                client
        );

        sdk.setValue("x", "3");

         */

        InputStream credentialsPath = new FileInputStream("/home/danny/IdeaProjects/Sudoku/sudoku-27fa4-0e734eeb02cb.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsPath);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();

        FirebaseApp.initializeApp(options);
        Firestore db = FirestoreClient.getFirestore();


        DocumentReference documentReference = db.collection("killerSudokus").document();
        Map<String, Object> data = new HashMap<>();
        data.put("sudoku", sudoku);

        ApiFuture<WriteResult> result = documentReference.set(data);
        System.out.println("\nUpdate Time: " + result.get().getUpdateTime());


    }



}
