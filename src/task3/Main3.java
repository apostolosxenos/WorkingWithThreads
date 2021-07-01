package task3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main3 {

    private static final String URL = "http://numbersapi.com/random/date";
    // Στατική thread-safe λίστα ακέραιων που θα περιέχει τα μεγέθη των κειμένων
    static List<Integer> textsLengthList = Collections.synchronizedList(new ArrayList<Integer>());
    // Στατικός thread-safe χάρτης που θα περιέχει τις λέξεις και τη συχνότητα εμφάνισης του
    static Map<String, Integer> wordsMap = new ConcurrentHashMap<String, Integer>();

    public static void main(String[] args) {

        Main3 main3 = new Main3();

        // Καταμέτρηση με ορισμένο αριθμό νημάτων και φορών που θα καλέσουμε το URL
        main3.countWithThreads(16, 8);

        int min = Collections.min(textsLengthList); //ελάχιστο μέγεθος κειμένου
        int max = Collections.max(textsLengthList); //μέγιστο μέγεθος κειμένου
        System.out.println("Minimum text length:" + min);
        System.out.println("Maximum text length:" + max);

        System.out.println();
        System.out.println("Map with words and their occurrences");
        // Εκτύπωση χάρτη με λέξεις και συχνότητα εμφάνισης
        wordsMap.forEach((K,V) -> System.out.println(K + " = " + V));
    }

    private void countWithThreads(int numberOfCalls, int numberOfThreads) {

        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Number of threads to be used must be 1 or more.");
        }

        // Αν ο αριθμός των νημάτων είναι μεγαλύτερος αυτού των κλήσεων
        // τότε αλλάζω τον αριθμό των κλήσεων για να εκμεταλευτώ καλύτερα τη χρήση των πολλαπλών νημάτων
        if (numberOfThreads > numberOfCalls) {
            numberOfCalls = numberOfThreads;
        }

        // Δημιουργία πίνακα νημάτων
        Thread[] threads = new Thread[numberOfThreads];

        // Εκκίνηση χρονομέτρησης
        long start = System.nanoTime();


        for (int i = 0; i < numberOfThreads; i++) {
            // Δημιουργία νήματος το οποίο θα τρέχει τόσες φορές όσες ο λόγος των κλήσεων με τον αριθμό των νημάτων
            threads[i] = new Thread(new HttpCallerThread(URL, numberOfCalls / numberOfThreads));
            // Σετάρισμα μέγιστης προτεραιότητας στο νήμα
            threads[i].setPriority(Thread.MAX_PRIORITY);
            // Εκκίνηση νήματος
            threads[i].start();
        }

        // Αναμονή για τερματισμό των νημάτων
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Τερματισμός χρονομέτρησης
        long end = System.nanoTime();

        // Εκτύπωση διάρκειας
        long duration = (end - start) / 1000000;
        System.out.println("Execution time with " + numberOfThreads +
                " threads for " +numberOfCalls+ " calls is: " + duration+ "ms");
    }
}
