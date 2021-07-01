package task2;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Main2 {

    // Στατική συγχρονισμένη λίστα εγγραφών
    static List<Record> recordsList = Collections.synchronizedList(new ArrayList<Record>());

    public static void main(String[] args) {

        Main2 main2 = new Main2();

        // Δημιουργία αντικειμένου το οποίο χειρίζεται τα IO (input/output) δεδομένα
        DataHandler dataHandler = new DataHandler();
        // Φόρτωση των γραμμών του αρχείου data.csv στην μνήμη (λίστα με συμβολοσειρές)
        List<String> dataList = dataHandler.readFile(new File("data.csv"));

        System.out.println("------------------------------------------------");
        System.out.println("                 SINGLE THREAD                  ");
        System.out.println("------------------------------------------------");
        main2.countWithThreads(dataList, 1, dataHandler);

        main2.clearMaps(dataHandler);
        recordsList.clear();

        System.out.println();
        System.out.println("------------------------------------------------");
        System.out.println("                   2 THREADS                    ");
        System.out.println("------------------------------------------------");
        main2.countWithThreads(dataList, 2, dataHandler);

        main2.clearMaps(dataHandler);
        recordsList.clear();

        System.out.println();
        System.out.println("------------------------------------------------");
        System.out.println("                   4 THREADS                    ");
        System.out.println("------------------------------------------------");
        main2.countWithThreads(dataList, 4, dataHandler);

        main2.clearMaps(dataHandler);
        recordsList.clear();

        System.out.println();
        System.out.println("------------------------------------------------");
        System.out.println("                   8 THREADS                    ");
        System.out.println("------------------------------------------------");
        main2.countWithThreads(dataList, 8, dataHandler);

        System.out.println();
        System.out.println("Export data to text files.");
        // Δημιουργία txt αρχείου και εξαγωγή των δεδομένων του χάρτη χωρών
        dataHandler.exportMapToTxtFile(dataHandler.getCountriesMap(), "cases_per_country.txt");
        // Δημιουργία txt αρχείου και εξαγωγή των δεδομένων του χάρτη ημερομηνιών
        dataHandler.exportMapToTxtFile(dataHandler.getDatesMap(), "cases_per_date.txt");
        System.out.println("Export finished.");

        System.out.println();
        System.out.println("------------------------------------------------");

        // Σορτάρισμα της λίστας εγγραφών με βάση το attribute Ratio της κλάσης Record.
        // Είναι reversed που μας φέρνει αποτελέσματα από το μεγαλύτερο στο μικρότερο
        recordsList.sort(Comparator.comparing(Record::getRatio).reversed());

        System.out.println();
        System.out.println("Top record for Cases/Population ratio: ");
        // Μετά το σορτάρισμα εκτυπώνεται το αντικείμενο με την μεγαλύτερη τιμή που είναι στην πρώτη θέση
        System.out.println(recordsList.get(0).toString());

    }

    // Διαβάζει τη λίστα γραμμών και καταμετρεί τις εγγραφές με ορισμένο αριθμό νημάτων
    // Με reference σε αντικείμενο κλάσης DataHandler, τα νήματα έχουν τη δυνατότητα πρόσβασης στους χάρτες δεδομένων
    private void countWithThreads(List<String> dataList, int numberOfThreads, DataHandler dataHandler) {

        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Number of threads to be used must be 1 or more.");
        }

        int size = dataList.size();

        // Αρχικοποίηση πίνακα νημάτων
        Thread[] threads = new Thread[numberOfThreads];

        // Εκκίνηση χρονομέτρησης
        Instant start = Instant.now();
        for (int i = 0; i < numberOfThreads; i++) {

            // Δημιουργία νήματος με ορισμένο εύρος κάλυψης στην λίστα δεδομένων
            // Με reference σε αντικείμενο κλάσης DataHandler
            // τα νήματα έχουν τη δυνατότητα πρόσβασης στους χάρτες δεδομένων
            threads[i] = new Thread(new SumThread(dataList,
                    (i * size) / numberOfThreads, ((i + 1) * size / numberOfThreads), dataHandler));
            // Σετάρισμα μέγιστης προτεραιότητας
            threads[i].setPriority(Thread.MAX_PRIORITY);
            // Εκκίνηση νήματος
            threads[i].start();
        }

        // Αναμονή για τερματισμό των νημάτων
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        // Τερματισμός χρονομέτρησης
        Instant end = Instant.now();
        System.out.println("Execution time: " + Duration.between(start, end).toMillis() + "ms");
    }

    // Καθαρισμός χαρτών που κρατάνε τα δεδομένων για χώρες και ημερομηνίες
    private void clearMaps(DataHandler dataHandler) {
        dataHandler.getCountriesMap().clear();
        dataHandler.getDatesMap().clear();
    }
}
