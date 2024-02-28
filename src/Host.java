import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Host {
    private TargetDataLine target;
    private ArrayList<PrintWriter> chatOutputStreams;
    private boolean stop = false;
    private int clientCount = 0;

    public void start(String name) throws IOException {
        try {
            target = getTarget();
        } catch (Exception e) {
            System.out.println(ANSIColor.err("please connect a microphone.."));
        }

        ServerSocket audioSocket = new ServerSocket(8808);
        ServerSocket chatSocket = new ServerSocket(8809);
        
        System.out.println("server started. connect with " + audioSocket.getLocalSocketAddress());
       
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
                        stop = true;
                    }
                }
            }
        }).start();

        // send host messages to all clients
        Scanner scanner = new Scanner(System.in);
        new Thread(new Runnable() {
        	@Override
            public void run() {
            	while (!stop) {
                    String message = scanner.nextLine();
                    if (message.equals("/end"))
                        stop = true;
                    else
                        sendOut(ANSIColor.gray(name + ": " + message), null);
            	}
                scanner.close();
            }
       }).start();
                  
       while (!stop) {
            Socket clientChatSocket = chatSocket.accept();
            Socket clientStreamSocket = audioSocket.accept();
            
            //handshake
            if (clientChatSocket.isConnected()) {
                clientCount++;
                PrintWriter chatOut = new PrintWriter(clientChatSocket.getOutputStream(), true);
                BufferedReader chatIn = new BufferedReader(new InputStreamReader(clientChatSocket.getInputStream()));

                System.out.println(ANSIColor.success(chatIn.readLine() + " connected"));
                chatOut.println(name);
                
                // connect new clients to stream
                target.flush();
                audioOutputStreams.add(clientStreamSocket.getOutputStream());
                chatOutputStreams.add(chatOut);

                // read messages from client and send
                new Thread(new Runnable() {
                    String clientColor = ANSIColor.clientColor(clientCount);
                	@Override
                    public void run() {
                    	while (!stop) {
                            try {
                        	    String message = chatIn.readLine();
                        	    if (message == null) {
                        	        chatOutputStreams.remove(chatOut);
                        	        stop = true;
                        	    } else {
                        	        sendOut(clientColor + message + ANSIColor.DEFAULT, chatOut);
                        	    }
                        	    System.out.println(clientColor + message + ANSIColor.DEFAULT);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                                stop = true;
                            }
                    	}
                    }
                }).start();
            }
        }
        chatSocket.close();
        audioSocket.close();
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
