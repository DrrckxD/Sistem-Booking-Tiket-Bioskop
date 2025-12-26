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