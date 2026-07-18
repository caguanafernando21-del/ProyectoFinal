
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class App {
    public static void main(String[] args) throws Exception {

        JFrame frmVentana = new JFrame();
        
        // Paneles
        JPanel pnlPanelPrincipal = new JPanel();
        JPanel pnlPanelDos = new JPanel();
        JPanel pnlPanelTres = new JPanel();
        JPanel pnlDondeColocarTodo = new JPanel();

        // Las barras que apareceren en la pantalla
        JMenuBar opciones = new JMenuBar();
        JMenuBar opcionesDos = new JMenuBar();

        // Lo que va dentro de las barras
        JMenu finalme = new JMenu("Opciones");
        JMenu otro = new JMenu("Opciones extras");

        // Opciones a seleccionar el boton/menu opcion
        JMenuItem BFS  = new JMenu("Metodo de busqueda BFS");
        JMenuItem DFS = new JMenu("Metodo de busqueda DFS");

        // Opciones de la segunda parte
        JMenuItem temperatura = new JMenuItem("Temperatura");
        JMenuItem salir = new JMenu("Salir");

        // instanciar para poder usar el borderLayout
        pnlPanelPrincipal.setLayout(new BorderLayout());
        pnlDondeColocarTodo.add(Box.createVerticalStrut(20), BorderLayout.NORTH);



        // opciones del primer panel
        finalme.add(DFS);
        finalme.add(BFS);
        // opciones del segundo panel
        otro.add(temperatura);
        otro.add(salir);

        // Agregar los menus a la barra
        opciones.add(finalme);
        opcionesDos.add(otro);
        // agregar el panel la barra
        pnlPanelDos.add(opciones);
        pnlPanelTres.add(opcionesDos);

        
        pnlDondeColocarTodo.setLayout(new BoxLayout(pnlDondeColocarTodo, BoxLayout.Y_AXIS));

        pnlDondeColocarTodo.add(pnlPanelDos);     // Opciones
        pnlDondeColocarTodo.add(Box.createVerticalStrut(20)); // Espacio
        pnlDondeColocarTodo.add(pnlPanelTres);    // ...
        pnlPanelPrincipal.add(pnlDondeColocarTodo, BorderLayout.WEST);
        
        
        frmVentana.add(pnlPanelPrincipal);

        
        
            // opciones
        salir.addActionListener(e-> {
            System.exit(0);
        });
            


     

    

        frmVentana.setSize(400,400);
        frmVentana.setVisible(true);
        frmVentana.setTitle("Proyecto");

    }
}
