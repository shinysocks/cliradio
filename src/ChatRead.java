import java.io.BufferedReader;
import java.io.PrintStream;

public class ChatRead implements Runnable {
    private BufferedReader in;
    private PrintStream out;
    
    public ChatRead(PrintStream out, BufferedReader in) {
        this.in = in;
        this.out = out;
    }
    
	@Override
	public void run() {
	    boolean stop = false;
	    while (!stop) {
            String message = in.readLine();
	        out.write(message);
	        try {
    	        Thread.sleep(5000);
            } catch (InterruptedException e) {
                stop = true;
            }
        }
	}
}
