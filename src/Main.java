import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // main menu
        scanner.useDelimiter("\n");
        System.out.println(" ____________");
        System.out.println("| cliradio 🎵|");
        System.out.println(" ‾‾‾‾‾‾‾‾‾‾‾‾");

        System.out.print("host or join? ");
        String choice = scanner.next();

        // host or join
        if (choice.strip().toLowerCase().equals("host")) {
            Host host = new Host();
            System.out.print("station name: ");
            String name = scanner.next().strip();
            host.start(name);
        } else if (choice.strip().toLowerCase().equals("join")) {
            System.out.print("display name: ");
            String name = scanner.next().strip();

            System.out.print("ip: ");
            String ip = scanner.next().strip();
            Client client = new Client();
            client.join(ip, name);
        } else {
            System.out.println("something went wrong, please try again.");
        }
    }
}
