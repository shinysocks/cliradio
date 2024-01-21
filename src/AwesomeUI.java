import java.io.IOException;
import java.util.Scanner;

public class AwesomeUI {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // main menu
        scanner.useDelimiter("\n");
        System.out.println("localradio");
        System.out.println("----------");
        // System.out.print("what is your username / room name? ");
        // name = scanner.next();

        System.out.print("would you like to host or join? ");
        String choice = scanner.next();

        // host or join
        if (choice.strip().toLowerCase().equals("host")) {
            Host host = new Host();
            host.start();
        } else if (choice.strip().toLowerCase().equals("join")) {
            System.out.print("what ip would you like to join? ");
            String ip = scanner.next().strip();
            Client client = new Client();
            client.join("127.0.0.1");
        }
    }
}
