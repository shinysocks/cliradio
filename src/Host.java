import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Thread;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Host {
    private TargetDataLine target;
    public InputStream chatInputStream;
    public ArrayList<PrintWriter> chatOutputStreams;

    public void start(String name) throws IOException {
        try {
            target = getTarget();
        } catch (Exception e) {
            System.out.println("please connect a microphone..");
        }

        ServerSocket streamSocket = new ServerSocket(8808);
        ServerSocket chatSocket = new ServerSocket(8809);
        
        System.out.println(name + " listening on port 8808");
       
        try {
            target.open();
            target.start();
        } catch (LineUnavailableException e) {
            System.out.println("cannot access microphone..");
        }

        AudioInputStream in = new AudioInputStream(target);
        ArrayList<OutputStream> audioOutputStreams = new ArrayList<OutputStream>();
        chatOutputStreams = new ArrayList<PrintWriter>();

        // send host audio to all clients
        new Thread(new Runnable() {
            boolean stop = false;
        	@Override
			public void run() {
                while (!stop) {
                    try {
                        int size = in.read(Constants.BUFFER_BYTES);
                        for (int i = 0; i < audioOutputStreams.size(); i++) {
                        	audioOutputStreams.get(i).write(Constants.BUFFER_BYTES, 0, size);
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();

        // send host messages to all clients
        Scanner scanner = new Scanner(System.in);
        new Thread(new Runnable() {
        	@Override
            public void run() {
            	while (true) {
            	    System.out.print("> ");
                    String message = scanner.nextLine();
                    sendOut(name + ": " + message, null);
            	}
            }
        }).start();
        
        while (true) {
            Socket clientChatSocket = chatSocket.accept();
            Socket clientStreamSocket = streamSocket.accept();
            
            //handshake
            if (clientChatSocket.isConnected()) {
                PrintWriter chatOut = new PrintWriter(clientChatSocket.getOutputStream(), true);
                BufferedReader chatIn = new BufferedReader(new InputStreamReader(clientChatSocket.getInputStream()));
                
                // connect new clients to stream
                target.flush();
                audioOutputStreams.add(clientStreamSocket.getOutputStream());
                chatOutputStreams.add(chatOut);

                // read messages from client and send
                new Thread(new Runnable() {
                	@Override
                    public void run() {
                    	while (true) {
                            try {
                        	    String message = chatIn.readLine();
                        	    sendOut(message, chatOut);
                        	    System.out.println(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    	}
                    }
                }).start();
            }
        }
    }

    private void sendOut(String message, PrintWriter ignore) {
        for (PrintWriter pw : chatOutputStreams) {
            if (pw != ignore) {
                pw.println(message);
            } 
        }
    }

    // access microphone
    private TargetDataLine getTarget() throws LineUnavailableException {
        // return default audio target
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, Constants.format);
        if (!AudioSystem.isLineSupported(info))
            return null;
        return (TargetDataLine) AudioSystem.getLine(info);
    }
}
