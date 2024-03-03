import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Host {
    private String name;
    private TargetDataLine target;
    private int count = 0;
    private Process internalTarget;
    private ArrayList<OutputStream> audioOutStreams = new ArrayList<OutputStream>();
    private static ArrayList<PrintWriter> chatOutStreams = new ArrayList<PrintWriter>();
    private ServerSocket audioSocket, chatSocket;
    private static boolean stop = false;

    public Host(String name, TargetDataLine target) {
        this.name = name;
        this.target = target;
    }

    public void run() throws IOException, LineUnavailableException {
        audioSocket = new ServerSocket(8808);
        chatSocket = new ServerSocket(8809);
        Scanner scanner = new Scanner(System.in);

        if (!TUI.IS_WINDOWS) {
            System.out.println(TUI.Color.success("internal audio capture is supported, creating virtual capture source.."));
            try {
                internalTarget = AudioUtils.startInternalTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                            
        target.open();
        target.start();

        System.out.println(TUI.hostChatStart());

        Socket socket = new Socket();
        System.out.println(TUI.Color.success("server started. connect with " + getLocalIP(socket)));
        socket.close();

        new Thread(new SendAudio(new AudioInputStream(target), audioOutStreams)).start();
        new Thread(new SendHostMessage(scanner, name)).start();

        while (!stop) {
            Socket clientChatSocket = chatSocket.accept();
            Socket clientStreamSocket = audioSocket.accept();
            
            count++;
            PrintWriter out = new PrintWriter(clientChatSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientChatSocket.getInputStream()));

            System.out.println(TUI.Color.success(in.readLine() + " connected"));
            out.println(name);
            
            // connect new clients to stream
            target.flush();
            audioOutStreams.add(clientStreamSocket.getOutputStream());
            chatOutStreams.add(out);

            // message send thread
            Thread clientThread = new Thread(new ForwardMessages(in, out, chatOutStreams, TUI.Color.clientColor(count)));
            clientThread.start();
        }
        
        try {
            chatSocket.close();
            audioSocket.close();
        } catch (IOException e) {}
        
        if (internalTarget != null)
            internalTarget.destroy();
    }

    

    public static void sendOut(String message, PrintWriter ignore) {
        for (PrintWriter pw : chatOutStreams) {
            if (pw != ignore) {
                pw.println(message);
            } 
        }
    }

    public static void stop() {
        stop = true;
    }

    private String getLocalIP(Socket socket) {
        try {
            socket.connect(new InetSocketAddress("google.com", 80));
            return TUI.Color.blue(socket.getLocalAddress().getHostAddress());
        } catch (IOException e) {
            System.out.println(TUI.Color.warn("can't get local ip address, are you connected to the internet?"));
            return TUI.Color.blue("localhost");
        }
    }
}
