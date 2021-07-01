package task4;

import java.util.Random;

public class Hospital implements Runnable {

    private final EmergencyQueue queue;

    public Hospital(EmergencyQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        // Το νήμα τρέχει όσες φορές του έχω ορίσει
        for (int i = 0; i < Main4.LOOPS; i++) {

            // Το νήμα κοιμάται για 5 δευτερόλεπτα περιμένοντας στην ουσία πρώτα την μόλυνση ένω κρουσμάτων
            // από το νήμα του COVID
            try {
                Thread.sleep(Main4.HOSPITAL_SLEEP_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Random random = new Random();
            // Τυχαίο αριθμός δυνατότητας ανάρρωσης
            int casesToRecover = random.nextInt(Main4.H); // [0...H]

            // Αφαιρεί από την ουρά τον αριθμό δυνατότητας ανάρρωσης
            queue.get(casesToRecover);
        }
        System.out.println();
        System.out.println(Thread.currentThread().getName() + "'s LOOPS ENDED.");
        System.out.println();
    }
}