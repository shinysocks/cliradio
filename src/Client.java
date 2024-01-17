//NETWORK DISCOVERY LOCALLY!!!!
//https://www.developer.com/web-services/dynamic-service-discovery-with-java/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
    public static Scanner scanner = new Scanner(System.in);

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void join() throws UnknownHostException, IOException {
        System.out.println("establishing connection with host..");
        clientSocket = new Socket("10.186.2.213", 8808);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
    }
}