import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import java.util.Scanner;

public class Client {
    private String name;
    private SourceDataLine source;
    
    public void join(String ip, String name) throws IOException {
        System.out.println("establishing connection with host..");
        
        Socket streamSocket = new Socket(ip, 8808);
        Socket chatSocket = new Socket(ip, 8809);

        // replace with host's name
        System.out.println("connected to " + chatSocket.getInetAddress());

        try {
            source = getSource();
            source.open(); 
            source.start();

        } catch (LineUnavailableException e) {
            System.out.println("cannot open microphone device..");
        }

        InputStream in = streamSocket.getInputStream();
        BufferedReader chatIn = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
        PrintWriter chatOut = new PrintWriter(chatSocket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        // send messages to server
         new Thread(new Runnable() {
        	@Override
			public void run() {
                while (true) {
                    String message = scanner.nextLine();
                    chatOut.println(name + ": " + message);
                }
            }
        }).start();

        // read out messages
         new Thread(new Runnable() {
        	@Override
			public void run() {
			    boolean stop = false;
                while (!stop) {
                    try {
                        String message = chatIn.readLine();
                        if (message != null) {
                            System.out.println(message);
                        } else {
                            stop = true;
                        }
                    } catch (IOException e) {
                        System.out.println("can't read message..");
                    }
                }
            }
        }).start();

        // play audio
        while (true) {
            try {
                // read audio
                source.write(Constants.BUFFER_BYTES, 0, in.read(Constants.BUFFER_BYTES));
            } catch (IOException e) {
                System.out.println("disconnected from stream.");
            }
        }
        // source.drain();
        // source.close();
    }

    private SourceDataLine getSource() throws LineUnavailableException {
        SourceDataLine.Info info = new SourceDataLine.Info(SourceDataLine.class, Constants.format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (SourceDataLine) AudioSystem.getLine(info);
    }
}
