import java.io.BufferedReader;
import java.io.IOException;

public class RecieveMessage implements Runnable {
    private BufferedReader in;

    public RecieveMessage(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = in.readLine();
                if (message != null)
                    System.out.println(message);
                else {
                    Client.stop();
                    break;
                }
            } catch (IOException e) {
                Client.stop();
                break;
            }
        }
    }
}
