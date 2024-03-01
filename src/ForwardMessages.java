import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ForwardMessages implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<PrintWriter> outStreams;
    private String color;

    public ForwardMessages(BufferedReader in, PrintWriter out, ArrayList<PrintWriter> outStreams,
            String color) {
        this.in = in;
        this.out = out;
        this.outStreams = outStreams;
        this.color = color;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = in.readLine();
                if (message == null) {
                    outStreams.remove(out);
                    break;
                } else {
                    Host.sendOut(color + message + TUI.Color.DEFAULT, out);
                }
                System.out.println(color + message + TUI.Color.DEFAULT);
            } catch (IOException e) {
                break;
            }
        }
    }
}
