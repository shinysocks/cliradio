import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Host {
    private TargetDataLine target;

    // enumerate microphones
    public static TargetDataLine getTarget() throws LineUnavailableException {
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

    public void start(String name) throws IOException {
        try {
            target = getTarget();
            System.out.println(target + " selected.");
        } catch (Exception e) {
            System.out.println("please connect a microphone..");
        }

        try {
            target.open();
            target.start();
        } catch (LineUnavailableException e) {
            System.out.println("cannot access microphone..");
        }

        ServerSocket serverSocket = new ServerSocket(8808);

        System.out.println(name + " listening on port 8808");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            //handshake
            System.out.println(clientSocket.getInetAddress().getHostName() + " connected");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean stop = false;
                    while (!stop) {
                        try {
                            clientSocket.getOutputStream().write(Constants.BUFFER_BYTES, 0, new AudioInputStream(target).read(Constants.BUFFER_BYTES));
                        } catch (IOException ee) {
                            System.out.println("stream disconnected");
                            stop = true;
                        }
                    }
                }
            }).start();
        }

        // serverSocket.close();
    }
}
