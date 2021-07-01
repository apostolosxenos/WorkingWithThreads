package task4;

import java.util.Random;

public class Covid implements Runnable {

    private final EmergencyQueue queue;
    private int totalCasesWithoutSpaceInEmergency = 0;
    private int totalCasesInfected = 0;
    private int caseId = 0;

    public Covid(EmergencyQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        Random random = new Random();

        // Το νήμα τρέχει όσες φορές του έχω ορίσει
        for (int i = 0; i < Main4.LOOPS; i++) {

            // Παράγει τυχαίο αριθμό κρουσμάτων [0...K]
            int newCases = random.nextInt(Main4.K + 1);
            int casesToEnterEmergency = 0;
            int casesWithoutSpaceInEmergency = 0;

            // Αν ο αριθμός των νέων κρουσμάτων υπερβαίνει τις διαθέσιμες θέσεις την ουρά
            // μπαίνουν όσα χωράνε και αυτά που μένουν εκτός προσμετρούνται στον συνολικό
            // αριθμό που δεν βρήκαν θέση στην εντατική
            if(newCases > queue.getAvailableSpace()) {
                casesToEnterEmergency = queue.getAvailableSpace();
                casesWithoutSpaceInEmergency = newCases - casesToEnterEmergency;
                totalCasesWithoutSpaceInEmergency += casesWithoutSpaceInEmergency;
            }

            // Διαφορετικά μπαίνουν όλα τα νέα κρούσματα στην ουρά
            else {
                casesToEnterEmergency = newCases;
            }

            // Βάζει ένα-ένα τα νέα κρούσματα στην ουρά δίνοντας τους ενδεικτικά έναν αύξων αριθμό ως id
            for (int k = 0; k < casesToEnterEmergency; k++) {
                queue.put(new CovidCase(++caseId));
            }

            totalCasesInfected += newCases;

            // Εκτυπώσεις ελέγχου ορθής λειτουργίας
            // Αν δεν χρειάζονται μπορούν να γίνουν σχόλια
            System.out.println("COVID infected " + newCases + " new cases.");
            System.out.println("Cases entered Emergency " + casesToEnterEmergency);
            System.out.println("Cases left out of Emergency " + casesWithoutSpaceInEmergency);

            if(totalCasesWithoutSpaceInEmergency > 0) {
                System.out.println("Total number of cases that couldn't find space in Emergency: "
                        + totalCasesWithoutSpaceInEmergency);
            }

            System.out.println("----------------------------------------------------------------");
            System.out.println();

            // Το νήμα COVID κοιμάται για 1 δευτερόλεπτο
            try {
                Thread.sleep(Main4.COVID_SLEEP_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        // Εκτυπώσεις ελέγχου ορθής λειτουργίας
        // Αν δεν χρειάζονται μπορούν να γίνουν σχόλια
        System.out.println();
        System.out.println("COVID LOOPS ENDED.");
        System.out.println("Total cases infected = " + totalCasesInfected);
        System.out.println("Total cases entered Emergency = " + (totalCasesInfected - totalCasesWithoutSpaceInEmergency));
        System.out.println();
    }
}
