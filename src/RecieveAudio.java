import java.io.InputStream;

import javax.sound.sampled.SourceDataLine;

public class RecieveAudio implements Runnable {
    private InputStream in;
    private SourceDataLine source;

    public RecieveAudio(InputStream in, SourceDataLine source) {
        this.in = in;
        this.source = source;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // read audio
                source.write(AudioUtils.BUFFER_BYTES, 0, in.read(AudioUtils.BUFFER_BYTES));
            } catch (Exception e) {
                System.out.println(TUI.Color.err("host stream ended, press enter to exit."));
                Client.stop();
                break;
            }
        }
    }
}
