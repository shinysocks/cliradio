import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;

public class SendAudio implements Runnable {
    private ArrayList<OutputStream> streams;
    private AudioInputStream in;

    public SendAudio(AudioInputStream in, ArrayList<OutputStream> streams) {
        this.streams = streams;
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int size = in.read(AudioUtils.BUFFER_BYTES);
                for (int i = 0; i < streams.size(); i++) {
                    try {
                        streams.get(i).write(AudioUtils.BUFFER_BYTES, 0, size);
                    } catch (IOException e) {
                    	streams.remove(i);
                    }
                }
            } catch (IOException e) {
                System.out.println(TUI.Color.err("can't read audio.."));
                Host.stop();
                break;
            }
        }
    }
}
