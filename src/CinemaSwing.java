import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// --- MODEL DATA ---
class Movie {
    String title;
    public Movie(String title) { this.title = title; }
    @Override public String toString() { return title; }
}

class Showtime {
    Movie movie;
    String time;
    boolean[][] seats = new boolean[5][5]; // false = kosong, true = terisi

    public Showtime(Movie movie, String time) {
        this.movie = movie;
        this.time = time;
    }
    @Override public String toString() { return movie.title + " (" + time + ")"; }
}

class Ticket {
    String user, movie, time, seat;
    public Ticket(String u, String m, String t, String s) {
        this.user = u; this.movie = m; this.time = t; this.seat = s;
    }
}

// --- MAIN UI ---
public class CinemaSwing extends JFrame {
    private JPanel cardPanel; // Untuk navigasi antar layar
    private CardLayout cl;

    // Data "Database"
    private List<Movie> movies = new ArrayList<>();
    private List<Showtime> schedules = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private String currentUser = "";

    public CinemaSwing() {
        setTitle("Sistem Tiket Bioskop - Java Swing");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inisialisasi Data Dummy
        movies.add(new Movie("Avengers: Endgame"));
        movies.add(new Movie("Spider-Man"));
        schedules.add(new Showtime(movies.get(0), "12:00"));
        schedules.add(new Showtime(movies.get(1), "15:00"));

        cl = new CardLayout();
        cardPanel = new JPanel(cl);

        initLoginScreen();
        initAdminScreen();
        initGuestScreen();

        add(cardPanel);
        setVisible(true);
    }

    // --- SCREEN: LOGIN ---
    private void initLoginScreen() {
        JPanel p = new JPanel(new GridLayout(4, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JTextField userField = new JTextField();
        JButton btnAdmin = new JButton("Login sebagai Admin");
        JButton btnGuest = new JButton("Login sebagai Guest");

        btnAdmin.addActionListener(e -> {
            currentUser = "Administrator";
            cl.show(cardPanel, "Admin");
        });

        btnGuest.addActionListener(e -> {
            currentUser = userField.getText().isEmpty() ? "Guest" : userField.getText();
            cl.show(cardPanel, "Guest");
        });

        p.add(new JLabel("Masukkan Nama:"));
        p.add(userField);
        p.add(btnAdmin);
        p.add(btnGuest);
        cardPanel.add(p, "Login");
    }

    // --- SCREEN: ADMIN (CRUD FILM) ---
    private void initAdminScreen() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Movie m : movies) listModel.addElement(m.title);
        JList<String> list = new JList<>(listModel);

        JButton btnDelete = new JButton("Hapus Film");
        btnDelete.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if(idx != -1) {
                movies.remove(idx);
                listModel.remove(idx);
            }
        });

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> cl.show(cardPanel, "Login"));

        p.add(new JLabel("Daftar Film (Admin Mode)"), BorderLayout.NORTH);
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.add(btnDelete);
        south.add(btnLogout);
        p.add(south, BorderLayout.SOUTH);

        cardPanel.add(p, "Admin");
    }

    // --- SCREEN: GUEST (BOOKING) ---
    private void initGuestScreen() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultListModel<Showtime> schedModel = new DefaultListModel<>();
        for (Showtime s : schedules) schedModel.addElement(s);
        JList<Showtime> list = new JList<>(schedModel);

        JButton btnBook = new JButton("Pilih Kursi");
        btnBook.addActionListener(e -> {
            Showtime selected = list.getSelectedValue();
            if(selected != null) showSeatPicker(selected);
        });

        JButton btnHistory = new JButton("Riwayat Tiket");
        btnHistory.addActionListener(e -> showHistory());

        JPanel south = new JPanel();
        south.add(btnBook);
        south.add(btnHistory);
        south.add(new JButton("Logout") {{ addActionListener(e -> cl.show(cardPanel, "Login")); }});

        p.add(new JLabel("Pilih Jadwal Film"), BorderLayout.NORTH);
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        cardPanel.add(p, "Guest");
    }

    // --- DIALOG: PEMILIHAN KURSI ---
    private void showSeatPicker(Showtime s) {
        JDialog dialog = new JDialog(this, "Pilih Kursi", true);
        dialog.setLayout(new BorderLayout());
        JPanel grid = new JPanel(new GridLayout(5, 5, 5, 5));



        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                String label = (char)('A'+r) + "" + (c+1);
                JButton btn = new JButton(label);

                if (s.seats[r][c]) {
                    btn.setBackground(Color.RED);
                    btn.setEnabled(false);
                } else {
                    btn.setBackground(Color.GREEN);
                    int finalR = r; int finalC = c;
                    btn.addActionListener(e -> {
                        s.seats[finalR][finalC] = true;
                        tickets.add(new Ticket(currentUser, s.movie.title, s.time, label));
                        JOptionPane.showMessageDialog(dialog, "Berhasil pesan kursi " + label);
                        dialog.dispose();
                    });
                }
                grid.add(btn);
            }
        }
        dialog.add(grid, BorderLayout.CENTER);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // --- DIALOG: RIWAYAT ---
    private void showHistory() {
        StringBuilder sb = new StringBuilder("Tiket Anda:\n");
        for(Ticket t : tickets) {
            if(t.user.equals(currentUser))
                sb.append("- ").append(t.movie).append(" (").append(t.time).append(") Kursi: ").append(t.seat).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CinemaSwing::new);
    }
}