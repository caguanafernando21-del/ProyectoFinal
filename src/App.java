import javax.swing.SwingUtilities;
import view.MainFrame;

public class App {
    public static void main(String[] args) {
        // Arranca la aplicación
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}