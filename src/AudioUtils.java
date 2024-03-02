import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioUtils {
    public static byte[] BUFFER_BYTES = new byte[4096];

    // build audio format
    private static AudioFormat getAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 8;
        boolean bigEndian = true;
        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    public static final AudioFormat format = getAudioFormat();

    public static Process startInternalTarget() {
        try {
            return new ProcessBuilder("/home/shinysocks/projects/cliradio/pw-cliradio").start();
        } catch (IOException e) {
        	System.out.println(TUI.Color.warn("can't create internal source, is Pipewire installed?"));
        	return null;
        }
    }

    // return default audio source (microphone or virtual source)
    public static TargetDataLine getTarget() throws LineUnavailableException {
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info))
            return null;
        return (TargetDataLine) AudioSystem.getLine(info);
    }

    // return default audio sink (speaker or headphones)
    public static SourceDataLine getSource() throws LineUnavailableException {
        SourceDataLine.Info info = new SourceDataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info))
            return null;
        return (SourceDataLine) AudioSystem.getLine(info);
    }
}
