import java.io.IOException;
import java.util.Scanner;

public class AwesomeUI {
    static Scanner scanner = new Scanner(System.in);
    public static String name;

    public static void main(String[] args) throws IOException {
        // main menu
        System.out.println("localradio");
        System.out.println("----------");
        // System.out.print("what is your username / room name? ");
        // name = scanner.next();

        System.out.print("would you like to host or join? ");
        String choice = scanner.next();

        // host or join
        if (choice.toLowerCase().equals("host")) {
            System.out.println("HOSTING!");
            Host host = new Host();

            host.start();
        } else if (choice.toLowerCase().equals("join")) {
            System.out.println("JOINING!");
            Client client = new Client();
            client.join();
        }
    }
}
