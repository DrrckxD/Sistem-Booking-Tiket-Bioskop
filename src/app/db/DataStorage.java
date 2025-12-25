package app.db;

import app.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static List<User> users = new ArrayList<>();
    public static List<Showtime> schedules = new ArrayList<>();
    public static List<Ticket> tickets = new ArrayList<>();

    private static final String FILE_SCHEDULES = "data_jadwal.dat";
    private static final String FILE_TICKETS = "data_tiket.dat";

    static {
        users.add(new User("admin", "admin", "ADMIN"));
        users.add(new User("guest", "guest", "GUEST"));


        File f = new File(FILE_SCHEDULES);
        if(!f.exists()) {
            String sinopsisDune = "Paul Atreides, seorang pemuda brilian dan berbakat yang lahir dalam takdir besar di luar pemahamannya, harus melakukan perjalanan ke planet paling berbahaya di alam semesta.";
            String castDune = "Timothée Chalamet, Rebecca Ferguson, Oscar Isaac";


            schedules.add(new Showtime("Dune: Part Two", "13:00", 50000, "poster_dune.jpg", sinopsisDune, "2j 46m", castDune));
            schedules.add(new Showtime("Dune: Part Two", "16:30", 50000, "poster_dune.jpg", sinopsisDune, "2j 46m", castDune));
            schedules.add(new Showtime("Dune: Part Two", "20:00", 60000, "poster_dune.jpg", sinopsisDune, "2j 46m", castDune));

            String sinopsisKungfu = "Po bersiap menjadi Pemimpin Spiritual Lembah Damai, tetapi juga membutuhkan seseorang untuk menggantikannya sebagai Prajurit Naga.";
            String castKungfu = "Jack Black, Awkwafina, Viola Davis";


            schedules.add(new Showtime("Kung Fu Panda 4", "12:00", 45000, "poster_panda.jpg", sinopsisKungfu, "1j 34m", castKungfu));
            schedules.add(new Showtime("Kung Fu Panda 4", "14:30", 45000, "poster_panda.jpg", sinopsisKungfu, "1j 34m", castKungfu));
            schedules.add(new Showtime("Kung Fu Panda 4", "17:00", 55000, "poster_panda.jpg", sinopsisKungfu, "1j 34m", castKungfu));
        }
    }

    
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_SCHEDULES))) {
            oos.writeObject(schedules);
        } catch (IOException e) { e.printStackTrace(); }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_TICKETS))) {
            oos.writeObject(tickets);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        File fSch = new File(FILE_SCHEDULES);
        if (fSch.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fSch))) {
                schedules = (List<Showtime>) ois.readObject();
            } catch (Exception e) {}
        }
        File fTic = new File(FILE_TICKETS);
        if (fTic.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fTic))) {
                tickets = (List<Ticket>) ois.readObject();
            } catch (Exception e) {}
        }
    }
}