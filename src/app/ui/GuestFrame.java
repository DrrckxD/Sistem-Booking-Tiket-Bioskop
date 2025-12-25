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
import javax.imageio.ImageIO;

public class GuestFrame extends JFrame {
    private User currentUser;

    private final Color COL_BG = new Color(26, 26, 46);
    private final Color COL_ACCENT = new Color(111, 234, 246);
    private final Color COL_SEAT_EMPTY = new Color(68, 68, 81);
    private final Color COL_SEAT_OCCUPIED = new Color(231, 76, 60);

    public GuestFrame(User user) {
        this.currentUser = user;
        setTitle("Menu Pemesanan Tiket - Aurora Cinema");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COL_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(COL_BG);

        JLabel lblTitle = new JLabel("FILM SEDANG TAYANG (Klik Poster)", SwingConstants.CENTER);
        lblTitle.setForeground(COL_ACCENT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(10,0,10,0));
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

        for (Showtime s : DataStorage.schedules) {
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

            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
            infoPanel.setBackground(new Color(35, 35, 55));
            infoPanel.setBorder(new EmptyBorder(10, 5, 10, 5));

            JLabel lblJudul = new JLabel(s.getTitle(), SwingConstants.CENTER);
            lblJudul.setForeground(Color.WHITE);
            lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel lblInfo = new JLabel(s.getTime() + " | Rp " + s.getPrice(), SwingConstants.CENTER);
            lblInfo.setForeground(COL_ACCENT);
            lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            infoPanel.add(lblJudul);
            infoPanel.add(lblInfo);

            card.add(btnPoster, BorderLayout.CENTER);
            card.add(infoPanel, BorderLayout.SOUTH);


            btnPoster.addActionListener(e -> openSeatPicker(s));

            panel.add(card);
        }
        return panel;
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void openSeatPicker(Showtime s) {
        JDialog d = new JDialog(this, "Pilih Kursi: " + s.getTitle(), true);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(COL_BG);

        ScreenPanel screenPanel = new ScreenPanel();
        screenPanel.setPreferredSize(new Dimension(500, 110));
        d.add(screenPanel, BorderLayout.NORTH);

        JPanel gridWrapper = new JPanel();
        gridWrapper.setBackground(COL_BG);
        gridWrapper.setLayout(new BoxLayout(gridWrapper, BoxLayout.Y_AXIS));

        JPanel grid = new JPanel(new GridLayout(5, 5, 10, 10));
        grid.setBackground(COL_BG);
        grid.setBorder(new EmptyBorder(10, 40, 30, 40));

        for(int r=0; r<5; r++) {
            for(int c=0; c<5; c++) {
                String seatCode = (char)('A' + r) + "" + (c+1);
                JButton btn = new JButton(seatCode);
                btn.setPreferredSize(new Dimension(45, 40));

                if(s.seats[r][c]) {
                    btn.setBackground(COL_SEAT_OCCUPIED);
                    btn.setEnabled(false);
                } else {
                    btn.setBackground(COL_SEAT_EMPTY);
                    btn.setForeground(Color.WHITE);
                    int finalR=r, finalC=c;

                    btn.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(d,
                                "Booking " + seatCode + " seharga Rp " + s.getPrice() + "?",
                                "Konfirmasi", JOptionPane.YES_NO_OPTION);

                        if(confirm == JOptionPane.YES_OPTION) {
                            s.seats[finalR][finalC] = true;
                            Ticket t = new Ticket(currentUser.getUsername(), s.getTitle(), s.getTime(), seatCode);
                            DataStorage.tickets.add(t);

                            DataStorage.saveData();

                            t.printTxT();
                            JOptionPane.showMessageDialog(d, "Tiket berhasil dipesan!");
                            d.dispose();
                            openSeatPicker(s);
                        }
                    });
                }
                grid.add(btn);
            }
        }
        gridWrapper.add(grid);
        d.add(gridWrapper, BorderLayout.CENTER);
        d.setSize(500, 550);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
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