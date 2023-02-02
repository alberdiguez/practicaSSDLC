package capanegocio;

import accesodatos.Admin;
import accesodatos.Usuario;
import accesodatos.Agenda;
import accesodatos.Asistente;
import accesodatos.Contacto;
import accesodatos.Gestor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgendaFacade {

    private static AgendaFacade instance;
    private Usuario usuarioLoggado;
    private Agenda agenda;
    private Scanner sc;
    private CifradorAES cifrador;

    public static AgendaFacade getInstance() {
        if (instance == null) {
            instance = new AgendaFacade();
        }
        return instance;
    }

    public AgendaFacade() {
        agenda = new Agenda();
        sc = new Scanner(System.in);
        cifrador = new CifradorAES();
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
                Auditoria.getInstance().auditar("Agregar contacto", "OK", contacto.getNombre() + contacto.getApellidos(), usuarioLoggado);
                System.out.println("Contacto añadido");
            } else {
                Auditoria.getInstance().auditar("Agregar contacto", "NOT OK", contacto.getNombre() + contacto.getApellidos(), usuarioLoggado);
                System.out.println("Ese contacto ya existe");
            }
        } else {
            Auditoria.getInstance().auditar("Agregar contacto", "NO AUTORIZADO", "N/A", usuarioLoggado);
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
                Auditoria.getInstance().auditar("Modificar contacto", "OK", nombre + apellidos, usuarioLoggado);
                System.out.println("Contacto modificado");
            } else {
                Auditoria.getInstance().auditar("Modificar contacto", "NOT OK", nombre + apellidos, usuarioLoggado);
                System.out.println("Ese contacto no existe");
            }

        } else {
            Auditoria.getInstance().auditar("Modificar contacto", "NO AUTORIZADO", "N/A", usuarioLoggado);
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
                Auditoria.getInstance().auditar("Eliminar contacto", "OK", agenda.getContactos().get(contactElegido).getNombre() + 
                      agenda.getContactos().get(contactElegido).getApellidos(), usuarioLoggado);
                System.out.println("Contacto " + contactElegido + " eliminado");
            } else {
                Auditoria.getInstance().auditar("Eliminar contacto", "NOT OK", agenda.getContactos().get(contactElegido).getNombre() + 
                      agenda.getContactos().get(contactElegido).getApellidos(), usuarioLoggado);
                System.out.println("Ese contacto no existe");
            }
        } else {
            Auditoria.getInstance().auditar("Eliminar contacto", "NO AUTORIZADO", "N/A", usuarioLoggado);
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void listarContactos() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("listar_contactos", usuarioLoggado)) {
            Auditoria.getInstance().auditar("Listar contactos", "OK", "Lista contactos", usuarioLoggado);
            agenda.listContactos();
        } else {
            Auditoria.getInstance().auditar("Listar contactos", "NO AUTORIZADO", "N/A", usuarioLoggado);
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void agregarUsuario() throws NoAutorizadoException {

        if (GestorPermisos.getInstance().estaAutorizado("agregar_usuario", usuarioLoggado)) {
            boolean resultado = false;
            String rolElegido = null;
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
                    rolElegido = "administrador";
                    break;
                case 2:
                    resultado = GestorPermisos.getInstance().agregarUsuario(new Gestor(usuario, password));
                    rolElegido = "gestor";
                    break;
                case 3:
                    resultado = GestorPermisos.getInstance().agregarUsuario(new Asistente(usuario, password));
                    rolElegido = "asistente";
                    break;
                default:
                    throw new AssertionError();
            }
            if (resultado == true) {
                Auditoria.getInstance().auditar("Agregar usuario", "OK", usuario + " con ROL: " + rolElegido, usuarioLoggado);
                System.out.println("Usuario agregado correctamente");
            } else {
                Auditoria.getInstance().auditar("Agregar usuario", "NOT OK", usuario + " con ROL: " + rolElegido, usuarioLoggado);
                System.out.println("Ese usuario ya existe");
            }
            sc.nextLine();
        } else {
            Auditoria.getInstance().auditar("Listar contactos", "NO AUTORIZADO", "N/A", usuarioLoggado);
            throw new NoAutorizadoException("Este usuario no esta autorizado");
        }
    }

    public void logIn(String usuario, String password) {
        boolean encontrado = false;
        for (int i = 0; i < GestorPermisos.getInstance().getUsuariosRegistrados().size() && !encontrado; i++) {
            if (GestorPermisos.getInstance().getUsuariosRegistrados().get(i).getNombreUsuario().equals(usuario)) {
                encontrado = true;
                if (GestorPermisos.getInstance().getUsuariosRegistrados().get(i).getPassword().equals(password)) {
                    Auditoria.getInstance().auditar("LOG IN", "OK", usuario, usuarioLoggado);
                    System.out.println("Log in correcto");
                    usuarioLoggado = GestorPermisos.getInstance().getUsuariosRegistrados().get(i);
                } else {
                    Auditoria.getInstance().auditar("LOG IN", "NOT OK", usuario, usuarioLoggado);
                    System.out.println("Nombre de usuario o contraseña incorrectos");
                    logOut();
                }
            }
        }
        if (!encontrado) {
            Auditoria.getInstance().auditar("LOG IN", "NOT OK", usuario, usuarioLoggado);
            System.out.println("Nombre de usuario o contraseña incorrectos");
            logOut();
        }
    }

    public void logOut() {
        Auditoria.getInstance().auditar("LOG OUT", "OK", usuarioLoggado.getNombreUsuario(), usuarioLoggado);
        usuarioLoggado = null;
        System.out.println("LOG OUT Correcto");
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

    public void guardarContactos() {
        try ( PrintWriter pwContactos = new PrintWriter("contactos.csv");  
                PrintWriter pwHashes = new PrintWriter("contactos_hashes.txt");) {
            for (Contacto contacto : agenda.getContactos()) {
                String direccionCifrada = cifrador.cifrar(contacto.getDireccion());
                String telefonoCifrado = cifrador.cifrar(contacto.getTelefono());
                String hash = Utilidades.generateSHA(contacto.getNombre() + "," + contacto.getApellidos() + ","
                        + direccionCifrada+ "," + telefonoCifrado);
                pwContactos.println(contacto.getNombre() + "," + contacto.getApellidos() + ","
                        + direccionCifrada+ "," + telefonoCifrado);
                pwHashes.println(hash);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leerContactos() {
        try ( BufferedReader brContactos = new BufferedReader(new FileReader("contactos.csv"));  
                BufferedReader brHashes = new BufferedReader(new FileReader("contactos_hashes.txt"))) {
            String linea = null;
            while((linea = brContactos.readLine()) != null){
                String[] campos = linea.split(",");
                String hash = Utilidades.generateSHA(campos[0] + "," + campos[1] + "," +  campos[2] + "," + campos[3]);
                Contacto contacto = new Contacto(campos[0], campos[1], cifrador.descifrar(campos[2]), cifrador.descifrar(campos[3]));
                String hashLeido = brHashes.readLine();
                if(hash.equals(hashLeido)){
                    agenda.addContacto(contacto);
                }else{
                    System.out.println("El contacto:" + contacto + " HA SIDO MANIPULADO");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarUsuarios() {
        try ( PrintWriter pwUsuarios = new PrintWriter("usuarios.csv");  
                PrintWriter pwHashes = new PrintWriter("usuarios_hashes.txt");) {
            for (Usuario usuario : GestorPermisos.getInstance().getUsuariosRegistrados()) {
                String hash = Utilidades.generateSHA(usuario.getNombreUsuario() + "," + 
                        usuario.getRol()+ "," + usuario.getPassword());
                pwUsuarios.println(usuario.getNombreUsuario() + "," + usuario.getRol()+ 
                        "," + usuario.getPassword());
                pwHashes.println(hash);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leerUsuarios() {
        try ( BufferedReader brUsuarios = new BufferedReader(new FileReader("usuarios.csv"));  
                BufferedReader brHashes = new BufferedReader(new FileReader("usuarios_hashes.txt"))) {
            String linea = null;
            while((linea = brUsuarios.readLine()) != null){
                String[] campos = linea.split(",");
                Usuario usuario = new Usuario(campos[0], campos[1], campos[2]) {};                
                String hashLeido = brHashes.readLine();
                String hash = Utilidades.generateSHA(usuario.getNombreUsuario() + "," + 
                        usuario.getRol()+ "," + usuario.getPassword());
                if(hash.equals(hashLeido)){
                    GestorPermisos.getInstance().agregarUsuario(usuario);
                }else{
                    System.out.println("El usuario:" + usuario + " HA SIDO MANIPULADO");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AgendaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
