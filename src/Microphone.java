import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Microphone {
    public static AudioFormat getAudioFormat() {
        AudioFormat.Encoding encoding = Constants.ENCODING;
        float rate = Constants.RATE;
        int channels = Constants.CHANNELS;
        int sampleSize = Constants.SAMPLE_SIZE;
        boolean bigEndian = Constants.BIG_ENDIAN;

        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    public static AudioFormat format = getAudioFormat();

    // eventually enumerate all targets and sources

    public static TargetDataLine getTarget() throws LineUnavailableException {
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (TargetDataLine) AudioSystem.getLine(info);
    }

    public static SourceDataLine getSource() throws LineUnavailableException {
        SourceDataLine.Info info = new SourceDataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        return (SourceDataLine) AudioSystem.getLine(info);
    }



        // Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        // for (Mixer.Info mixerInfo : mixers) {
        //     Mixer m = AudioSystem.getMixer(mixerInfo);
        //     Line.Info[] lineInfos = m.getTargetLineInfo();

        //     // gets target lines (mics)
        //     if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
        //         System.out.println(mixerInfo.getName());

        //         for (Line.Info lineInfo:lineInfos) {
        //             TargetDataLine line;
        //             try {
        //                 line = (TargetDataLine) m.getLine(lineInfo);
        //                 System.out.println("\t"+line + "; level: " + line.getLevel());
        //                 return line;
                        
        //             } catch (LineUnavailableException e) {}  
        //         }
        //     } 
        // }
        // didn't find any suitable device
        // return null;

    public static void play() throws IOException {
        TargetDataLine in;
        SourceDataLine out;

        try {
            in = getTarget();
            out = getSource();
        } catch (LineUnavailableException e) {
            System.out.println("no available microphone or speaker found..");
            in = null;
            out = null;
        }

        // open microphone & speaker
        try {
            in.open();
            out.open();

        } catch (LineUnavailableException e) {
            System.out.println("no available microphone or speaker found..");
        }

        // start microphone & speaker
        in.start();
        out.start();

        AudioInputStream stream = new AudioInputStream(in);

        // stream audio from 
        byte[] bufferBytes = new byte[Constants.BUFFER_SIZE];
        int readBytes = -1;
        while ((readBytes = stream.read(bufferBytes)) != -1) {
           out.write(bufferBytes, 0, readBytes);
        }

        out.drain();
        out.close();
        stream.close();
    }
}
