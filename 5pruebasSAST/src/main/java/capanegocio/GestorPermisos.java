package capanegocio;

import accesodatos.Usuario;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author alber
 */
public class GestorPermisos {

    private static GestorPermisos instance;
    private HashMap<String, ArrayList<String>> rolesNecesarios;
    private ArrayList<Usuario> usuariosRegistrados;

    public static GestorPermisos getInstance() {
        if (instance == null) {
            instance = new GestorPermisos();
        }
        return instance;
    }

    public GestorPermisos() {
        rolesNecesarios = new HashMap<>();
        usuariosRegistrados = new ArrayList<>();
//        usuariosRegistrados.add(new Admin("admin1", "admin"));
        ArrayList<String> rolesAgregarContacto = new ArrayList<>();//hacer lo mismo con todas las operaciones
        rolesAgregarContacto.add("administrador");
        rolesAgregarContacto.add("gestor");
        rolesNecesarios.put("agregar_contacto", rolesAgregarContacto);//hacer lo mismo con todas las operaciones

        ArrayList<String> rolesModificarContacto = new ArrayList<>();
        rolesModificarContacto.add("administrador");
        rolesModificarContacto.add("gestor");
        rolesNecesarios.put("modificar_contacto", rolesModificarContacto);

        ArrayList<String> rolesEliminarContacto = new ArrayList<>();
        rolesEliminarContacto.add("administrador");
        rolesNecesarios.put("eliminar_contacto", rolesEliminarContacto);

        ArrayList<String> roleslistarContactos = new ArrayList<>();
        roleslistarContactos.add("administrador");
        roleslistarContactos.add("gestor");
        roleslistarContactos.add("asistente");
        rolesNecesarios.put("listar_contactos", roleslistarContactos);

        ArrayList<String> rolesAgregarUsuario = new ArrayList<>();
        rolesAgregarUsuario.add("administrador");
        rolesNecesarios.put("agregar_usuario", rolesAgregarUsuario);
    }

    public void agregarRolAOperacion(String operacion, String rol) {
        rolesNecesarios.get(operacion).add(rol);
    }

    public boolean estaAutorizado(String operacion, Usuario usuario) {
        boolean autorizado = false;
        if (usuario != null) {
            ArrayList<String> rolesOperacion = rolesNecesarios.get(operacion);
            for (int i = 0; i < rolesOperacion.size() && !autorizado; i++) {
                if (rolesOperacion.get(i).equals(usuario.getRol())) {
                    autorizado = true;
                }
            }
        }
        return autorizado;
    }

    /**
     * @return the usuariosRegistrados
     */
    public ArrayList<Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    /**
     * @param usuariosRegistrados the usuariosRegistrados to set
     */
    public void setUsuariosRegistrados(ArrayList<Usuario> usuariosRegistrados) {
        this.usuariosRegistrados = usuariosRegistrados;
    }

    public boolean agregarUsuario(Usuario usuario) {
        if (this.usuariosRegistrados.contains(usuario)) {
            return false;
        } else {
            this.usuariosRegistrados.add(usuario);
            return true;
        }

    }

}
