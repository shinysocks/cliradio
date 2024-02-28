import javax.sound.sampled.AudioFormat;

public class Constants {
    public static byte[] BUFFER_BYTES = new byte[4096];

    // https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-os/src/main/java/com/baeldung/example/soundapi/ApplicationProperties.java

    public static AudioFormat getAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 8;
        boolean bigEndian = true;

        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    public static final AudioFormat format = getAudioFormat();
}
