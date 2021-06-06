import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.SocketException;
import java.util.*;


public class GameServer {

    private int numberOfPlayers;
    private int port;
    private ServerSocket welcomingSocket;
    private ArrayList<Handler> clients;
    private ArrayList<Person> persons;
    private boolean isValidVoting;

    public GameServer() {
        setServerSocket();
        clients = new ArrayList<>();
        persons = new ArrayList<>();
        this.isValidVoting = true;
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
        if (client.isConnected()) {
            client.getPrintWriter().println(msg);
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
    public void sendToLives(String msg){
        for (Handler handler:clients) {
            if (handler.getPerson().isAlive() && handler.isConnected()){
                sendMsg(msg,handler);
            }
        }
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
        while (!finish()) {
            refreshVotes();
            try {
                Thread.sleep(20000);// zaman sohbat karddan
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("aaaaaaaaaaaaaaaa");
            //voting time
            new Voting(this).startVoting();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isValidVoting){
                votedDeath();
            }else {
                sendToAll("mayor ray giri ra molgha kard.");
            }
            //night
        }
        stopAll();
    }

    public void votedDeath(){
        ArrayList<Handler> voted = votedClients();
        Random random =new Random();
        Handler handler = voted.get(random.nextInt(voted.size()));
        sendToAll(handler.getName()+" ba ray giri kharej shod.");
        handler.getPerson().setAlive(false);
        sendMsg("baraye khoroj az bazi \"exit\" ra vared konid.",handler);
    }

    public ArrayList<Handler> votedClients(){
        ArrayList<Handler> temp= new ArrayList<>();
        int maxVote=0;

        for (Handler handler: clients) {
            if (handler.getPerson().isAlive()){
                if (handler.getPerson().numberOfVotes() > maxVote){
                    maxVote=handler.getPerson().numberOfVotes();
                }
            }
        }
        for (Handler handler : clients) {
            if (handler.getPerson().numberOfVotes() == maxVote && handler.getPerson().isAlive()){
                temp.add(handler);
            }
        }
        return temp;
    }

    public void refreshVotes(){
        for (Handler temp :clients) {
            temp.getPerson().refreshVotes();
        }
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
            if (temp.getPerson()==null||temp.getPerson().isAlive()) {
                names.add(temp.getName());
            }
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
            if (temp.getPerson() instanceof Citizen && temp.getPerson().isAlive()) {
                counter++;
            }
        }
        return counter;
    }

    public int numberOfMafia() {
        int counter = 0;
        for (Handler temp : clients) {
            if (temp.getPerson() instanceof Mafia && temp.getPerson().isAlive()) {
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
            if (client.getPerson().isAlive()) {
                temp += client.getName() + " " + client.getPerson().getVotes() + "\n";
            }
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
    public void removeClient(Handler client){
        clients.remove(client);
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
    private boolean isConnected;
    public Handler(Socket socket, String name, GameServer gameServer) {
        this.socket = socket;
        this.name = name;
        this.gameServer = gameServer;
        this.exit = false;
        this.isScan=true;
        this.isFirst = true;
        this.isConnected = true;
        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        }catch (SocketException e){
            isConnected =false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler(Handler handler) {
        this(handler.getSocket(), handler.getName(), handler.gameServer);
        this.person = handler.person;
        this.isFirst =false;
        this.isConnected=handler.isConnected();
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
//        try {
            while (!exit) {
                while (isScan) {
                    if (isConnected && scanner.hasNextLine()) {
                        String msg = scanner.nextLine();
                        checkMsg(msg);
                        System.out.println(msg);
                    }
                }
            }
//        }catch (IllegalStateException e){
//            System.out.println(getName()+ "az bazi kharej shod.");
//        }
    }

    public void checkMsg(String msg) {
        if (msg.equals("VoTe")){
            isScan=false;
        }else if (msg.equals("spurious vote")){

        }else if (msg.equals("exit")){
//            gameServer.removeVote(this);
            printWriter.println("exit");
            closAll();
        }
        else {
            if ( person==null || (person.isAlive()&& !person.isMuted())) {
                Date date = new Date();
                gameServer.sendToAll(this, name + " : " + msg
                        + "\t(" + date.getHours() + ":" + date.getMinutes() + ")");
            }
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
        if (!socket.isConnected()){
            return false;
        }
        return isConnected;
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
        this.isConnected=false;
        setExit(true);
        if (scanner !=null){
            scanner.close();
        }

        if (printWriter!=null) {
            printWriter.close();
        }
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