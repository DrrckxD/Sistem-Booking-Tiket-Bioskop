package app.ui;

import app.db.DataStorage;
import app.models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GuestFrame extends JFrame {
    private User currentUser;
    private JComboBox<Showtime> comboMovie;

    public GuestFrame(User user) {
        this.currentUser = user;
        setTitle("Menu Pemesanan Tiket");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        JLabel lblWelcome = new JLabel("Halo, " + user.getUsername(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBorder(new EmptyBorder(15,0,15,0));
        header.add(lblWelcome, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // CENTER (Panel Pilihan)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboMovie = new JComboBox<>();
        refreshCombo();

        JButton btnSeat = new JButton("PILIH KURSI (BOOKING)");
        btnSeat.setBackground(new Color(39, 174, 96));
        btnSeat.setForeground(Color.WHITE);
        btnSeat.setFont(new Font("Arial", Font.BOLD, 14));
        btnSeat.setPreferredSize(new Dimension(250, 50));

        JButton btnHistory = new JButton("LIHAT RIWAYAT TIKET");
        btnHistory.setBackground(new Color(52, 152, 219));
        btnHistory.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);

        gbc.gridx=0; gbc.gridy=0;
        mainPanel.add(new JLabel("Silahkan Pilih Film:"), gbc);
        gbc.gridy=1;
        mainPanel.add(comboMovie, gbc);
        gbc.gridy=2;
        mainPanel.add(btnSeat, gbc);
        gbc.gridy=3;
        mainPanel.add(btnHistory, gbc);
        gbc.gridy=4;
        mainPanel.add(btnLogout, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // ACTIONS
        btnSeat.addActionListener(e -> {
            Showtime s = (Showtime) comboMovie.getSelectedItem();
            if(s != null) openSeatPicker(s);
        });

        btnHistory.addActionListener(e -> showHistoryTable());

        btnLogout.addActionListener(e -> { new LoginFrame(); dispose(); });

        setVisible(true);
    }

    private void refreshCombo() {
        comboMovie.removeAllItems();
        for(Showtime s : DataStorage.schedules) comboMovie.addItem(s);
    }

    // POPUP PILIH KURSI (GRID)
    private void openSeatPicker(Showtime s) {
        JDialog d = new JDialog(this, "Pilih Kursi: " + s.getTitle(), true);
        d.setLayout(new BorderLayout());

        // Layar
        JLabel screen = new JLabel("--- LAYAR BIOSKOP ---", SwingConstants.CENTER);
        screen.setOpaque(true);
        screen.setBackground(Color.DARK_GRAY);
        screen.setForeground(Color.WHITE);
        screen.setPreferredSize(new Dimension(400, 30));
        d.add(screen, BorderLayout.NORTH);

        // Grid Kursi
        JPanel grid = new JPanel(new GridLayout(5, 5, 5, 5));
        grid.setBorder(new EmptyBorder(20,20,20,20));

        for(int r=0; r<5; r++) {
            for(int c=0; c<5; c++) {
                String seatCode = (char)('A' + r) + "" + (c+1);
                JButton btn = new JButton(seatCode);

                if(s.seats[r][c]) {
                    btn.setBackground(new Color(231, 76, 60)); // Merah
                    btn.setEnabled(false);
                } else {
                    btn.setBackground(new Color(46, 204, 113)); // Hijau
                    int finalR=r, finalC=c;
                    btn.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(d, "Booking kursi " + seatCode + "?");
                        if(confirm == JOptionPane.YES_OPTION) {
                            s.seats[finalR][finalC] = true;
                            Ticket t = new Ticket(currentUser.getUsername(), s.getTitle(), s.getTime(), seatCode);
                            DataStorage.tickets.add(t);
                            t.printTxT();
                            JOptionPane.showMessageDialog(d, "Sukses! Tiket dicetak ke file TXT.");
                            d.dispose();
                        }
                    });
                }
                grid.add(btn);
            }
        }
        d.add(grid, BorderLayout.CENTER);
        d.setSize(400, 400);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    // POPUP RIWAYAT (TABEL)
    private void showHistoryTable() {
        JDialog d = new JDialog(this, "Riwayat Tiket Saya", true);
        String[] cols = {"Film", "Jam", "Kursi"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for(Ticket t : DataStorage.tickets) {
            if(t.getOwner().equals(currentUser.getUsername())) {
                model.addRow(new Object[]{t.getMovie(), t.getTime(), t.getSeat()});
            }
        }

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Total Tiket: " + model.getRowCount()));

        d.add(sp);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }
}