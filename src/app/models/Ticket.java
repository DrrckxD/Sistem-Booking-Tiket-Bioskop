package app.models;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Ticket {
    private String owner, movie, time, seat;

    public Ticket(String o, String m, String t, String s) {
        this.owner=o; this.movie=m; this.time=t; this.seat=s;
    }

    // Getter untuk Tabel
    public String getMovie() { return movie; }
    public String getTime() { return time; }
    public String getSeat() { return seat; }
    public String getOwner() { return owner; }

    public void printTxT() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("Tiket_" + owner + "_" + seat + ".txt"))) {
            pw.println("=== CINEMA TICKET ===");
            pw.println("Film  : " + movie);
            pw.println("Jam   : " + time);
            pw.println("Kursi : " + seat);
            pw.println("User  : " + owner);
            pw.println("=====================");
        } catch (Exception e) { e.printStackTrace(); }
    }
}