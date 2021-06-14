import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Receive thread for receive messages from the server.
 */
public class Receive implements Runnable {
    // socket connection with server
    private Socket socket;
    // client of this thread
    private Client client;
    // scanner for read from server
    private Scanner scanner;
    // for end of thread
    private boolean exit;

    /**
     * Instantiates a new Receive.
     *
     * @param socket the socket
     * @param client the client
     */
    public Receive(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.exit = false;
        try {
            this.scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * receive messages from the server.
     */
    @Override
    public void run() {
        while (!exit) {
            if (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                checkMsg(msg);
            }
        }
    }

    /**
     * Check msg & execute the corresponding command.
     *
     * @param msg the msg
     */
    public void checkMsg(String msg) {
        //for exit from server
        if (msg.equals("exit")) {
            exit();
        } else if (msg.startsWith("players:")) {//for voting time
            ((Send) client.getSend()).getPrintWriter().println("VoTe");
            System.out.println(msg);
        } else if (msg.equals("***PlayRole***")) {//for role move in night
            ((Send) client.getSend()).getPrintWriter().println("***PlayRole***");
        } else if (msg.equals("MayorRole")) {// for Mayor role in day
            ((Send) client.getSend()).getPrintWriter().println("MayorRole");
        } else if (msg.equals("spurious vote")) {// for spurious vote in voting
            ((Send) client.getSend()).getPrintWriter().println("spurious vote");
        } else if (msg.equals("spurious choice")) { // for spurious choice in day(Mayor)
            ((Send) client.getSend()).getPrintWriter().println("spurious choice");
        } else if (msg.equals("spurious role")) {// for spurious choice in night
            ((Send) client.getSend()).getPrintWriter().println("spurious role");
        } else {
            System.out.println(msg);
        }

    }

    /**
     * Exit.
     */
    public void exit() {
        client.sendExit();
        stop();
        client.closeSocket();
        System.exit(1);
    }

    /**
     * Sets exit.
     *
     * @param exit the exit
     */
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    /**
     * Stop this thread.
     */
    public void stop() {
        this.exit = true;
        scanner.close();
    }
}