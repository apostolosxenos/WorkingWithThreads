package task2;

import java.util.List;

public class SumThread implements Runnable {

    private List<String> dataList;
    private int startIndex, endIndex;
    private DataHandler dataHandler;
    private static final String SPLIT_BY_COMMA = ",";

    public SumThread(List<String> dataList, int startIndex, int endIndex, DataHandler dataHandler) {

        this.dataList = dataList;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dataHandler = dataHandler;
    }

    @Override
    public void run() {

        // το νήμα τρέχει στο ορισμένο εύρος
        for (int i = startIndex; i < endIndex; i++) {

            // Διαχωρίζει τα δεδομένα κάθε γραμμής που βρίσκεται στη λίστα δεδομένων με βάση το κόμμα
            // και τα τοποθετεί σε διαφορετική θέση σε έναν προσωρινό πίνακα ώστε να πάρουμε συγκεκριμένες πληροφορίες
            String[] data = dataList.get(i).split(SPLIT_BY_COMMA);
            String date = data[0]; // Η ημερομηνία βρίσκεται στο index 0
            int newCases = Integer.parseInt(data[4]); // Τα κρούσματα βρίσκονται στο index 4
            String country = data[6]; //Η χώρα βρίσκεται στο index 6
            // Ο πληθυσμός βρίσκεται στο index 9.
            // Σε κάποιες εγγραφές δεν ισχύει αυτό οπότε γίνεται ο απαραίτητος έλεγχος
            long population = isNumeric(data[9]) ? Long.parseLong(data[9]) : 0;

            double ratio = 0.0d; //αρχικοποίηση μεταβλητής αναλογίας κρουσμάτων-πληθυσμού

            // Ελέγχει αν υπάρχουν νέα κρούσματα και αν ο πληθυσμός είναι από πριν σε σωστή μορφή
            // Ορίζει την αναλογία μετά αυτών των 2
            if (newCases >= 0 && population > 0) {
                ratio = (double) newCases / population;
            }

            this.getAndIncreaseCasesPerCountry(country, newCases);
            this.getAndIncreaseCasesPerDate(date, newCases);

            // Τοποθετεί στη λίστα εγγραφών νέα αντικείμενο κλάσης Record
            // με τις ωφέλιμες πληροφορίες που άντλησε παραπάνω
            Main2.recordsList.add(new Record(date, country, newCases, population, ratio));
        }

    }

    // Γεμίζει συγχρονισμένα τον χάρτη των χωρών και αυξάνει τη συχνότητα εμφάνισης τους
    private synchronized void getAndIncreaseCasesPerCountry(String country, int newCases) {
        if (!dataHandler.getCountriesMap().containsKey(country)) dataHandler.getCountriesMap().put(country, 0);
        int currentCases = dataHandler.getCountriesMap().get(country);
        dataHandler.getCountriesMap().put(country, currentCases + newCases);
    }

    // Γεμίζει συγχρονισμένα τον χάρτη των ημερομηνιών και αυξάνει τη συχνότητα εμφάνισης τους
    private synchronized void getAndIncreaseCasesPerDate(String date, int newCases) {
        if (!dataHandler.getDatesMap().containsKey(date)) dataHandler.getDatesMap().put(date, 0);
        int currentCases = dataHandler.getDatesMap().get(date);
        dataHandler.getDatesMap().put(date, currentCases + newCases);
    }

    // Επιστρέφει true αν η συμβολοσειρά αποτελεί αριθμό ή false σε διαφορετική περίπτωση
    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
