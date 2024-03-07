import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TargetDataLine target;

    public static void main(String[] args) {
        System.out.println(TUI.logo());

        System.out.print("host or join? ");
        String choice = scanner.next().strip().toLowerCase();

        if (choice.equals("host")) {
            System.out.print("station name: ");
            String name = scanner.next().strip();
            try {
                target = AudioUtils.getTarget();
            } catch (LineUnavailableException e) {
                System.out.println(TUI.Color.err("can't get default audio source, try connecting a microphone."));
            }
            try {
                Host host = new Host(name, target);
                host.run();
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(TUI.Color.err("cannot start a new host."));
            }
        } else if (choice.equals("join")) {
            System.out.print("display name: ");
            String name = scanner.next().strip();

            System.out.print("ip to join: ");
            String ip = scanner.next().strip();

            try {
                SourceDataLine source = AudioUtils.getSource();
                Client client = new Client(name, ip, source);
                try {
                    client.run();
                } catch (IOException e) {
                    System.out.println(TUI.Color.err("host unavailable."));
                }
            } catch (LineUnavailableException e) {
                System.out.println(TUI.Color.err("can't get default audio sink, are speakers or headphones connected?"));
            }

        } else {
            System.out.println(TUI.Color.err("no such option."));
        }
        System.out.println(TUI.Color.err("goodbye!"));
    }
}
