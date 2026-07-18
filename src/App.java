
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class App {
    public static void main(String[] args) throws Exception {

        JFrame frmVentana = new JFrame();
        JPanel pnlPanelUno = new JPanel();
        JMenuBar opciones = new JMenuBar();

        JMenu finalme = new JMenu("Opciones");
        JMenuItem BFS    = new JMenu("Metodo de busqueda BFS");
        JMenuItem DFS = new JMenu("Metodo de busqueda DFS");
        JMenuItem salir = new JMenu("Salir");

        finalme.add(DFS);
        finalme.add(BFS);
        finalme.add(salir);
        opciones.add(finalme);
        
        frmVentana.setJMenuBar(opciones);

        
        
            // opciones
        
            

        salir.addActionListener(e->{
                System.exit(0);
            });

        


        frmVentana.setSize(400,400);
        frmVentana.setVisible(true);
        frmVentana.setTitle("Proyecto");

    }
}
