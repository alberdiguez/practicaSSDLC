package accesodatos;

import accesodatos.Usuario;

/**
 *
 * @author alber
 */
public class Admin extends Usuario{

    public Admin(String nombreUsuario, String password) {
        super(nombreUsuario,"administrador",password);
    }


    
}
