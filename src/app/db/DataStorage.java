package app.db;
import app.models.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static List<User> users = new ArrayList<>();
    public static List<Showtime> schedules = new ArrayList<>();
    public static List<Ticket> tickets = new ArrayList<>();

    static {
        users.add(new User("admin", "admin", "ADMIN"));
        users.add(new User("guest", "guest", "GUEST"));
        schedules.add(new Showtime("Interstellar", "14:00", 50000));
        schedules.add(new Showtime("Inception", "19:00", 60000));
    }
}