package capanegocio;

import accesodatos.Admin;
import accesodatos.Usuario;
import accesodatos.Agenda;
import accesodatos.Asistente;
import accesodatos.Contacto;
import accesodatos.Gestor;
import java.util.Scanner;

public class AgendaFacade {

    private static AgendaFacade instance;
    private Usuario usuarioLoggado;
    private Agenda agenda;
    private Scanner sc;

    public static AgendaFacade getInstance() {
        if (instance == null) {
            instance = new AgendaFacade();
        }
        return instance;
    }

    public AgendaFacade() {
        agenda = new Agenda();
        sc = new Scanner(System.in);
    }

    /*operaciones agregar contacto etc.*/
    public void agregarContacto() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("agregar_contacto", usuarioLoggado)) {
            System.out.println("Introduzca los datos del contacto");
            System.out.println("Introduzca nombre");
            String nombre = sc.nextLine();
            System.out.println("Introduzca apellidos");
            String apellidos = sc.nextLine();
            System.out.println("Introduzca direccion");
            String direccion = sc.nextLine();
            System.out.println("Introduzca numero de telefono");
            String tlfno = sc.nextLine();

            Contacto contacto = new Contacto(nombre, apellidos, direccion, tlfno);
            if (agenda.addContacto(contacto) == true) {
                System.out.println("Contacto añadido");
            } else {
                System.out.println("Ese contacto ya existe");
            }
        } else {
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void modificarContacto() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("modificar_contacto", usuarioLoggado)) {
            agenda.listContactos();
            System.out.println("Introduzca qué contacto desea modificar");
            int contactElegido = sc.nextInt();
            sc.nextLine();
            System.out.println("Introduzca los datos del contacto");
            System.out.println("Introduzca nombre");
            String nombre = sc.nextLine();
            System.out.println("Introduzca apellidos");
            String apellidos = sc.nextLine();
            System.out.println("Introduzca direccion");
            String direccion = sc.nextLine();
            System.out.println("Introduzca numero de telefono");
            String tlfno = sc.nextLine();
            if (agenda.modifyContacto(contactElegido, nombre, apellidos, direccion, tlfno) == true) {
                System.out.println("Contacto modificado");
            } else {
                System.out.println("Ese contacto no existe");
            }

        } else {
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void eliminarContacto() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("eliminar_contacto", usuarioLoggado)) {
            agenda.listContactos();
            System.out.println("Introduzca qué contacto desea eliminar");
            int contactElegido = sc.nextInt();
            sc.nextLine();

            if (agenda.eliminarContacto(contactElegido) == true) {
                System.out.println("Contacto " + contactElegido + " eliminado");
            } else {
                System.out.println("Ese contacto no existe");
            }
        } else {
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void listarContactos() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("listar_contactos", usuarioLoggado)) {
            agenda.listContactos();
        } else {
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void agregarUsuario() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("agregar_usuario", usuarioLoggado)) {
            boolean resultado = false;
            System.out.println("Introduzca los datos del nuevo usuario");
            System.out.println("Introduzca nombre de usuario");
            String usuario = sc.nextLine();
            System.out.println("Introduzca contraseña");
            String password = sc.nextLine();
            System.out.println("Elija el rol");
            System.out.println("1. Admin\n" + "2. Gestor\n" + "3. Asistente\n");
            int rol = sc.nextInt();
            switch (rol) {
                case 1:
                    resultado = GestorPermisos.getInstance().agregarUsuario(new Admin(usuario, password));
                    break;
                case 2:
                    resultado = GestorPermisos.getInstance().agregarUsuario(new Gestor(usuario, password));
                    break;
                case 3:
                    resultado = GestorPermisos.getInstance().agregarUsuario(new Asistente(usuario, password));
                    break;
                default:
                    throw new AssertionError();
            }
            if (resultado == true) {
                System.out.println("Usuario agregado corrrectamente");
            } else {
                System.out.println("Ese usuario ya existe");
            }
            sc.nextLine();
        } else {
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void logIn(String usuario, String password) {
        boolean encontrado = false;
        for (int i = 0; i < GestorPermisos.getInstance().getUsuariosRegistrados().size() && !encontrado; i++) {
            if (GestorPermisos.getInstance().getUsuariosRegistrados().get(i).getNombreUsuario().equals(usuario)) {
                encontrado = true;
                if (GestorPermisos.getInstance().getUsuariosRegistrados().get(i).getPassword().equals(password)) {
                    System.out.println("Log in correcto");
                    usuarioLoggado = GestorPermisos.getInstance().getUsuariosRegistrados().get(i);
                } else {
                    System.out.println("Nombre de usuario o contraseña incorrectos");
                    logOut();
                }
            }
        }
        if (!encontrado) {
            System.out.println("Nombre de usuario o contraseña incorrectos");
            logOut();
        }
    }

    public void logOut() {
        usuarioLoggado = null;
    }

    /**
     * @return the usuarioLoggado
     */
    public Usuario getUsuarioLoggado() {
        return usuarioLoggado;
    }

    /**
     * @param usuarioLoggado the usuarioLoggado to set
     */
    public void setUsuarioLoggado(Usuario usuarioLoggado) {
        this.usuarioLoggado = usuarioLoggado;
    }

    
    
}
