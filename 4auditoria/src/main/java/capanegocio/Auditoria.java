package capanegocio;

import accesodatos.Usuario;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alber
 */
public class Auditoria {

    private static Auditoria instance;
    private PrintWriter pw;

    public Auditoria() {
        try {
            pw = new PrintWriter(new FileWriter("auditoria.txt", true));
        } catch (IOException ex) {
            Logger.getLogger(Auditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Auditoria getInstance() {
        if (instance == null) {
            instance = new Auditoria();
        }
        return instance;
    }

    // fecha y hora -- accion -- resultado -- objeto implicado -- actor
    public void auditar(String accion, String resultado, String objeto, Usuario usuario) {
        if (usuario == null) {
            pw.println(new Date() + ";" + accion + ";" + resultado + ";" + objeto + ";" + "sin usuario loggado");
        } else {
            pw.println(new Date() + ";" + accion + ";" + resultado + ";" + objeto + ";" + usuario.getNombreUsuario());
        }
        pw.flush();
    }

    public void cerrarFichero(){
        pw.close();
    }
    
}
