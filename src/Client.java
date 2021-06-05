import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
            this.socket = new Socket("127.0.0.1",port);
            System.out.println("vasl shod");
            receive =new Receive(socket , this);
//            send= new Thread(new Send(socket,this));
            send = new Send(socket,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
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

    public void sendExit(){
        ((Send)send).stop();
    }
    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            while (!exit){
                //todo اگر پیام شامل بای بای و سرور بود از برنامه خارج شو
                if (scanner.hasNextLine()){
                    String msg = scanner.nextLine();
                    checkMsg(msg);
                }
            }
    }
    public void checkMsg(String msg){
        switch (msg){
            case "exit":
                //todo set kardan boolean exit ba false
                exit();
                break;
            default:
                System.out.println(msg);
        }
    }
    public void exit(){
        client.sendExit();
        stop();
        client.closeSocket();
        System.exit(1);
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
    public void stop(){
        this.exit=true;
        scanner.close();
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

    public void setExit(boolean exit) {
        this.exit = exit;
        if (exit){
            printWriter.close();
        }
    }
    public void stop(){
        this.exit = true;
        this.printWriter.close();

    }
}