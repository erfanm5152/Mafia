import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner keyboard;
    private String name;
    private Thread receive;
    private Thread send;

    public Client() {
        keyboard = new Scanner(System.in);
        int port;
        System.out.println("port ra vared konid : ");
        port = Integer.parseInt(keyboard.nextLine());
        try {
            this.socket = new Socket("127.0.0.1",port);
            System.out.println("vasl shod");
            System.out.println("name ra vared konid : ");
            name = keyboard.nextLine();
            receive = new Thread(new Receive(socket , this));
            send= new Thread(new Send(socket,this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        receive.start();
        send.start();
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

    public String getName() {
        return name;
    }

    public Thread getReceive() {
        return receive;
    }

    public Thread getSend() {
        return send;
    }
}
class Receive implements Runnable{
    private Socket socket;
    private Client client;

    public Receive(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner scanner =new Scanner(socket.getInputStream());
            while (true){
                if (scanner.hasNextLine()){
                    System.out.println(scanner.nextLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Send implements Runnable{
    private Socket socket;
    private Client client;

    public Send(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner =new Scanner(System.in);
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                if (scanner.hasNextLine()) {
                    pw.println(client.getName()+" : "+scanner.nextLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}