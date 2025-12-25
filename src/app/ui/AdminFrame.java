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

    JTextField txtTitle = new JTextField();
    JTextField txtTime = new JTextField();
    JTextField txtPrice = new JTextField();
    JTextField txtImage = new JTextField();

    public AdminFrame() {
        setTitle("Dashboard Administrator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(192, 57, 43));
        JLabel lblHeader = new JLabel("KELOLA DATA FILM & JADWAL");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(lblHeader);
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(header, BorderLayout.NORTH);

        String[] columns = {"Judul Film", "Jam Tayang", "Harga", "File Gambar"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Film Aktif"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Form Kelola Jadwal", TitledBorder.LEFT, TitledBorder.TOP));

        inputPanel.add(new JLabel("Judul Film:")); inputPanel.add(txtTitle);
        inputPanel.add(new JLabel("Waktu (HH:MM):")); inputPanel.add(txtTime);
        inputPanel.add(new JLabel("Harga Tiket:")); inputPanel.add(txtPrice);

        JPanel imgContainer = new JPanel(new BorderLayout());
        imgContainer.add(txtImage, BorderLayout.CENTER);
        JButton btnBrowse = new JButton("...");
        btnBrowse.addActionListener(e -> browseImage());
        imgContainer.add(btnBrowse, BorderLayout.EAST);

        inputPanel.add(new JLabel("Path Gambar:")); inputPanel.add(imgContainer);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDel = new JButton("Hapus");
        JButton btnLogout = new JButton("Logout");

        btnAdd.setBackground(new Color(40, 167, 69)); btnAdd.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(255, 193, 7)); btnEdit.setForeground(Color.BLACK);
        btnDel.setBackground(new Color(220, 53, 69)); btnDel.setForeground(Color.WHITE);

        actionPanel.add(btnAdd); actionPanel.add(btnEdit); actionPanel.add(btnDel); actionPanel.add(btnLogout);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(inputPanel, BorderLayout.CENTER);
        bottomContainer.add(actionPanel, BorderLayout.SOUTH);
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(bottomContainer, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            try {
                String t = txtTitle.getText();
                String jam = txtTime.getText();
                int hrg = Integer.parseInt(txtPrice.getText());
                String img = txtImage.getText();
                if(img.isEmpty()) img = "placeholder.png";

                DataStorage.schedules.add(new Showtime(t, jam, hrg, img));
                DataStorage.saveData();
                refreshTable();
                clearForm();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Harga harus angka!"); }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) {
                Showtime s = DataStorage.schedules.get(row);
                try {
                    s.setTitle(txtTitle.getText());
                    s.setTime(txtTime.getText());
                    s.setPrice(Integer.parseInt(txtPrice.getText()));
                    s.setImagePath(txtImage.getText());

                    DataStorage.saveData();
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                    clearForm();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Pastikan format input benar!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit dulu!");
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) {
                Showtime s = DataStorage.schedules.get(row);
                txtTitle.setText(s.getTitle());
                txtTime.setText(s.getTime());
                txtPrice.setText(String.valueOf(s.getPrice()));
                txtImage.setText(s.getImagePath());
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) {
                DataStorage.schedules.remove(row);
                DataStorage.saveData();
                refreshTable();
                clearForm();
            }
        });

        btnLogout.addActionListener(e -> { new LoginFrame(); dispose(); });

        setVisible(true);
    }

    private void browseImage() {
        JFileChooser fc = new JFileChooser();
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtImage.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    private void clearForm() {
        txtTitle.setText(""); txtTime.setText(""); txtPrice.setText(""); txtImage.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for(Showtime s : DataStorage.schedules) {
            tableModel.addRow(new Object[]{s.getTitle(), s.getTime(), "Rp " + s.getPrice(), s.getImagePath()});
        }
    }
}