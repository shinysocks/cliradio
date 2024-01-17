import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Host {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void start(String name) throws IOException {
        System.out.println("server starting...");
        serverSocket = new ServerSocket(findPort());
        System.out.println("waiting...");
        clientSocket = serverSocket.accept();
        clientSocket.setKeepAlive(true);
        if (clientSocket.isConnected()) {
            System.out.println("connected to client: " + clientSocket.toString());
        }
        // send server name
        PrintStream out = new PrintStream(clientSocket.getOutputStream());  
        out.println(name);

        // recieve client name
        Scanner in = new Scanner(clientSocket.getInputStream());
        System.out.println(in.next() + " has connected.");

        stop();
    }

    public int findPort() {
        // make sure port is free first before binding to it
        System.out.println("Hosting on port 8808");
        return 8808;
    }

    public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }
}