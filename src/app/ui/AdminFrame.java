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

    JTextField txtTitle = new JTextField(20);
    JTextField txtTime = new JTextField(10);
    JTextField txtPrice = new JTextField(15);
    JTextField txtImage = new JTextField(20);
    JTextField txtDuration = new JTextField(10);
    JTextField txtCast = new JTextField(30);
    JTextArea txtSynopsis = new JTextArea(4, 20);

    JCheckBox chkMultiAdd = new JCheckBox("Mode Input Banyak Jam");

    public AdminFrame() {
        setTitle("Dashboard Administrator");
        setSize(950, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(192, 57, 43));
        header.add(new JLabel("<html><h2 style='color:white'>KELOLA DATA FILM & JADWAL</h2></html>"));
        add(header, BorderLayout.NORTH);

        String[] columns = {"Judul Film", "Jam", "Harga", "Durasi"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Film Aktif"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Data Film"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; inputPanel.add(new JLabel("Judul:"), gbc);
        gbc.gridx=1; gbc.weightx=1.0; inputPanel.add(txtTitle, gbc);
        gbc.gridx=2; gbc.weightx=0; inputPanel.add(new JLabel("Jam:"), gbc);
        gbc.gridx=3; gbc.weightx=0.3; inputPanel.add(txtTime, gbc);

        gbc.gridx=0; gbc.gridy=1; inputPanel.add(new JLabel("Harga:"), gbc);
        gbc.gridx=1; inputPanel.add(txtPrice, gbc);
        gbc.gridx=2; inputPanel.add(new JLabel("Durasi:"), gbc);
        gbc.gridx=3; inputPanel.add(txtDuration, gbc);

        gbc.gridx=0; gbc.gridy=2; inputPanel.add(new JLabel("Cast:"), gbc);
        gbc.gridx=1; gbc.gridwidth=3; inputPanel.add(txtCast, gbc);

        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=1; inputPanel.add(new JLabel("Gambar:"), gbc);
        JPanel imgContainer = new JPanel(new BorderLayout(5,0));
        imgContainer.add(txtImage, BorderLayout.CENTER);
        JButton btnBrowse = new JButton("...");
        btnBrowse.addActionListener(e -> browseImage());
        imgContainer.add(btnBrowse, BorderLayout.EAST);
        gbc.gridx=1; gbc.gridwidth=3; inputPanel.add(imgContainer, gbc);

        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=1; gbc.anchor = GridBagConstraints.NORTHWEST;
        inputPanel.add(new JLabel("Sinopsis:"), gbc);
        gbc.gridx=1; gbc.gridwidth=3; inputPanel.add(new JScrollPane(txtSynopsis), gbc);

        gbc.gridx=1; gbc.gridy=5; gbc.gridwidth=3; inputPanel.add(chkMultiAdd, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Simpan");
        JButton btnClear = new JButton("Bersihkan");
        JButton btnEdit = new JButton("Update");
        JButton btnDel = new JButton("Hapus");

        JButton btnResetTickets = new JButton("RESET TIKET");
        JButton btnResetAll = new JButton("RESET DB");
        JButton btnLogout = new JButton("LOGOUT");

        styleButton(btnAdd, new Color(40, 167, 69));
        styleButton(btnClear, Color.GRAY);
        styleButton(btnEdit, new Color(255, 193, 7));
        styleButton(btnDel, new Color(220, 53, 69));

        btnResetTickets.setBackground(new Color(255, 140, 0));
        btnResetTickets.setForeground(Color.WHITE);
        btnResetTickets.setFocusPainted(false);

        btnResetAll.setBackground(Color.BLACK);
        btnResetAll.setForeground(Color.RED);
        btnResetAll.setFocusPainted(false);


        btnLogout.setBackground(new Color(52, 73, 94));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        actionPanel.add(btnAdd); actionPanel.add(btnClear);
        actionPanel.add(btnEdit); actionPanel.add(btnDel);
        actionPanel.add(Box.createHorizontalStrut(15));
        actionPanel.add(btnResetTickets);
        actionPanel.add(btnResetAll);
        actionPanel.add(Box.createHorizontalStrut(15));
        actionPanel.add(btnLogout);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputPanel, BorderLayout.CENTER);
        bottom.add(actionPanel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);


        btnAdd.addActionListener(e -> {
            try {
                String img = txtImage.getText().isEmpty() ? "placeholder.png" : txtImage.getText();
                Showtime s = new Showtime(
                        txtTitle.getText(), txtTime.getText(), Integer.parseInt(txtPrice.getText()),
                        img, txtSynopsis.getText(), txtDuration.getText(), txtCast.getText()
                );
                DataStorage.schedules.add(s);
                DataStorage.saveData();
                refreshTable();

                if(chkMultiAdd.isSelected()) {
                    txtTime.setText(""); txtTime.requestFocus();
                } else {
                    clearForm();
                }
                JOptionPane.showMessageDialog(this, "Berhasil disimpan!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Cek input!"); }
        });

        btnEdit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                try {
                    Showtime s = new Showtime(
                            txtTitle.getText(), txtTime.getText(), Integer.parseInt(txtPrice.getText()),
                            txtImage.getText(), txtSynopsis.getText(), txtDuration.getText(), txtCast.getText()
                    );
                    DataStorage.schedules.set(r, s);
                    DataStorage.saveData();
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Updated!");
                    if(!chkMultiAdd.isSelected()) clearForm();
                } catch(Exception ex) {}
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                Showtime s = DataStorage.schedules.get(r);
                txtTitle.setText(s.getTitle()); txtTime.setText(s.getTime());
                txtPrice.setText(s.getPrice()+""); txtImage.setText(s.getImagePath());
                txtDuration.setText(s.getDuration()); txtCast.setText(s.getCast());
                txtSynopsis.setText(s.getSynopsis());
            }
        });

        btnDel.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                DataStorage.schedules.remove(r);
                DataStorage.saveData();
                refreshTable();
                clearForm();
            }
        });

        btnClear.addActionListener(e -> clearForm());

        btnResetTickets.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus SEMUA RIWAYAT TIKET? Kursi akan kosong kembali.",
                    "Reset Tiket", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                DataStorage.tickets.clear();
                for(Showtime s : DataStorage.schedules) s.seats = new boolean[5][5];
                DataStorage.saveData();
                JOptionPane.showMessageDialog(this, "Kursi berhasil dikosongkan!");
            }
        });

        btnResetAll.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "PERINGATAN: Hapus SEMUA DATA (Film & Tiket)?",
                    "Reset Database Total", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                DataStorage.schedules.clear();
                DataStorage.tickets.clear();
                File f1 = new File("data_jadwal.dat");
                File f2 = new File("data_tiket.dat");
                if(f1.exists()) f1.delete();
                if(f2.exists()) f2.delete();
                DataStorage.saveData();
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Aplikasi telah di-reset total.");
            }
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void browseImage() {
        JFileChooser fc = new JFileChooser();
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtImage.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    private void clearForm() {
        txtTitle.setText(""); txtTime.setText(""); txtPrice.setText("");
        txtImage.setText(""); txtDuration.setText(""); txtCast.setText(""); txtSynopsis.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for(Showtime s : DataStorage.schedules) {
            tableModel.addRow(new Object[]{s.getTitle(), s.getTime(), "Rp " + s.getPrice(), s.getDuration()});
        }
    }

    private void styleButton(JButton btn, Color c) {
        btn.setBackground(c); btn.setForeground(Color.WHITE); btn.setFocusPainted(false);
    }
}