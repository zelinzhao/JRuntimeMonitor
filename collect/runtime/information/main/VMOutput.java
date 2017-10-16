package collect.runtime.information.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VMOutput extends VM implements Runnable {

    private BufferedReader bufferedReader;
    private String threadName;

    private Thread thread;

    VMOutput(String threadName, InputStream stream) {
        this.threadName = threadName;
        this.bufferedReader = new BufferedReader(new InputStreamReader(stream));
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void start() {
        this.thread = new Thread(this, threadName);
        this.thread.setPriority(Thread.MAX_PRIORITY);
        this.connected = true;
        this.thread.start();
    }

    @Override
    public void run() {
        String line;
        while (connected) {
            try {
                line = bufferedReader.readLine();
                if (line != null)
                    System.out.println("[vm] print:\n    " + line);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}