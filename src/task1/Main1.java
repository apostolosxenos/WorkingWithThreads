package task1;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Main1 {

    // Στατικός χάρτης που κρατάει τις συνολικές μετρήσεις χακτήρων
    static Map<Character, Integer> mainMap = new ConcurrentHashMap<Character, Integer>();

    public static void main(String[] args) {

        Main1 main1 = new Main1();
        int arrayLength = (int) Math.pow(2, 20); // 2^20 = 1048576 κωδικοί
        String[] passwordsArray = new String[arrayLength]; // Αρχικοποίηση πίνακα κωδικών με το παραπάνω μήκος

        //Γέμισμα του πίνακα με 1048576 τυχαίους κωδικούς
        main1.fillWithRandomPasswords(passwordsArray);

        System.out.println("------------------------------------------------");
        System.out.println("                 SINGLE THREAD                  ");
        System.out.println("------------------------------------------------");

        // Γέμισμα του στατικού χάρτη με τους χαρακτήρες της συμβολοσειράς ως keys
        fillCharacters();
        // Χρονική καταμέτρηση με τη χρήση 1 thread
        long duration = main1.countWithThreads(passwordsArray, 1);
        System.out.println("Execution time with 1 thread = " + duration + "ms");
        System.out.println(mainMap);
        System.out.println("");

        System.out.println("------------------------------------------------");
        System.out.println("                   2 THREADS                    ");
        System.out.println("------------------------------------------------");

        // Καθαρισμός βασικού χάρτη
        mainMap.clear();
        // Γέμισμα του στατικού χάρτη με τους χαρακτήρες της συμβολοσειράς ως keys
        fillCharacters();
        // Χρονική καταμέτρηση με τη χρήση 2 threads
        long duration2 = main1.countWithThreads(passwordsArray, 2);
        System.out.println("Execution time with 2 threads = " + duration2 + "ms");
        System.out.println(mainMap);
        System.out.println("");

        System.out.println("------------------------------------------------");
        System.out.println("                   4 THREADS                    ");
        System.out.println("------------------------------------------------");

        // Καθαρισμός βασικού χάρτη
        mainMap.clear();
        // Γέμισμα του στατικού χάρτη με τους χαρακτήρες της συμβολοσειράς ως keys
        fillCharacters();
        // Χρονική καταμέτρηση με τη χρήση 4 threads
        long duration4 = main1.countWithThreads(passwordsArray, 4);
        System.out.println("Execution time with 4 threads = " + duration4 + "ms");
        System.out.println(mainMap);
        System.out.println("");

        // Καθαρισμός βασικού χάρτη
        mainMap.clear();
        //Γέμισμα του στατικού χάρτη με τους χαρακτήρες της συμβολοσειράς ως keys
        fillCharacters();
        System.out.println("------------------------------------------------");
        System.out.println("                   8 THREADS                    ");
        System.out.println("------------------------------------------------");
        // Χρονική καταμέτρηση με τη χρήση 8 threads
        long duration8 = main1.countWithThreads(passwordsArray, 8);
        System.out.println("Execution time with 8 threads = " + duration8 + "ms");
        System.out.println(mainMap);
    }

    private long countWithThreads(String[] array, int numberOfThreads) {

        // Δεν είναι αποδεκτός αριθμός μικρότερος ή ίσος του 0
        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Number of threads to be used must be 1 or more.");
        }

        int length = array.length;

        // Αρχικοποίηση πίνακα με threads
        Thread[] threads = new Thread[numberOfThreads];

        // Εκκίνηση χρονομέτρησης
        long start = System.nanoTime();

        for (int i = 0; i < numberOfThreads; i++) {

            // Δημιουργία thread που θα καταμετρήσει σε συγκεκριμένο έυρος του πίνακα των κωδικών
            threads[i] = new Thread(new CharCounterThread(array,
                    (i * length) / numberOfThreads, ((i + 1) * length / numberOfThreads)));

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
                System.err.println(e.getMessage());
            }
        }
        // Τερματισμός χρονομέτρησης
        long end = System.nanoTime();

        // Επιστροφή χρονικής διάρκειας σε milliseconds
        return (end - start) / 1000000L;
    }

    // Γέμισμα του πίνακα με τυχαίους κωδικούς
    private void fillWithRandomPasswords(String[] arr) {

        int length = arr.length;
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            // Δημιουργία τυχαίου κωδικού τυχαίου μήκους [8...16]
            Password password = new Password(random.nextInt(16 - 8) + 8);
            arr[i] = password.generate();
        }
    }

    // Γέμισμα του στατικού χάρτη με τους χαρακτήρες της συμβολοσειράς ως keys
    private static void fillCharacters() {
        int length = Password.ALPHABET.length();
        for (int i = 0; i < length; i++) {
            mainMap.put(Password.ALPHABET.charAt(i), 0);
        }
    }

    public static Map<Character, Integer> getMainMap() {
        return mainMap;
    }
}