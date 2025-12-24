package app.ui;

import app.db.DataStorage;
import app.models.Showtime;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    public AdminFrame() {
        setTitle("Dashboard Administrator");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. HEADER
        JPanel header = new JPanel();
        header.setBackground(new Color(192, 57, 43)); // Dark Red
        JLabel lblHeader = new JLabel("KELOLA DATA FILM & JADWAL");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(lblHeader);
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(header, BorderLayout.NORTH);

        // 2. CENTER - TABEL
        // Kolom Tabel
        String[] columns = {"Judul Film", "Jam Tayang", "Harga"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Film Aktif"));
        add(scrollPane, BorderLayout.CENTER);

        // 3. SOUTH - FORM INPUT
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Tambah / Hapus Jadwal", TitledBorder.LEFT, TitledBorder.TOP));

        JTextField txtTitle = new JTextField();
        JTextField txtTime = new JTextField();
        JTextField txtPrice = new JTextField();

        inputPanel.add(new JLabel("Judul Film:")); inputPanel.add(txtTitle);
        inputPanel.add(new JLabel("Waktu (HH:MM):")); inputPanel.add(txtTime);
        inputPanel.add(new JLabel("Harga Tiket:")); inputPanel.add(txtPrice);

        // PANEL TOMBOL AKSI
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Simpan");
        JButton btnDel = new JButton("Hapus Terpilih");
        JButton btnLogout = new JButton("Logout");

        // Styling Button
        btnAdd.setBackground(new Color(40, 167, 69)); btnAdd.setForeground(Color.WHITE);
        btnDel.setBackground(new Color(220, 53, 69)); btnDel.setForeground(Color.WHITE);

        actionPanel.add(btnAdd); actionPanel.add(btnDel); actionPanel.add(btnLogout);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(inputPanel, BorderLayout.CENTER);
        bottomContainer.add(actionPanel, BorderLayout.SOUTH);
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(bottomContainer, BorderLayout.SOUTH);

        // --- LOGIC ---
        btnAdd.addActionListener(e -> {
            try {
                String t = txtTitle.getText();
                String jam = txtTime.getText();
                int hrg = Integer.parseInt(txtPrice.getText());
                DataStorage.schedules.add(new Showtime(t, jam, hrg));
                refreshTable();
                txtTitle.setText(""); txtTime.setText(""); txtPrice.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Input Harga harus angka!"); }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) {
                DataStorage.schedules.remove(row);
                refreshTable();
            }
        });

        btnLogout.addActionListener(e -> { new LoginFrame(); dispose(); });

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Reset data
        for(Showtime s : DataStorage.schedules) {
            tableModel.addRow(new Object[]{s.getTitle(), s.getTime(), "Rp " + s.getPrice()});
        }
    }
}