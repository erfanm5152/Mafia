import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;


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
            stopAll();
            System.exit(1);
        }
        persons = PersonFactory.createPersons(numberOfPlayers);
    }

    public void closeAll() {
        for (Handler temp : clients) {
            temp.closAll();
        }
    }

    public synchronized void sendToAll(String msg) {
        for (Handler temp : clients) {
            sendMsg(msg, temp);
        }
    }

    public synchronized void sendToAll(Handler sender, String msg) {
        for (Handler temp : clients) {
            if (!temp.equals(sender)) {
                sendMsg(msg, temp);
            }
        }
    }

    public synchronized void sendMsg(String msg, Handler client) {
        client.getPrintWriter().println(msg);
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
        System.out.println("port : " + port);
        System.out.println("server montazer ast.");
        int i = 0;
        numberOfPlayers = 1;

        while (i < numberOfPlayers) {
            Socket socket = welcomingSocket.accept();
            System.out.println("client vasl shod.");
            Handler client = new Handler(socket, "", this);
            if (i == 0) {
                setNumberOfPlayers(client);
            }
            clients.add(client);
            new Thread(client).start();
            sendMsg("be server man khosh amdid.", client);
            i++;
        }
        System.out.println("tamam");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initialize();
        distributionOfRoles(persons);
        gameLoop();
    }

    public void setClients(ArrayList<Handler> clients) {
        this.clients = clients;
    }

    public void gameLoop() {
        sendToAll(help());
        int i=0;
        while (!finish()) {
            try {
                Thread.sleep(20000);// zaman sohbat karddan
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("aaaaaaaaaaaaaaaa");
            //voting time
            new Voting(this).startVoting();
            //night
        }
        stopAll();

    }

    public void voting(){
       sendToAll("zamane ray giri fara resid. ");
       sendToAll("players: "+getNames().toString());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printVoteList();
    }

    public void stopAll() {
        sendToAll("exit");
        closeAll();
        System.exit(1);
    }



    public void night() {
        sendToAll("shab mishavad\n" +
                "va hame be khab miravand.");
    }

    public void removeUnconnected() {
        Iterator<Handler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Handler temp = iterator.next();
            if (!temp.isConnected()) {
                sendToAll(temp.getName() + "az bazi kharej shod.");
                iterator.remove();
            }
        }
    }

    public String help() {
        return "az hala be bad shoma be onvoan yek mafia ya shahrvand dar bazi hozor darid\n va bayad talash konid ke" +
                "team shoma barande shavad.";
    }

    public void setNumberOfPlayers(Handler firstClient) {
        sendMsg("tedad bazikon ha ra vared konid: ", firstClient);
        this.numberOfPlayers = Integer.parseInt(firstClient.getScanner().nextLine());
    }

    public void distributionOfRoles(ArrayList<Person> persons) {
        ArrayList<Person> temp = persons;
        Collections.shuffle(temp);
        for (int i = 0; i < persons.size(); i++) {
            Person role = persons.get(i);
            clients.get(i).setPerson(role);
            sendMsg("naghshe shoma : " + role.getClass().getName(), clients.get(i));
        }
    }


    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Handler temp : clients) {
            names.add(temp.getName());
        }
        return names;
    }

    public void printVoteList(){
        sendToAll(getVotes());
    }

    public boolean finish() {
        if (numberOfMafia() == 0 || numberOfMafia() >= numberOfCitizen()) {
            return true;
        }
        return false;
    }

    public int numberOfCitizen() {
        int counter = 0;
        for (Handler temp : clients) {
            if (temp.getPerson() instanceof Citizen) {
                counter++;
            }
        }
        return counter;
    }

    public int numberOfMafia() {
        int counter = 0;
        for (Handler temp : clients) {
            if (temp.getPerson() instanceof Mafia) {
                counter++;
            }
        }
        return counter;
    }

    public ArrayList<Handler> getClients() {
        return clients;
    }

    public synchronized String getVotes(){
        String temp="";
        for (Handler client:clients) {
            temp+=client.getName()+" "+client.getPerson().getVotes()+"\n";
        }
        return temp;
    }
    public synchronized void vote(Handler client,String votedName){
        removeVote(client);
        for (Handler temp : clients) {
            if (temp.getName().equals(votedName)){
                temp.getPerson().addVote(client.getName());
            }
        }
    }
    public void removeVote(Handler client){
        for (Handler temp:clients) {
            temp.getPerson().removeVote(client.getName());
        }
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
    private boolean exit;
    private boolean isScan;
    private boolean isFirst;
    public Handler(Socket socket, String name, GameServer gameServer) {
        this.socket = socket;
        this.name = name;
        this.gameServer = gameServer;
        this.exit = false;
        this.isScan=true;
        this.isFirst = true;
        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler(Handler handler) {
        this(handler.getSocket(), handler.getName(), handler.gameServer);
        this.person = handler.person;
        this.isFirst =false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void getNameFromClient() {
        String name = null;
        do {
            printWriter.println("name ra vared konid : ");
            name = scanner.nextLine().strip();
        } while (gameServer.getNames().contains(name));
        setName(name);
    }

    @Override
    public void run() {
        if (isFirst) {
            getNameFromClient();
        }
        while (!exit) {
            while (isScan) {
                if (scanner.hasNextLine()) {
                    String msg = scanner.nextLine();
                    checkMsg(msg);
                    System.out.println(msg);
                }
            }
        }
    }

    public void checkMsg(String msg) {
        if (msg.equals("VoTe")){
            isScan=false;
        }else {
            Date date = new Date();
            gameServer.sendToAll(this, name + " : " + msg
                    + "\t(" + date.getHours() + ":" + date.getMinutes() + ")");
        }
    }
    public void vote(String msg){
        String votedName= votedName(msg);
        gameServer.vote(this,votedName);
    }
    public String votedName(String msg){
        String temp="";
        String[] msgParts= msg.split(" ");
        ArrayList<String> parts= new ArrayList<String>(Arrays.asList(msgParts));
        parts.remove("vote");
        parts.remove("to");
        for (String string:parts) {
            temp+=string+" ";
        }
        temp+="\b";
        System.out.println(temp);
        return temp;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isScan() {
        return isScan;
    }

    public void setIsScan(boolean scan) {
        isScan = scan;
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
    public void closeScanner(){
        scanner.close();
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void closAll() {
        setExit(true);
        scanner.close();
        printWriter.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Handler handler = (Handler) o;
        return Objects.equals(socket, handler.socket) && Objects.equals(person, handler.person) && Objects.equals(name, handler.name) && Objects.equals(gameServer, handler.gameServer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, person, name, gameServer, scanner, printWriter, exit, isScan, isFirst);
    }
}