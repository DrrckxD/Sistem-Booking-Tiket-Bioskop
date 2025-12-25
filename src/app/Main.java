package app;
import app.db.DataStorage;
import app.ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DataStorage.loadData();

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}