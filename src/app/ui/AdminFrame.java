package app.ui;

import app.db.DataStorage;
import app.models.Showtime;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class AdminFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    // Komponen Input
    JTextField txtTitle = new JTextField(20);
    JTextField txtTime = new JTextField(10);
    JTextField txtPrice = new JTextField(15);
    JTextField txtImage = new JTextField(20);
    JTextField txtDuration = new JTextField(10);
    JTextField txtCast = new JTextField(30);
    JTextArea txtSynopsis = new JTextArea(4, 20);
    JCheckBox chkMultiAdd = new JCheckBox("Mode Input Banyak Jam (Gunakan koma: 14:00, 16:00)");

    public AdminFrame() {
        setTitle("Dashboard Administrator - Aurora Cinema");
        setSize(1000, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel();
        header.setBackground(new Color(192, 57, 43));
        header.add(new JLabel("<html><h2 style='color:white; padding:10px'>KELOLA DATA FILM & JADWAL</h2></html>"));
        add(header, BorderLayout.NORTH);

        // FORM INPUT
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(new TitledBorder("Input Data Film"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addFormField(leftPanel, "Judul Film:", txtTitle, gbc, row++);
        addFormField(leftPanel, "Jam Tayang:", txtTime, gbc, row++);
        addFormField(leftPanel, "Harga Tiket:", txtPrice, gbc, row++);
        addFormField(leftPanel, "Durasi:", txtDuration, gbc, row++);
        addFormField(leftPanel, "Pemain:", txtCast, gbc, row++);

        gbc.gridy = row++; gbc.gridx = 0;
        leftPanel.add(new JLabel("Sinopsis:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(new JScrollPane(txtSynopsis), gbc);

        gbc.gridy = row++; gbc.gridx = 1;
        leftPanel.add(chkMultiAdd, gbc);

        // BUTTONS
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("TAMBAH JADWAL");
        JButton btnDelete = new JButton("HAPUS TERPILIH");
        JButton btnResetAll = new JButton("RESET SEMUA DATA (TXT & DAT)");
        JButton btnLogout = new JButton("LOGOUT");

        styleButton(btnAdd, new Color(46, 204, 113));
        styleButton(btnDelete, new Color(230, 126, 34));
        styleButton(btnResetAll, Color.BLACK);
        styleButton(btnLogout, Color.GRAY);

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnResetAll);
        btnPanel.add(btnLogout);

        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 2;
        leftPanel.add(btnPanel, gbc);

        add(leftPanel, BorderLayout.WEST);

        // TABLE
        String[] columns = {"Judul Film", "Jam", "Harga", "Durasi"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // LOGIKA BUTTONS
        btnAdd.addActionListener(e -> {
            try {
                String title = txtTitle.getText();
                int price = Integer.parseInt(txtPrice.getText());
                String duration = txtDuration.getText();
                String cast = txtCast.getText();
                String synopsis = txtSynopsis.getText();

                if(chkMultiAdd.isSelected()) {
                    String[] times = txtTime.getText().split(",");
                    for(String t : times) {
                        DataStorage.schedules.add(new Showtime(title, t.trim(), price, "", synopsis, duration, cast));
                    }
                } else {
                    DataStorage.schedules.add(new Showtime(title, txtTime.getText(), price, "", synopsis, duration, cast));
                }

                DataStorage.saveData();
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Jadwal berhasil ditambahkan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid!");
            }
        });

        btnDelete.addActionListener(e -> {
            int rowIdx = table.getSelectedRow();
            if(rowIdx != -1) {
                DataStorage.schedules.remove(rowIdx);
                DataStorage.saveData();
                refreshTable();
            }
        });

        // LOGIKA RESET (ALUR TXT)
        btnResetAll.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus SEMUA data jadwal, tiket di sistem (.dat), dan riwayat di notepad (.txt)?",
                    "Reset Total", JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION) {
                // 1. Bersihkan List di memori
                DataStorage.schedules.clear();
                DataStorage.tickets.clear();

                // 2. Hapus file-file fisik
                File f1 = new File("data_jadwal.dat");
                File f2 = new File("data_tiket.dat");
                File f3 = new File("riwayat_transaksi.txt"); // Menghapus file riwayat TXT

                if(f1.exists()) f1.delete();
                if(f2.exists()) f2.delete();
                if(f3.exists()) f3.delete();

                // 3. Simpan state kosong dan refresh UI
                DataStorage.saveData();
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Semua database dan file riwayat TXT telah dihapus.");
            }
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        refreshTable();
        setVisible(true);
    }

    private void addFormField(JPanel p, String label, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(comp, gbc);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void clearForm() {
        txtTitle.setText(""); txtTime.setText(""); txtPrice.setText("");
        txtDuration.setText(""); txtCast.setText(""); txtSynopsis.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for(Showtime s : DataStorage.schedules) {
            tableModel.addRow(new Object[]{s.getTitle(), s.getTime(), "Rp " + s.getPrice(), s.getDuration()});
        }
    }
}