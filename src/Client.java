import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Scanner;

public class Client {
    private SourceDataLine source;
    private String name;
    private String ip;
    private InputStream audioIn;
    private BufferedReader chatIn;
    private PrintWriter chatOut;
    private static Scanner scanner;
    private Socket audioSocket, chatSocket;
    private static boolean stop = false;

    public Client(String name, String ip, SourceDataLine source) {
        this.name = name;
        this.ip = ip;
        this.source = source;
    }

    public void run() throws IOException, LineUnavailableException {
        System.out.println("establishing connection with host..");

        audioSocket = new Socket(ip, 8808);
        chatSocket = new Socket(ip, 8809);

        source.open();
        source.start();

        audioIn = audioSocket.getInputStream();
        chatIn = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
        chatOut = new PrintWriter(chatSocket.getOutputStream(), true);
        scanner = new Scanner(System.in);

        // send server client name
        chatOut.println(name);
        System.out.println(TUI.clientChatStart());
        System.out.println(TUI.Color.success("connected to " + chatIn.readLine()));

        new Thread(new RecieveAudio(audioIn, source)).start();
        new Thread(new RecieveMessage(chatIn)).start();

        while (!stop) {
            // send client messages
            String message = scanner.nextLine();
            chatOut.println(name + ": " + message);
        } 
        
        // close resources
        try {
            audioSocket.close();
            chatSocket.close();
        } catch (IOException e) {}
        source.drain();
        source.close();
    }

    public static void stop() {
        scanner.close();
        stop = true;
    }
}
