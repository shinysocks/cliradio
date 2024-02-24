import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // put all main logic here, never leave main..
        
        // main menu
        System.out.println(":------------:");
        System.out.println("|  cliradio  |");
        System.out.println(":------------:");

        System.out.print("host or join? ");
        String choice = scanner.next().strip().toLowerCase();

        // while not exited program
        // system.clear()
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
            System.out.println("something went wrong, please try again.");
            // try again
        }
    }
}
