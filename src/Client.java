import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner keyboard;
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
    private Scanner scanner;
    private boolean exit;

    public Receive(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.exit =false;
        try {
            this.scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner =new Scanner(socket.getInputStream());
            while (!exit){
                //todo اگر پیام شامل بای بای و سرور بود از برنامه خارج شو
                if (scanner.hasNextLine()){
                    String msg = scanner.nextLine();
                    checkMsg(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void checkMsg(String msg){
        switch (msg){
            case "exit":
                //todo set kardan boolean exit ba false

            default:
                System.out.println(msg);
        }
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
class Send implements Runnable{
    private Socket socket;
    private Client client;
    private boolean exit;
    private PrintWriter printWriter;

    public Send(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.exit =false;
        try {
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner =new Scanner(System.in);
        while (!exit) {
            if (scanner.hasNextLine()) {
                printWriter.println(scanner.nextLine());
            }
        }
    }
}