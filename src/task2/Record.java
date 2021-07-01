package task2;

public class Record {

    private String date;
    private String country;
    private Long population;
    private int cases;
    private double ratio;

    public Record(String date, String country, int cases, Long population, double ratio) {
        this.date = date;
        this.country = country;
        this.cases = cases;
        this.population = population;
        this.ratio = ratio;
    }

    public double getRatio() {
        return this.ratio;
    }

    // Τροποίηση της μεθόδου ώστε να πάρουμε την εκτύπωση που θέλουμε
    @Override
    public String toString() {
        return "Date=" + date + ", Country=" + country + ", Ratio=" + ratio;
    }
}