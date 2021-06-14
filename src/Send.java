import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * send thread for client for send messages to the server.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Send implements Runnable {
    // socket of the client
    private Socket socket;
    // client of this thread
    private Client client;
    // for end of thread
    private boolean exit;
    // for send messages to server.
    private PrintWriter printWriter;

    /**
     * Instantiates a new Send.
     *
     * @param socket the socket
     * @param client the client
     */
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

    /**
     * send scanned messages to server.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            if (scanner.hasNextLine()) {
                printWriter.println(scanner.nextLine());
            }
        }
    }

    /**
     * Sets exit.
     *
     * @param exit the exit
     */
    public void setExit(boolean exit) {
        this.exit = exit;
        if (exit) {
            printWriter.close();
        }
    }

    /**
     * Stop this thread.
     */
    public void stop() {
        this.exit = true;
        this.printWriter.close();

    }

    /**
     * Gets print writer.
     *
     * @return the print writer
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}