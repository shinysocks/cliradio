import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            System.out.println("Internal audio hosting not supported on Windows.");
        } else {
            Process p = new ProcessBuilder("/home/shinysocks/projects/cliradio/pw-cliradio").start();
        }
        
        // main menu
        System.out.print(ANSIColor.BOLD_DEFAULT);
        System.out.println(":------------:");
        System.out.println("|  " + ANSIColor.magenta("cliradio") + "  |");
        System.out.println(":------------:");
        System.out.print(ANSIColor.DEFAULT);

        System.out.print("host or join? ");
        String choice = scanner.next().strip().toLowerCase();

        // while not exited program
        // host or join
        if (choice.equals("host")) {
            Host host = new Host();
            System.out.print("station name: ");
            String name = scanner.next().strip();
            host.start(name);
            
            
        } else if (choice.equals("join")) {
            System.out.print("display name: ");
            String name = scanner.next().strip();

            System.out.print("ip: ");
            String ip = scanner.next().strip();
            Client client = new Client();
            client.join(ip, name);

            
        } else {
            System.out.println(ANSIColor.err("something went wrong, please try again."));
            // p.destroy();
        }
    }
}
