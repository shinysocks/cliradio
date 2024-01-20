import java.io.IOException;
import java.util.Scanner;

public class AwesomeUI {
    static Scanner scanner = new Scanner(System.in);
    public static String name;

    public static void main(String[] args) throws IOException {
        // main menu
        scanner.useDelimiter("\n");
        System.out.println("localradio");
        System.out.println("----------");
        System.out.print("what is your username / room name? ");
        name = scanner.next();

        System.out.print("would you like to host or join? ");
        String choice = scanner.next();

        // host or join
        if (choice.toLowerCase().equals("host")) {
            Host host = new Host();
            host.start(name);
        } else if (choice.toLowerCase().equals("join")) {
            Client client = new Client();
            client.join(name);
        } else if (choice.equals("a")) {
            Microphone.play();
        }
    }
}
