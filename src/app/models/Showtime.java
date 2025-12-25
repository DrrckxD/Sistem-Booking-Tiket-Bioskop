package app.models;
import java.io.Serializable;

public class Showtime implements Serializable {
    private String movieTitle;
    private String time;
    private int price;
    private String imagePath;

    private String synopsis;
    private String duration;
    private String cast;

    public boolean[][] seats = new boolean[5][5];

    public Showtime(String title, String time, int price, String imagePath, String synopsis, String duration, String cast) {
        this.movieTitle = title;
        this.time = time;
        this.price = price;
        this.imagePath = imagePath;
        this.synopsis = synopsis;
        this.duration = duration;
        this.cast = cast;
    }


    public String getTitle() { return movieTitle; }
    public void setTitle(String t) { this.movieTitle = t; }

    public String getTime() { return time; }
    public void setTime(String t) { this.time = t; }

    public int getPrice() { return price; }
    public void setPrice(int p) { this.price = p; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String p) { this.imagePath = p; }

    public String getSynopsis() { return synopsis; }
    public String getDuration() { return duration; }
    public String getCast() { return cast; }

    @Override public String toString() { return time; }
}