import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    public void start() throws IOException {
        System.out.println("server starting...");
        serverSocket = new ServerSocket(findPort());
        System.out.println("waiting...");
        clientSocket = serverSocket.accept();
        clientSocket.setKeepAlive(true);
        if (clientSocket.isConnected()) {
            System.out.println("connected to client" + clientSocket.getLocalSocketAddress());
        }
    }

    public int findPort() {
        // make sure port is free first before binding to it
        System.out.println("Hosting on port 8808");
        return 8808;
    }

    public void stop() throws IOException {
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}