import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteHandler implements Runnable { //es lo que va a comunicar el cliente con el servidor

    private Socket socket;
    private ArrayList<ClienteHandler> clienteHandlers = new ArrayList<>();
    private BufferedReader in;
    private BufferedWriter out;
    private String nombreCliente;

    public ClienteHandler(Socket socket) {
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.nombreCliente = in.readLine();
            clienteHandlers.add(this);
            mandarMensaje("SERVER: Se ha unido el cliente " + nombreCliente);
        } catch (IOException e) {
            e.printStackTrace();
            cerrarComunicacion(socket, out, in);
        }
    }

    public void quitarClienteHandler() {
        this.clienteHandlers.remove(this);
        mandarMensaje("SERVER: Se ha ido el cliente " + nombreCliente);
    }

    @Override
    public void run() {
        String mensajeDesdeCliente;
        while(socket.isConnected()){
            try {
                mensajeDesdeCliente = in.readLine();
                mandarMensaje(mensajeDesdeCliente);
            } catch (IOException e) {
                e.printStackTrace();
                cerrarComunicacion(socket, out, in);
            }
        }
    }

    public void mandarMensaje(String mensaje) {
        for (ClienteHandler clienteHandler : clienteHandlers) {
           /*por cada objeto clientehandler al que llamamos clientehandler
           que haya en el array clientehandlers hacemos lo de dentro del for*/
            try {
                clienteHandler.out.write(mensaje);
                clienteHandler.out.newLine();
                clienteHandler.out.flush(); //para vaciar
            } catch (IOException e) {
                e.printStackTrace();
                cerrarComunicacion(socket, out, in);
            }
        }
    }

    public void cerrarComunicacion(Socket socket, BufferedWriter out, BufferedReader in){
        quitarClienteHandler();
        try{
            if(socket != null){
                socket.close();
            }
            if(socket != null){
                in.close();
            }
            if(socket != null){
                out.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
