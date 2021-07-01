package task4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EmergencyQueue {

    private BlockingQueue<CovidCase> queue;
    private int capacity;
    private int totalCasesRecovered;

    public EmergencyQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedBlockingQueue<>(capacity);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getAvailableSpace() {
        return capacity - queue.size();
    }

    public int getCurrentSize() {
        return queue.size();
    }

    public void put(CovidCase covidCase) {
        queue.add(covidCase);
    }

    // Συγχρονισμένη μέθοδος αφαίρεσης από την ουρά γιατί χρησιμοποιείται από > 1 νήματα
    public synchronized void get(int casesToRecover) {

        // Αν η ουρά δεν είναι άδεια
        if (!isEmpty()) {

            // Αν ο αριθμός δυνατότητας θεραπείας είναι μεγαλύτερος των κρουσμάτων στην ουρά
            // μεταβάλλεται ώστε να είναι ίσος με τον αριθμό των κρουσμάτων που υπάρχουν
            if (casesToRecover > getCurrentSize()) {
                casesToRecover = getCurrentSize();
            }

            // 'Θεραπεύει' ένα-ένα τα κρούσματα αφαιρώντας τα από την ουρά
            for (int r = 0; r < casesToRecover; r++) {
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Εκτυπώσεις ελέγχου ορθής λειτουργίας
            // Αν δεν χρειάζονται μπορούν να γίνουν σχόλια
            System.out.println(Thread.currentThread().getName() + " recovered " + casesToRecover);
            totalCasesRecovered += casesToRecover;
            System.out.println("Total cases recovered:" + totalCasesRecovered);
        }

        else {
            System.out.println(Thread.currentThread().getName() + ": No cases to recover.");
        }
    }
}
