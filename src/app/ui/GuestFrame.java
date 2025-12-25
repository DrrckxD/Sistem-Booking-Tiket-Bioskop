package app.ui;

import app.db.DataStorage;
import app.models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

public class GuestFrame extends JFrame {
    private User currentUser;

    private Showtime currentSelectedSchedule;
    private JPanel seatGridPanel;

    private final Color COL_BG = new Color(26, 26, 46);
    private final Color COL_ACCENT = new Color(111, 234, 246);
    private final Color COL_SEAT_EMPTY = new Color(68, 68, 81);
    private final Color COL_SEAT_OCCUPIED = new Color(231, 76, 60);

    public GuestFrame(User user) {
        this.currentUser = user;
        setTitle("Menu Pemesanan Tiket - Aurora Cinema");
        setSize(950, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COL_BG);


        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(COL_BG);

        JLabel lblTitle = new JLabel("FILM SEDANG TAYANG (Klik Poster untuk Pesan)", SwingConstants.CENTER);
        lblTitle.setForeground(COL_ACCENT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(15,0,15,0));
        centerPanel.add(lblTitle, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(createMovieGrid());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);


        JPanel footer = new JPanel();
        footer.setBackground(COL_BG);
        JButton btnHistory = new JButton("RIWAYAT TIKET");
        styleButton(btnHistory, new Color(52, 152, 219), Color.WHITE);
        JButton btnLogout = new JButton("KELUAR");
        styleButton(btnLogout, new Color(149, 165, 166), Color.BLACK);

        footer.add(btnHistory);
        footer.add(btnLogout);
        add(footer, BorderLayout.SOUTH);

        btnHistory.addActionListener(e -> showHistoryTable());
        btnLogout.addActionListener(e -> { new LoginFrame(); dispose(); });

        setVisible(true);
    }


    private JPanel createMovieGrid() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        panel.setBackground(COL_BG);

        if (DataStorage.schedules.isEmpty()) {
            JLabel empty = new JLabel("Belum ada jadwal film.");
            empty.setForeground(Color.GRAY);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            panel.add(empty);
            return panel;
        }

        Set<String> displayedTitles = new HashSet<>();

        for (Showtime s : DataStorage.schedules) {
            if (displayedTitles.contains(s.getTitle())) continue;

            displayedTitles.add(s.getTitle());

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(35, 35, 55));
            card.setPreferredSize(new Dimension(200, 320));
            card.setBorder(BorderFactory.createLineBorder(COL_ACCENT, 1));


            JButton btnPoster = new JButton();
            btnPoster.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnPoster.setFocusPainted(false);
            btnPoster.setContentAreaFilled(false);
            btnPoster.setBorderPainted(false);
            btnPoster.setMargin(new Insets(0,0,0,0));

