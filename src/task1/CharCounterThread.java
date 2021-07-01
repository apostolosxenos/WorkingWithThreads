package task1;

import java.util.HashMap;
import java.util.Map;

    public class CharCounterThread implements Runnable {

    private String[] passwordsArray;
    private int startIndex, endIndex;
    private Map<Character, Integer> charactersSubMap;

    public CharCounterThread(String[] passwordsArray, int startIndex, int endIndex) {

        this.passwordsArray = passwordsArray;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        charactersSubMap = new HashMap<>();

    }

    @Override
    public void run() {

        // Το νήμα τρέχει σε συγκεκριμένο εύρος του πίνακα κωδικών
        for (int i = startIndex; i < endIndex; i++) {

            String password = passwordsArray[i];
            int passwordLength = password.length();

            // Κάθε χαρακτήρας του κωδικού αποθηκεύεται στον υποχάρτη του νήματος
            // Αν υπάρχει ήδη ανεβαίνει η τιμή του κατά 1.
            for (int j = 0; j < passwordLength; j++) {
                Character key = password.charAt(j);
                if (!this.charactersSubMap.containsKey(key)) this.charactersSubMap.put(key, 0);
                int count = this.charactersSubMap.get(key);
                this.charactersSubMap.replace(key, count + 1);
            }
        }

        this.addCharactersSubMapToMainMap();
    }

    // Αθροίζω τον υποχάρτη του νήματος στον βασικό χάρτη που κρατάει τις τελικές μετρήσεις
    private synchronized void addCharactersSubMapToMainMap() {
        for (Map.Entry<Character, Integer> entry : charactersSubMap.entrySet()) {
            int count = Main1.getMainMap().get(entry.getKey());
            Main1.getMainMap().replace(entry.getKey(), count + entry.getValue());
        }
    }
}