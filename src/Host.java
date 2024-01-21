import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Host {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    // get microphone
    public static TargetDataLine getTarget() throws LineUnavailableException {
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, Constants.format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (TargetDataLine) AudioSystem.getLine(info);
    }

    public void start() throws IOException {
        // System.out.println("make sure to open the port on your firewall..");

        serverSocket = new ServerSocket(findPort());

        System.out.println("server started on port " + findPort());

        clientSocket = serverSocket.accept();
        clientSocket.setKeepAlive(true);

        if (clientSocket.isConnected()) {
            // reveal name eventually??
            System.out.println(clientSocket.getInetAddress() + " connected");
        }

        // setup stream to client
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        try {
            TargetDataLine target = getTarget();
            Stream stream = new Stream(target, out);
            if (new Scanner(System.in).next().equals("stop")) {
                stream.stop();
            }
        } catch (Exception e) {
            System.out.println("couldn't get an audio target..");
        }
    }

    public int findPort() {
        // make sure port is free first before binding to it?
        // eventually with discovery use a random port?
        return 8808;
    }
}
