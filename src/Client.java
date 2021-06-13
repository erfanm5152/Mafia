import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner keyboard;
    private Runnable receive;
    private Runnable send;

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

    public void start() {
//        receive.start();
        new Thread(receive).start();
//        send.start();
        new Thread(send).start();
    }

    public static void main(String[] args) {
        new Client().start();
    }

    public Socket getSocket() {
        return socket;
    }

    public Scanner getKeyboard() {
        return keyboard;
    }


    public Runnable getReceive() {
        return receive;
    }

    public Runnable getSend() {
        return send;
    }

    public void sendExit() {
        ((Send) send).stop();
    }

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
