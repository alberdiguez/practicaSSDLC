package ui;

import capanegocio.AgendaFacade;
import capanegocio.Auditoria;
import capanegocio.NoAutorizadoException;
import java.util.Scanner;

/**
 *
 * @author alber
 */
public class Autorizacion {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //Usuario ya creado: admin1
        //Contraseña: admin
        AgendaFacade.getInstance().leerContactos();
        AgendaFacade.getInstance().leerUsuarios();
        int opcionMenu;

        while ((opcionMenu = imprimirMenu()) != 8) {
            try {
                switch (opcionMenu) {

                    case 1:
                        AgendaFacade.getInstance().agregarContacto();
                        break;

                    case 2:
                        AgendaFacade.getInstance().modificarContacto();
                        break;

                    case 3:
                        AgendaFacade.getInstance().eliminarContacto();
                        break;

                    case 4:
                        AgendaFacade.getInstance().listarContactos();
                        break;

                    case 5:
                        AgendaFacade.getInstance().agregarUsuario();
                        break;

                    case 6:
                        sc.nextLine();
                        System.out.println("introduzca usuario");
                        String usuario = sc.nextLine();
                        System.out.println("Introduzca contraseña");
                        String password = sc.nextLine();
                        AgendaFacade.getInstance().logIn(usuario, password);
                        break;

                    case 7:
                        AgendaFacade.getInstance().logOut();
                        break;
                    default:
                        break;
                }
            } catch (NoAutorizadoException ex) {
                   System.out.println("No estás autorizado para realizar esta operación");
            }
        }
        Auditoria.getInstance().cerrarFichero();
        AgendaFacade.getInstance().guardarContactos();
        AgendaFacade.getInstance().guardarUsuarios();

    }

    public static int imprimirMenu() {
        int opcionMenu;
        //menu con opciones
        System.out.println("1. Agregar contacto");
        System.out.println("2. Modificar contacto");
        System.out.println("3. Eliminar contacto");
        System.out.println("4. Listar contactos");
        System.out.println("5. Agregar usuario");
        System.out.println("6. Log in");
        System.out.println("7. Log out");
        System.out.println("8. Salir");
        System.out.println("Seleccione una opción");
        opcionMenu = sc.nextInt();

        return opcionMenu;

    }
}
