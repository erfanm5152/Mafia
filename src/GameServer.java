import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    private int numberOfPlayers;
    private int port;
    private ServerSocket welcomingSocket;
    private ArrayList<Handler> clients;
    private ArrayList<Person> persons;

    public GameServer() {
        setServerSocket();
        clients = new ArrayList<>();
        persons = new ArrayList<>();
    }

    public void initialize() {
        if (numberOfPlayers < 5) {
            sendToAll("tedad kam ast.");
            System.exit(1);
        }
        persons = PersonFactory.createPersons(numberOfPlayers);
    }

    public synchronized void sendToAll(String msg) {
        for (Handler temp : clients) {
            sendMsg(msg, temp.getSocket());
        }
    }

    public synchronized void sendToAll(Handler sender, String msg) {
        for (Handler temp : clients) {
            if (!temp.equals(sender)) {
                sendMsg(msg, temp.getSocket());
            }
        }
    }

    public synchronized void sendMsg(String msg, Socket socket) {
        try {
            PrintWriter outputStream = new PrintWriter((socket.getOutputStream()), true);
            outputStream.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setServerSocket() {
        do {
            setPort();
            try {
                this.welcomingSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (welcomingSocket == null);
    }

    private void setPort() {
        this.port = new Random().nextInt(10000) + 10000;
    }

    public void startServer() throws IOException {
        ExecutorService pool = Executors.newCachedThreadPool();
        System.out.println("port : " + port);
        System.out.println("server montazer ast.");
        TimerMe t = new TimerMe(200);
        new Thread(t).start();
        while (!t.isEnd()) {
            Socket socket = welcomingSocket.accept();
            System.out.println("client vasl shod.");
            Handler client = new Handler(socket, "", this);
            clients.add(client);
            numberOfPlayers++;
            pool.execute(client);
            sendMsg("be server man khosh amdid.", client.getSocket());
        }
        System.out.println("tamam");
        initialize();

    }


    public static void main(String[] args) {
        try {
            new GameServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Handler implements Runnable {
    private Socket socket;
    private Person person;
    private String name;
    private GameServer gameServer;

    public Handler(Socket socket, String name, GameServer gameServer) {
        this.socket = socket;
        this.name = name;
        this.gameServer = gameServer;
    }


    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            while (true) {
                if (scanner.hasNextLine()) {
                    String msg = scanner.nextLine();
                    gameServer.sendToAll(this, msg);
                    System.out.println(msg);
                    if (name.equals("")){
                        name=msg.split(":")[0].strip();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

}