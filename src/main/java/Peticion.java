import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Peticion implements Runnable{

    private Thread hilo;
    private Socket socket;
    private List<Libro> libros;
    public Peticion(Socket socket, List<Libro> libros){
        this.socket = socket;
        this.libros = libros;
        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void run() {

        try (OutputStream output = this.socket.getOutputStream();
             InputStream input = this.socket.getInputStream();){
            // Mensaje bienvenida

            String saludo = "Hola!";
            output.write(saludo.getBytes());
            output.flush();
            System.out.println("Saludo enviado");

            // Bucle para recibir y procesar múltiples peticiones
            boolean seguir = true;
            while (seguir) {
                byte[] buffer = new byte[1024];
                int numeroBytes = input.read(buffer);
                if (numeroBytes == -1) {
                    break; // Si no hay datos, salir del bucle
                }

                String mensajeIn = new String(buffer, 0, numeroBytes).trim();
                System.out.println("Mensaje recibido: " + mensajeIn);

                // Lógica de procesamiento
                String[] partes = mensajeIn.split(";");
                String opcion = partes[0];
                String respuesta = "";

                switch (opcion) {
                    case "1":
                        String isbn = partes[1];
                        respuesta = consultaISBN(isbn);
                        break;
                    case "2":
                        String titulo = partes[1];
                        respuesta = consultaTitulo(titulo);
                        break;
                    case "3":
                        String autor = partes[1];
                        respuesta = consultaAutor(autor);
                        break;
                    case "4":
                        String nuevoIsbn = partes[1];
                        String nuevoTitulo = partes[2];
                        String nuevoAutor = partes[3];
                        double nuevoPrecio = Double.parseDouble(partes[4]);
                        respuesta = agregarLibro(new Libro(nuevoIsbn, nuevoTitulo, nuevoAutor, nuevoPrecio));
                        break;
                    case "exit":
                        respuesta = "Conexión cerrada.";
                        seguir = false;
                        break;
                    default:
                        respuesta = "Opción no válida.";
                }

                // Devolver respuesta
                output.write(respuesta.getBytes());
                output.flush();
                System.out.println("Respuesta enviada");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private String consultaISBN(String isbn){
            for(Libro lib : this.libros){
                if(lib.getIsbn().equals(isbn)){
                    return lib.toString();
                }
            }
            return "No se encuentra el libro";
        }

        private String consultaTitulo(String titulo){
            for(Libro lib : this.libros){
                if(lib.getTitulo().equalsIgnoreCase(titulo)){
                    return lib.toString();
                }
            }
            return "No se encuentra el libro";
        }

        private String consultaAutor(String autor) {
            String resultado = "";
            for (Libro libro : libros) {
                if (libro.getAutor().equalsIgnoreCase(autor)) {
                    resultado += libro.toString() + "\n";
                }
            }
            return resultado;

        }

        private synchronized String agregarLibro(Libro nuevoLibro) {
            libros.add(nuevoLibro);
            return "Libro agregado exitosamente.";
        }
    }

