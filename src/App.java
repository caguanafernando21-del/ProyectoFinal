import javax.swing.SwingUtilities;
import controllers.MapController;
import persistance.FileGraphRepository;
import view.MainFrame;

public class App {
    public static void main(String[] args) {
        //Iniciar la aplicación
        SwingUtilities.invokeLater(() -> {
            // 1. Crear el repositorio de archivos
            FileGraphRepository repo = new FileGraphRepository();
            
            // 2. Crear el controlador del mapa pasándole el repositorio
            MapController controlador = new MapController(repo);
            
            // 3. Pasar el controlador a MainFrame para que construya toda la interfaz
            new MainFrame(controlador).setVisible(true);
        });
    }
}