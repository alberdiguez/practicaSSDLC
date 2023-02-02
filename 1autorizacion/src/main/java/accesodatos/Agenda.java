package accesodatos;

import java.util.ArrayList;

/**
 *
 * @author alber
 */
public class Agenda {

    private ArrayList<Contacto> contactos;

    public Agenda() {
        contactos = new ArrayList<>();
    }

    public boolean addContacto(Contacto contacto) {
        if (this.contactos.contains(contacto)) {
            return false;
        } else {
            contactos.add(contacto);
            return true;
        }

    }

    public boolean modifyContacto(int idContacto, String nombre, String apellidos, String direccion, String tlfno) {
        boolean modificado = false;
        if (idContacto - 1 < contactos.size()) {
            modificado = true;
            contactos.get(idContacto - 1).setNombre(nombre);
            contactos.get(idContacto - 1).setApellidos(apellidos);
            contactos.get(idContacto - 1).setDireccion(direccion);
            contactos.get(idContacto - 1).setTelefono(tlfno);
        }
        return modificado;
    }

    public boolean eliminarContacto(int idContacto) {
        boolean eliminado = false;
        if (idContacto - 1 < contactos.size()) {
            eliminado = true;
            contactos.remove(idContacto - 1);
        }
        return eliminado;
    }

    public void listContactos() {
        for (int i = 0; i < contactos.size(); i++) {
            System.out.println((i + 1) + ". " + contactos.get(i));
        }
    }

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }
    
}
