import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The type Client.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Client {
    // socket of connection between server & client
    private Socket socket;
    // scanner of System.in
    private Scanner keyboard;
    // receive thread
    private Runnable receive;
    // send thread
    private Runnable send;

    /**
     * Instantiates a new Client.
     */
    public Client() {
        keyboard = new Scanner(System.in);
        int port;
        System.out.println("port ra vared konid : ");
        port = Integer.parseInt(keyboard.nextLine());
        try {
            this.socket = new Socket("127.0.0.1", port);
            System.out.println("vasl shod");
            receive = new Receive(socket, this);
//            send= new Thread(new Send(socket,this));
            send = new Send(socket, this);
        } catch (ConnectException e) {
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start.
     */
    public void start() {
//        receive.start();
        new Thread(receive).start();
//        send.start();
        new Thread(send).start();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Client().start();
    }

    /**
     * Gets socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Gets keyboard.
     *
     * @return the keyboard
     */
    public Scanner getKeyboard() {
        return keyboard;
    }


    /**
     * Gets receive.
     *
     * @return the receive
     */
    public Runnable getReceive() {
        return receive;
    }

    /**
     * Gets send.
     *
     * @return the send
     */
    public Runnable getSend() {
        return send;
    }

    /**
     * Send exit.
     */
    public void sendExit() {
        ((Send) send).stop();
    }

    /**
     * Close socket.
     */
    public void closeSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
