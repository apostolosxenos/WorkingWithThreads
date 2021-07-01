package task4;

public class Main4 {

    static final int LOOPS = 20;
    static final int K = 5;
    static final int H = 4;
    static final int E = 10;
    static final int COVID_SLEEP_DURATION = 1000;
    static final int HOSPITAL_SLEEP_DURATION = 5000;

    public static void main(String[] args) {

        //Δημιουργία ουράς με το ορισμένο μέγεθος
        EmergencyQueue queue = new EmergencyQueue(E);

        // Δημιουργία νήματος COVID ως βασικός Producer
        Thread covidThread = new Thread(new Covid(queue), "COVID");

        // Δημιουργία νήματων HOSPITAL ως 3 Consumers
        Thread hospitalThread = new Thread(new Hospital(queue), "Hospital 1");
        Thread hospitalThread2 = new Thread(new Hospital(queue), "Hospital 2");
        Thread hospitalThread3 = new Thread(new Hospital(queue), "Hospital 3");

        // Εκκίνηση νημάτων
        covidThread.start();
        hospitalThread.start();
        hospitalThread2.start();
        hospitalThread3.start();

        // Αναμονή τερματισμού νημάτων
        try {
            covidThread.join();
            hospitalThread.join();
            hospitalThread2.join();
            hospitalThread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("END");
    }
}
