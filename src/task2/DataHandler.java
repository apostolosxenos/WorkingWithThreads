package task2;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {

    private Map<String, Integer> countriesMap;
    private Map<String, Integer> datesMap;


    public DataHandler() {
        //Δημιουργία νέου χάρτη χωρών σε χάρτη thread-safe κλάσης ConcurrentHashMap
        this.countriesMap = new ConcurrentHashMap<>();
        //Δημιουργία νέου χάρτη ημερομηνιών σε χάρτη thread-safe κλάσης ConcurrentHashMap
        this.datesMap = new ConcurrentHashMap<>();
    }

    public Map<String, Integer> getCountriesMap() {
        return this.countriesMap;
    }

    public Map<String, Integer> getDatesMap() {
        return this.datesMap;
    }

    // Διαβάζει ένα αρχείο γραμμή-γραμμή και τις βάζει σε μια λίστα που επιστρέφει στο τέλος
    public List<String> readFile(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            List<String> dataList = new ArrayList<String>();

            br.readLine(); // προσπερνάει την πρώτη γραμμή με μη ωφέλιμη πληροφορία
            String line = "";

            // Όσο υπάρχουν μη κενές γραμμές τις τοποθετεί σε λίστα
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }

            return dataList;

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    // Εξάγει τα δεδομένα ενός χάρτη σε αρχείο
    public void exportMapToTxtFile(Map<String, Integer> map, String fileName) {

        if(map.isEmpty()) throw new IllegalStateException("Map is empty.");

        File file = new File(fileName);

        // Φορτώνει τον χάρτη σε TreeMap για αυτόματο σορτάρισμα
        TreeMap<String, Integer> sortedMap = new TreeMap<String,Integer>(map);

        // Γράφει τα δεδομένα του χάρτη στο αρχείο με μορφή "Key=Value"
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                bufferedWriter.write(entry.getKey() + "=" + entry.getValue());
                bufferedWriter.newLine(); //νέα γραμμή για την επόμενη εγγραφή
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
