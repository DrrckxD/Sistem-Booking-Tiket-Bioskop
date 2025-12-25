package app.ui;

import app.db.DataStorage;
import app.models.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Cinema Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(33, 37, 41));
        JLabel title = new JLabel("LUMINA SYSTEM LOGIN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(title);
        header.setBorder(new EmptyBorder(15,0,15,0));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JTextField userTxt = new JTextField();
        JPasswordField passTxt = new JPasswordField();

        formPanel.add(new JLabel("Username:")); formPanel.add(userTxt);
        formPanel.add(new JLabel("Password:")); formPanel.add(passTxt);
        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnLogin = new JButton("MASUK");
        btnLogin.setBackground(new Color(13, 110, 253));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(300, 40));

        btnLogin.addActionListener(e -> {
            String u = userTxt.getText();
            String p = new String(passTxt.getPassword());
            for(User user : DataStorage.users) {
                if(user.getUsername().equals(u) && user.getPassword().equals(p)) {
                    dispose();
                    if(user.getRole().equals("ADMIN")) new AdminFrame();
                    else new GuestFrame(user);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Login Gagal!");
        });

        btnPanel.add(btnLogin);
        btnPanel.setBorder(new EmptyBorder(0,0,20,0));
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}