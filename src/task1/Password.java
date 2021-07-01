package task1;

public class Password {

    //Συμβολοσειρά λατινικών χαρακτήρων
    static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";

    //Δεξαμενή λατινικών χαρακτήρων
    private int length;

    public Password(int length) {

        //Αν δεν έχει οριστεί το προαπαιτούμενο μήκος πετάει εξαίρεση
        if (length < 8 || length > 16) {
            throw new IllegalArgumentException("Password length must be between 8 and 16 characters.");
        }

        this.length = length;
    }

    public String generate() {

        StringBuilder sb = new StringBuilder();

        // Σύμφωνα με το ορισμένο μέγεθος ξεκινάει η δημιουργία κωδικού
        for (int i = 0; i < this.length; i++) {
            // Τυχαία επιλογή χαρακτήρα από τη συμβολοσειρά χαρακτήρων
            int index = (int) (ALPHABET.length() * Math.random());
            // Χτίσιμο του κωδικού προσθέτοντας έναν χαρακτήρα κάθε φορά
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
}