            try {
                File f = new File(s.getImagePath());
                ImageIcon icon;
                if(f.exists()) {
                    BufferedImage img = ImageIO.read(f);
                    Image scaled = img.getScaledInstance(198, 250, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaled);
                } else {
                    BufferedImage placeholder = new BufferedImage(198, 250, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = placeholder.createGraphics();
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(0,0, 198, 250);
                    g2.setColor(Color.WHITE);
                    g2.drawString("NO IMAGE", 70, 125);
                    g2.dispose();
                    icon = new ImageIcon(placeholder);
                }
                btnPoster.setIcon(icon);
            } catch (Exception e) {
                btnPoster.setText("ERROR");
                btnPoster.setForeground(Color.RED);
            }

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(new Color(35, 35, 55));
            infoPanel.setBorder(new EmptyBorder(10, 5, 10, 5));

            JLabel lblJudul = new JLabel(s.getTitle(), SwingConstants.CENTER);
            lblJudul.setForeground(Color.WHITE);
            lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel lblKlik = new JLabel("Klik untuk Pilih Jadwal", SwingConstants.CENTER);
            lblKlik.setForeground(Color.GRAY);
            lblKlik.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            infoPanel.add(lblJudul, BorderLayout.CENTER);
            infoPanel.add(lblKlik, BorderLayout.SOUTH);

            card.add(btnPoster, BorderLayout.CENTER);
            card.add(infoPanel, BorderLayout.SOUTH);

            btnPoster.addActionListener(e -> openBookingDialog(s));

            panel.add(card);
        }
        return panel;
    }


    private void openBookingDialog(Showtime initialShowtime) {
        List<Showtime> movieSchedules = new ArrayList<>();
        for(Showtime s : DataStorage.schedules) {
            if(s.getTitle().equals(initialShowtime.getTitle())) {
                movieSchedules.add(s);
            }
        }

        currentSelectedSchedule = initialShowtime;

        JDialog d = new JDialog(this, "Booking: " + initialShowtime.getTitle(), true);
        d.setSize(1000, 650);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());


        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(COL_BG);
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        ScreenPanel screen = new ScreenPanel();
        screen.setPreferredSize(new Dimension(400, 90));
        leftPanel.add(screen, BorderLayout.NORTH);


        seatGridPanel = new JPanel();
        seatGridPanel.setLayout(new BoxLayout(seatGridPanel, BoxLayout.Y_AXIS));
        seatGridPanel.setBackground(COL_BG);


        refreshSeatGrid(d);

        leftPanel.add(seatGridPanel, BorderLayout.CENTER);


        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(30, 30, 50));
        rightPanel.setPreferredSize(new Dimension(320, 650));
        rightPanel.setBorder(new EmptyBorder(25, 25, 25, 25));


        JLabel lblPoster = new JLabel();
        lblPoster.setAlignmentX(Component.LEFT_ALIGNMENT);
        try {
            File f = new File(initialShowtime.getImagePath());
            if(f.exists()) {
                Image img = ImageIO.read(f).getScaledInstance(140, 200, Image.SCALE_SMOOTH);
                lblPoster.setIcon(new ImageIcon(img));
            } else {
                lblPoster.setText("[NO IMAGE]");
                lblPoster.setForeground(Color.WHITE);
            }
        } catch(Exception e){}

        JLabel lblTitle = new JLabel("<html><h2 style='color:#6feaf6'>"+initialShowtime.getTitle()+"</h2></html>");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMeta = new JLabel("<html><body style='width: 250px; color:white;'>" +
                "<b>Durasi:</b> " + initialShowtime.getDuration() + "<br>" +
                "<b>Cast:</b> " + initialShowtime.getCast() +
                "</body></html>");
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMeta.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel lblSinopsisHeader = new JLabel("<html><br><b>SINOPSIS:</b></html>");
        lblSinopsisHeader.setForeground(COL_ACCENT);
        lblSinopsisHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtSinopsis = new JTextArea(initialShowtime.getSynopsis());
        txtSinopsis.setLineWrap(true);
        txtSinopsis.setWrapStyleWord(true);
        txtSinopsis.setEditable(false);
        txtSinopsis.setBackground(new Color(30, 30, 50));
        txtSinopsis.setForeground(Color.LIGHT_GRAY);
        txtSinopsis.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JScrollPane scrollSinopsis = new JScrollPane(txtSinopsis);
        scrollSinopsis.setPreferredSize(new Dimension(280, 120));
        scrollSinopsis.setBorder(null);
        scrollSinopsis.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPilihJam = new JLabel("<html><br><b>PILIH JAM TAYANG:</b></html>");
        lblPilihJam.setForeground(COL_ACCENT);
        lblPilihJam.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<Showtime> comboSchedule = new JComboBox<>(movieSchedules.toArray(new Showtime[0]));
        comboSchedule.setSelectedItem(initialShowtime);
        comboSchedule.setMaximumSize(new Dimension(300, 35));
        comboSchedule.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboSchedule.setFont(new Font("Segoe UI", Font.BOLD, 14));


        comboSchedule.addActionListener(e -> {
            currentSelectedSchedule = (Showtime) comboSchedule.getSelectedItem();
            refreshSeatGrid(d);
        });


        rightPanel.add(lblPoster);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(lblMeta);
        rightPanel.add(lblSinopsisHeader);
        rightPanel.add(scrollSinopsis);
        rightPanel.add(lblPilihJam);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(comboSchedule);


        d.add(leftPanel, BorderLayout.CENTER);
        d.add(rightPanel, BorderLayout.EAST);
        d.setVisible(true);
    }


    private void refreshSeatGrid(JDialog parentDialog) {
        seatGridPanel.removeAll();

        JPanel grid = new JPanel(new GridLayout(5, 5, 12, 12));
        grid.setBackground(COL_BG);
        grid.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel lblPrice = new JLabel("HARGA TIKET: Rp " + currentSelectedSchedule.getPrice(), SwingConstants.CENTER);
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setBorder(new EmptyBorder(0,0,20,0));
        seatGridPanel.add(lblPrice);

        for(int r=0; r<5; r++) {
            for(int c=0; c<5; c++) {
                String seatCode = (char)('A' + r) + "" + (c+1);
                JButton btn = new JButton(seatCode);
                btn.setPreferredSize(new Dimension(50, 45));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                if(currentSelectedSchedule.seats[r][c]) {
                    btn.setBackground(COL_SEAT_OCCUPIED);
                    btn.setForeground(Color.WHITE);
                    btn.setEnabled(false);
                } else {
                    btn.setBackground(COL_SEAT_EMPTY);
                    btn.setForeground(Color.WHITE);
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    int finalR=r, finalC=c;


                    btn.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            btn.setBackground(Color.GRAY);
                        }
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            btn.setBackground(COL_SEAT_EMPTY);
                        }
                    });


                    btn.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(parentDialog,
                                "Film: " + currentSelectedSchedule.getTitle() + "\n" +
                                        "Jam: " + currentSelectedSchedule.getTime() + "\n" +
                                        "Kursi: " + seatCode + "\n" +
                                        "Harga: Rp " + currentSelectedSchedule.getPrice() + "\n\n" +
                                        "Lanjutkan Pembayaran?",
                                "Konfirmasi Booking", JOptionPane.YES_NO_OPTION);

                        if(confirm == JOptionPane.YES_OPTION) {
                            currentSelectedSchedule.seats[finalR][finalC] = true;

                            Ticket t = new Ticket(
                                    currentUser.getUsername(),
                                    currentSelectedSchedule.getTitle(),
                                    currentSelectedSchedule.getTime(),
                                    seatCode
                            );
                            DataStorage.tickets.add(t);


                            DataStorage.saveData();

                            t.printTxT();
                            JOptionPane.showMessageDialog(parentDialog, "Pembayaran Berhasil!\nTiket telah dicetak.");

                            refreshSeatGrid(parentDialog);
                        }
                    });
                }
                grid.add(btn);
            }
        }
        seatGridPanel.add(grid);


        JLabel legend = new JLabel("MERAH: Terisi   |   ABU-ABU: Tersedia", SwingConstants.CENTER);
        legend.setForeground(Color.GRAY);
        seatGridPanel.add(legend);


        seatGridPanel.revalidate();
        seatGridPanel.repaint();
    }



    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 60));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblLogo = new JLabel("LUMINA CINEMA");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(COL_ACCENT);

        try {
            File f = new File("logo.png");
            if(f.exists()) {
                Image img = ImageIO.read(f).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
                lblLogo.setText("");
            }
        } catch(Exception e){}

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        JLabel lblUser = new JLabel("Hi, " + currentUser.getUsername().toUpperCase());
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoPanel.add(lblUser);

        panel.add(lblLogo, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.EAST);
        return panel;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    class ScreenPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(), h=getHeight();

            g2d.setColor(COL_BG); g2d.fillRect(0,0,w,h);

            GeneralPath shape = new GeneralPath();
            shape.moveTo(60, 20); shape.lineTo(w-60, 20);
            shape.lineTo(w-20, 35); shape.lineTo(20, 35);
            shape.closePath();

            g2d.setColor(Color.WHITE); g2d.fill(shape);
            g2d.setColor(COL_ACCENT); g2d.setStroke(new BasicStroke(3)); g2d.draw(shape);

            GradientPaint glow = new GradientPaint(w/2, 35, new Color(111,234,246,80), w/2, h, new Color(26,26,46,0));
            g2d.setPaint(glow);
            g2d.fillPolygon(new int[]{20, w-20, w-100, 100}, new int[]{35, 35, h, h}, 4);

            g2d.setColor(Color.BLACK); g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("LAYAR", w/2 - 20, 30);
        }
    }

    private void showHistoryTable() {
        JDialog d = new JDialog(this, "Riwayat Tiket", true);
        String[] cols = {"Film", "Jam", "Kursi"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for(Ticket t : DataStorage.tickets) {
            if(t.getOwner().equals(currentUser.getUsername())) {
                model.addRow(new Object[]{t.getMovie(), t.getTime(), t.getSeat()});
            }
        }
        d.add(new JScrollPane(new JTable(model)));
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }
}