import classic.ClassicSudokuType;
import classic.ClassicSudokuType.SudokuType;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SendClassicSudokuToDatabase {

    static boolean firebaseInitialised = false;

    static int GetDocumentCountInCollection(Firestore db, String collection) throws Exception {
        DocumentReference documentReference = db.collection(collection).document("data");
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if(document.exists()){
            Map<String, Object> data = document.getData();
            System.out.println("Document Data: " + data);
            if (data != null) {
                return (int) Math.floor((long) data.get("documentCount"));
            } else{
                throw new Exception();
            }
        } else{
            throw new Exception();
        }
    }

    public SendClassicSudokuToDatabase(ClassicSudokuType sudoku) throws Exception {
        if(!firebaseInitialised) {
            InputStream serviceAccount = new FileInputStream("/home/danny/IdeaProjects/Sudoku/sudoku-27fa4-0e734eeb02cb.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
            firebaseInitialised = true;
        }

        Firestore db = FirestoreClient.getFirestore();

        int documentCount = GetDocumentCountInCollection(db, Main.collectionNameBySudokuType.get(SudokuType.Classic));

        DocumentReference document = db.collection(Main.collectionNameBySudokuType.get(SudokuType.Classic)).document("classicsudoku-"+(documentCount+1));
        Map<String, Object> data = new HashMap<>();

        // Grid
        for(int row = 0; row < sudoku.getGrid().length; row++){
            ArrayList<Integer> newRow = new ArrayList<>();
            for(int val : sudoku.getGrid()[row]){
                newRow.add(val);
            }
            data.put("grid-row"+row, newRow);
        }


        // Filled Grid
        for(int row = 0; row < sudoku.getFilledGrid().length; row++){
            ArrayList<Integer> newRow = new ArrayList<>();
            for(int val : sudoku.getFilledGrid()[row]){
                newRow.add(val);
            }
            data.put("filledGrid-row"+row, newRow);
        }

        data.put("cellsGiven", sudoku.GetCellsGiven());

        ApiFuture<WriteResult> result = document.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());

        IncrementDocumentCount(db, Main.collectionNameBySudokuType.get(SudokuType.Classic), documentCount+1);
    }

    static void IncrementDocumentCount(Firestore db, String collection, int newVal) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("documentCount", newVal);

        ApiFuture<WriteResult> future = db.collection(collection).document("data").set(data);
        System.out.println("Update time : " + future.get().getUpdateTime());

    }

}
