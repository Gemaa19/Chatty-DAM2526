import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String nombreCliente;

    public Cliente(Socket socket, String nombreCliente) {
        try {
            this.socket = socket;
            this.nombreCliente = nombreCliente;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            cerrarComunicacion(socket, out, in);
        }
    }

    public void cerrarComunicacion(Socket socket, BufferedWriter out, BufferedReader in) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (socket != null) {
                in.close();
            }
            if (socket != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje() {
        try {
            out.write(nombreCliente);
            out.newLine();
            out.flush();
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                System.out.println("Escribe el mensaje: ");
                String mensaje = scanner.next();
                out.write(nombreCliente + ": " + mensaje);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            cerrarComunicacion(socket, out, in);
        }
    }

    public void escucharMensaje(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensajeDesdeChat;
                while(socket.isConnected()){
                    try {
                        mensajeDesdeChat = in.readLine();
                        System.out.println(mensajeDesdeChat);
                    } catch (IOException e) {
                        e.printStackTrace();
                        cerrarComunicacion(socket, out, in);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce tu nombre");
        String nombreCliente = scanner.next();

        Socket socket = new Socket("localhost", 5000);
        Cliente cliente = new Cliente(socket, nombreCliente);
        //está escuchando hasta que recibe el mensaje
        cliente.escucharMensaje();
        cliente.enviarMensaje();
    }
}