package app.models;
import java.io.Serializable;

public class Showtime implements Serializable {
    private String movieTitle;
    private String time;
    private int price;
    private String imagePath;
    public boolean[][] seats = new boolean[5][5];

    public Showtime(String title, String time, int price, String imagePath) {
        this.movieTitle = title;
        this.time = time;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getTitle() { return movieTitle; }
    public void setTitle(String t) { this.movieTitle = t; }

    public String getTime() { return time; }
    public void setTime(String t) { this.time = t; }

    public int getPrice() { return price; }
    public void setPrice(int p) { this.price = p; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String p) { this.imagePath = p; }

    @Override public String toString() { return movieTitle + " (" + time + ")"; }
}