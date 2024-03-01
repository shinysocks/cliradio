import java.util.Scanner;

public class SendHostMessage implements Runnable {
    private Scanner scanner;
    private String name;

    public SendHostMessage(Scanner scanner, String name) {
        this.scanner = scanner;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            String message = scanner.nextLine();
            Host.sendOut(TUI.Color.gray(name + ": " + message), null);
        }
    }
}
