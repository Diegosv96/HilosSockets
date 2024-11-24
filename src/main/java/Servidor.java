import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Servidor {

    private static int puerto = 8080;
    private static List<Libro> libros = new LinkedList<>();

    public static void main(String[] args) {

        // Crear libros
        inicializarLibros();

        try {
            // Crear socket en 8080
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor abierto");

            while(true){

                // Esperar conexión
                Socket socket = serverSocket.accept();
                System.out.println("Conectado");

                // Hacer la peticion
                new Peticion(socket, libros);

            }

        }catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    private static void inicializarLibros() {
        libros.add(new Libro("100", "El Quijote", "Cervantes", 20.5));
        libros.add(new Libro("101", "Cien años de soledad", "Gabriel García Márquez", 18.0));
        libros.add(new Libro("102", "Orgullo y prejuicio", "Jane Austen", 15.0));
        libros.add(new Libro("103", "1984", "George Orwell", 22.5));
        libros.add(new Libro("104", "Moby Dick", "Herman Melville", 25.0));
    }
}
