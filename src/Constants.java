import javax.sound.sampled.AudioFormat;

public class Constants {
    public static final int BUFFER_SIZE = 4096;

    // https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-os/src/main/java/com/baeldung/example/soundapi/ApplicationProperties.java
    public static final AudioFormat.Encoding ENCODING = AudioFormat.Encoding.PCM_SIGNED;
    public static final float RATE = 44100.0f;
    public static final int CHANNELS = 2;
    public static final int SAMPLE_SIZE = 8;
    public static final boolean BIG_ENDIAN = true;
}
