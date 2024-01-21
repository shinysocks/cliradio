import java.io.DataOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Stream implements Runnable {
    private TargetDataLine target;
    private DataOutputStream out;
    private boolean stop = false;

    public Stream(TargetDataLine target, DataOutputStream out) {
        this.target = target;
        this.out = out;

        // create and start thread based on runnable?
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            target.open();   
        } catch (LineUnavailableException e) {
            System.out.println("Audio target is no longer available..");
        }

        try {
                // start microphone
                target.start();
        
                // create stream object
                AudioInputStream stream = new AudioInputStream(target);
        
                // create byte buffer
                byte[] bufferBytes = new byte[Constants.BUFFER_SIZE];
                
                while (!stop) {
                   out.write(bufferBytes, 0, stream.read(bufferBytes));
                }
                out.close();

        } catch (IOException e) {
            System.out.println("stream disconnected");
        }
    }

    public void stop() {
        this.stop = true;
    }
}
