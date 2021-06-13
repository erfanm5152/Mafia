import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Send implements Runnable {
    private Socket socket;
    private Client client;
    private boolean exit;
    private PrintWriter printWriter;

    public Send(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.exit = false;
        try {
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            if (scanner.hasNextLine()) {
                printWriter.println(scanner.nextLine());
            }
        }
    }

    public void setExit(boolean exit) {
        this.exit = exit;
        if (exit) {
            printWriter.close();
        }
    }

    public void stop() {
        this.exit = true;
        this.printWriter.close();

    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}