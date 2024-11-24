import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 8080;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PUERTO);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);

            byte[] buffer = new byte[1024];
            int bytesLeidos = input.read(buffer);
            String saludo = new String(buffer, 0, bytesLeidos);
            System.out.println("Servidor: " + saludo);

            boolean continuar = true;

            while (continuar) {
                System.out.println("\nMenú:");
                System.out.println("1. Consultar libro por ISBN");
                System.out.println("2. Consultar libro por título");
                System.out.println("3. Consultar libros por autor");
                System.out.println("4. Añadir libro");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                String opcion = scanner.nextLine();

                String mensaje = "";

                switch (opcion) {
                    case "1":
                        System.out.print("Ingrese el ISBN: ");
                        String isbn = scanner.nextLine();
                        mensaje = "1;" + isbn;
                        break;
                    case "2":
                        System.out.print("Ingrese el título: ");
                        String titulo = scanner.nextLine();
                        mensaje = "2;" + titulo;
                        break;
                    case "3":
                        System.out.print("Ingrese el autor: ");
                        String autor = scanner.nextLine();
                        mensaje = "3;" + autor;
                        break;
                    case "4":
                        System.out.print("Ingrese el ISBN: ");
                        String nuevoIsbn = scanner.nextLine();
                        System.out.print("Ingrese el título: ");
                        String nuevoTitulo = scanner.nextLine();
                        System.out.print("Ingrese el autor: ");
                        String nuevoAutor = scanner.nextLine();
                        System.out.print("Ingrese el precio: ");
                        double precio = Double.parseDouble(scanner.nextLine());
                        mensaje = "4;" + nuevoIsbn + ";" + nuevoTitulo + ";" + nuevoAutor + ";" + precio;
                        break;
                    case "5":
                        System.out.println("Saliendo de la aplicación...");
                        continuar = false;
                        continue;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        continue;
                }

                output.write(mensaje.getBytes());
                output.flush();

                bytesLeidos = input.read(buffer);
                String respuesta = new String(buffer, 0, bytesLeidos);
                System.out.println("Servidor: " + respuesta);
            }

        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
