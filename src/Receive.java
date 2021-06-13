import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Receive implements Runnable {
    private Socket socket;
    private Client client;
    private Scanner scanner;
    private boolean exit;

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

    @Override
    public void run() {
        while (!exit) {
            if (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                checkMsg(msg);
            }
        }
    }

    public void checkMsg(String msg) {
        if (msg.equals("exit")) {
            exit();
        } else if (msg.startsWith("players:")) {
            ((Send) client.getSend()).getPrintWriter().println("VoTe");
            System.out.println(msg);
        } else if (msg.equals("***PlayRole***")) {
            ((Send) client.getSend()).getPrintWriter().println("***PlayRole***");
        } else if (msg.equals("MayorRole")) {
            ((Send) client.getSend()).getPrintWriter().println("MayorRole");
        } else if (msg.equals("spurious vote")) {
            ((Send) client.getSend()).getPrintWriter().println("spurious vote");
        } else if (msg.equals("spurious choice")) {
            ((Send) client.getSend()).getPrintWriter().println("spurious choice");
        } else if (msg.equals("spurious role")) {
            ((Send) client.getSend()).getPrintWriter().println("spurious role");
        } else {
            System.out.println(msg);
        }

    }

    public void exit() {
        client.sendExit();
        stop();
        client.closeSocket();
        System.exit(1);
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void stop() {
        this.exit = true;
        scanner.close();
    }
}