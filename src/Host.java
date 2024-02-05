import java.io.BufferedReader;
import java.io.IOException;
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

    public void start(String name) throws IOException {
        try {
            target = getTarget();
            System.out.println(target + " selected.");
        } catch (Exception e) {
            System.out.println("please connect a microphone..");
        }

        ServerSocket serverSocket = new ServerSocket(8808);

        System.out.println(name + " listening on port 8808");
       
        try {
            target.open();
            target.start();
        } catch (LineUnavailableException e) {
            System.out.println("cannot access microphone..");
        }

        AudioInputStream in = new AudioInputStream(target);

        ArrayList<OutputStream> streams = new ArrayList<OutputStream>();
        
        new Thread(new Runnable() {
            boolean stop = false;
        	@Override
			public void run() {
                while (!stop) {
                    try {
                        int size = in.read(Constants.BUFFER_BYTES);
                        for (int i = 0; i < streams.size(); i++) {
                        	streams.get(i).write(Constants.BUFFER_BYTES, 0, size);
                        }
                    } catch (IOException e) {}
                }
            }
        }).start();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            //handshake
            if (clientSocket.isConnected()) {
                // BufferedReader chatIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // PrintWriter chatOut = new PrintWriter(clientSocket.getOutputStream());
                // Scanner scanner = new Scanner(System.in);
                // handshake..
                // chat start
                target.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                streams.add(clientSocket.getOutputStream());
                String clientName = clientSocket.getInetAddress().getHostName();
                System.out.println(clientSocket.getInetAddress().getHostName() + " connected");
           }
        }
    }

    // enumerate microphones
    private TargetDataLine getTarget() throws LineUnavailableException {
        // return default audio target
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, Constants.format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (TargetDataLine) AudioSystem.getLine(info);

        // ArrayList<TargetDataLine> lines = new ArrayList<TargetDataLine>();
        // TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, Constants.format);

        // for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
        //     Mixer m = AudioSystem.getMixer(mixerInfo);
        //     Line.Info[] lineInfos = m.getTargetLineInfo();

        //     for (Line.Info lineInfo : lineInfos) {
        //         if (AudioSystem.isLineSupported(info)) {
        //             lines.add((TargetDataLine) AudioSystem.getLine(info));
        //             System.out.println(lineInfo);
        //         }
        //     }
        // }
        // return lines;
    }

    private void chat() {
        
    }
}
