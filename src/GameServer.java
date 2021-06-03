import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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
            sendToAll("Server: tedad kam ast.\nbye bye");
            closeAll();
            System.exit(1);
        }
        persons = PersonFactory.createPersons(numberOfPlayers);
    }

    public void closeAll(){
        for (Handler temp:clients) {
            temp.closeSocket();
        }
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
        TimerMe timerMe = new TimerMe(60);
        Thread time = new Thread(timerMe);
        time.start();
        while (!timerMe.isEnd()) {
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
        distributionOfRoles(persons);

    }

    public void distributionOfRoles(ArrayList<Person> persons){
        ArrayList<Person> temp = persons;
        Collections.shuffle(temp);
        for (int i = 0 ; i<persons.size() ; i++) {
            Person role=persons.get(i);
            clients.get(i).setPerson(role);
            sendMsg("naghshe shoma : "+ role.getClass().getName(),clients.get(i).getSocket());
        }
    }

    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Handler temp:clients) {
            names.add(temp.getName());
        }
        return names;
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
    private Scanner scanner;
    private PrintWriter printWriter;

    public Handler(Socket socket, String name, GameServer gameServer) {
        this.socket = socket;
        this.name = name;
        this.gameServer = gameServer;
        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void getNameFromClient(){
        String name = null;
        do {
            gameServer.sendMsg("name ra vared konid : ",socket);
            name=scanner.nextLine().strip();
        }while (gameServer.getNames().contains(name));
        setName(name);
    }

    @Override
    public void run() {
        getNameFromClient();
        while (true) {
            if (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                gameServer.sendToAll(this, name+" : "+msg);
                System.out.println(msg);
            }
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