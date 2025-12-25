package app.models;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;

public class Ticket implements Serializable {
    private String owner, movie, time, seat;

    public Ticket(String o, String m, String t, String s) {
        this.owner = o; this.movie = m; this.time = t; this.seat = s;
    }

    public String getMovie() { return movie; }
    public String getTime() { return time; }
    public String getSeat() { return seat; }
    public String getOwner() { return owner; }

    public void printTxT() {
        // 'true' membuat data baru tertulis di bawah data lama (tidak menimpa)
        try (PrintWriter pw = new PrintWriter(new FileWriter("riwayat_transaksi.txt", true))) {
            pw.println("USER: " + owner + " | FILM: " + movie + " | JAM: " + time + " | KURSI: " + seat);
            pw.println("------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}