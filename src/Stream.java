public class Stream extends Thread {
    @Override
    public synchronized void start() {
        super.start();
        try {
            //Wait for one sec so it doesn't print too fast
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
