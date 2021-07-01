package task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpCallerThread implements Runnable {

    private String url;
    private int numberOfCalls;

    public HttpCallerThread(String url, int numberOfCalls) {
        this.url = url;
        this.numberOfCalls = numberOfCalls;
    }

    @Override
    public void run() {

        for (int i = 0; i < numberOfCalls; i++) {

            try {

                // Εγκατάσταση σύνδεσης με URL μέσω HTTP κλήσης
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                // Παίρνω το input stream της σύνδεσης
                InputStream inputStream = connection.getInputStream();

                // Δημιουργώ το κείμενο με βάση το διάβασμα του input stream
                // διαβάζοντας χαρακτήρες με κωδικοποίηση UTF8 και χωρίζοντας τις γραμμές
                // με τον ειδικό χαρακτήρα σήμανσης τερματισμού γραμμής \n
                String text = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                        .collect(Collectors.joining("\n"));

                // Παίρνω το μέγεθος του κειμένου
                int length = text.length();
                // Το αποθηκεύω στην λίστα
                Main3.textsLengthList.add(length);

                // Αφαιρώ τα σημεία στίξης από το κέιμενο με τη βοήθεια του regular expression \p{P}
                String textWithoutPuncuation = text.replaceAll("\\p{P}", "");
                // Φιλτράρω σε πίνακα τις λέξεις με βάση τα κενά μεταξύ τους
                String[] words = textWithoutPuncuation.split(" ");

                // Παίρνω το μέγεθος του πίνακα που περιέχει τις λέξεις
                int wordsLength = words.length;

                // Κάθε λέξη προστίθεται στον στατικό χάρτη και ανανεώνεται η συχνότητα εμφάνισης της συγχρονισμένα
                for (int w = 0; w < wordsLength; w++) {
                    this.getAndIncreaseValue(Main3.wordsMap,words[w]);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Προσθέτει κάθε λέξη στον χάρτη ανανεώνοντας την συχνότητα εμφάνισης της συγχρονισμένα
    private synchronized void getAndIncreaseValue(Map<String, Integer> map, String word) {
        if (!map.containsKey(word)) map.put(word, 0);
        int count = map.get(word);
        map.replace(word, count + 1);
    }
}
