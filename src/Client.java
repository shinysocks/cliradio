//NETWORK DISCOVERY LOCALLY!!!!
//https://www.developer.com/web-services/dynamic-service-discovery-with-java/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
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

    public void join(String name) throws UnknownHostException, IOException {
        System.out.println("establishing connection with host..");
        clientSocket = new Socket("10.183.3.189", 8808);   
         
        Scanner in = new Scanner(clientSocket.getInputStream());
        PrintStream out = new PrintStream(clientSocket.getOutputStream()); 

        out.println(name);
        System.out.println("connected to " + in.next());

        in.close();
    }
}
