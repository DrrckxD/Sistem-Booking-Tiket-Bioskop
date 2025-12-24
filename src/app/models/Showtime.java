package app.models;

public class Showtime {
    private String movieTitle;
    private String time;
    private int price;
    public boolean[][] seats = new boolean[5][5];

    public Showtime(String title, String time, int price) {
        this.movieTitle = title;
        this.time = time;
        this.price = price;
    }

    public String getTitle() { return movieTitle; }
    public String getTime() { return time; }
    public int getPrice() { return price; }

    // Untuk ComboBox
    @Override public String toString() { return movieTitle + " (" + time + ")"; }
}