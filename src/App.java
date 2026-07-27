import javax.swing.SwingUtilities;
import view.MainFrame;

public class App {
    public static void main(String[] args) {
        //Iniciar la aplicación
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}