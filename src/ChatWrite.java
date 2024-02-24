import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ChatWrite implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    
    public ChatWrite(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }
    
	@Override
	public void run() {
	    // try interrupting threads to stop them??
	    while (true) {
            try {
                String message = in.readLine();
                if (message != null) {
        	        out.write("name: " + message);
                }
            } catch (IOException e) {
                System.out.println("can't read..");
            }
        }
	}
}
