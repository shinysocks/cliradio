import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Host {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void start() throws IOException {
        System.out.println("server starting...");
        serverSocket = new ServerSocket(findPort());
        System.out.println("waiting...");
        clientSocket = serverSocket.accept();
        clientSocket.setKeepAlive(true);
        if (clientSocket.isConnected()) {
            System.out.println("connected to client: " + clientSocket.toString());
        }
        // send server name
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        stream(out);
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

    public static TargetDataLine getTarget() throws LineUnavailableException {
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, Constants.format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (TargetDataLine) AudioSystem.getLine(info);
    }

    public static void stream(DataOutputStream out) throws IOException {
        TargetDataLine in;

        try {
            in = getTarget();

            in.open();
        } catch (LineUnavailableException e) {
            System.out.println("no available microphone found..");
            in = null;
        }

        // start microphone & speaker
        in.start();

        AudioInputStream stream = new AudioInputStream(in);

        // stream audio
        byte[] bufferBytes = new byte[Constants.BUFFER_SIZE];
        
        while (true) {
           out.write(bufferBytes, 0, stream.read(bufferBytes));
        }

        // out.close();
        // stream.close();
    }

}
