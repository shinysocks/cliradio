//NETWORK DISCOVERY LOCALLY!!!!
//https://www.developer.com/web-services/dynamic-service-discovery-with-java/

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Client {
    private SourceDataLine source;

    public void join(String ip, String name) throws IOException {
        System.out.println("establishing connection with host..");
        Socket hostSocket = new Socket(ip, 8808);

        System.out.println("connected to " + hostSocket.getInetAddress());

        try {
            source = getSource();
            source.open(); 
            source.start();

        } catch (LineUnavailableException e) {
            System.out.println("cannot open microphone device..");
        }

        InputStream in = hostSocket.getInputStream();

        boolean stop = false;
        while (!stop) {
            
            try {
                source.write(Constants.BUFFER_BYTES, 0, in.read(Constants.BUFFER_BYTES));
            } catch (IOException e) {
                System.out.println("disconnected from stream.");
                stop = true;
            }
        }
        source.drain();
        source.close();
    }

    private SourceDataLine getSource() throws LineUnavailableException {
        SourceDataLine.Info info = new SourceDataLine.Info(SourceDataLine.class, Constants.format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (SourceDataLine) AudioSystem.getLine(info);
    }
}
