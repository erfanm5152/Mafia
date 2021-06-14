import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.SocketException;
import java.util.*;


/**
 * The type Game server.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class GameServer {
    // the number of players
    private int numberOfPlayers;
    // port number of Server socket
    private int port;
    // socket of the server
    private ServerSocket welcomingSocket;
    // list of the clients of the server
    private ArrayList<Handler> clients;
    // List of people in the game.
    private ArrayList<Person> persons;
    // for check validity of voting
    private boolean isValidVoting;
    // for introduction
    private boolean isIntroduction;
    // for check inquiry
    private boolean isInquiry;
    // for save the messages
    private SaveFile file;

    /**
     * Instantiates a new Game server.
     */
    public GameServer() {
        setServerSocket();
        clients = new ArrayList<>();
        persons = new ArrayList<>();
        file = new SaveFile("" + port);
        this.isValidVoting = true;
        this.isIntroduction = true;
        this.isInquiry = true;
    }

    /**
     * Initialize.
     * create list of persons.
     */
    public void initialize() {
        if (numberOfPlayers < 5) {
            sendToAll("Server: tedad kam ast.\nbye bye");
            sendToAll("exit");
            stopAll();
            System.exit(1);
        }
        persons = PersonFactory.createPersons(numberOfPlayers);
    }

    /**
     * Close all.
     */
    public void closeAll() {
        for (Handler temp : clients) {
            temp.closAll();
        }
    }

    /**
     * Send to all from the server.
     *
     * @param msg the msg
     */
    public synchronized void sendToAll(String msg) {
        for (Handler temp : clients) {
            sendMsg(msg, temp);
        }
    }

    /**
     * Send to all from a client .
     *
     * @param sender the sender
     * @param msg    the msg
     */
    public synchronized void sendToAll(Handler sender, String msg) {
        for (Handler temp : clients) {
            if (!temp.equals(sender)) {
                sendMsg(msg, temp);
            }
        }
    }

    /**
     * Send msg to a client.
     *
     * @param msg    the msg
     * @param client the client
     */
    public synchronized void sendMsg(String msg, Handler client) {
        if (client.isConnected()) {
            client.getPrintWriter().println(msg);
        }
    }

    /**
     * create server socket.
     */
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

    /**
     * Send to lives.
     *
     * @param msg the msg
     */
    public void sendToLives(String msg) {
        for (Handler handler : clients) {
            if (handler.getPerson().isAlive() && handler.isConnected()) {
                sendMsg(msg, handler);
            }
        }
    }

    /**
     * set port of the server with random number.
     */
    private void setPort() {
        this.port = new Random().nextInt(10000) + 10000;
    }

    /**
     * Start server.
     *
     * @throws IOException the io exception
     */
    public void startServer() throws IOException {
        System.out.println("port : " + port);
        System.out.println("server montazer ast.");
        int i = 0;
        numberOfPlayers = 1;

        while (i < numberOfPlayers) {
            Socket socket = welcomingSocket.accept();
            System.out.println("client vasl shod.");
            Handler client = new Handler(socket, "", this);
            if (i == 0) {// get number of players from the first client
                setNumberOfPlayers(client);
            }
            clients.add(client);
            new Thread(client).start();
            sendMsg("be server man khosh amdid.", client);
            i++;
        }
        System.out.println("tamam");
        startGame();
    }

    /**
     * Start game.
     */
    public void startGame() {
        while (true) {
            if (isStart()) {
                System.out.println("STRT");
                break;
            }
        }
        initialize();
        distributionOfRoles(persons);
        gameLoop();
    }

    /**
     * Is start boolean.
     * check readiness of the players
     * @return the boolean
     */
    public boolean isStart() {
        boolean temp = true;
        for (Handler handler : clients) {
            temp = temp && handler.isStart();
        }
        return temp;
    }

    /**
     * Sets clients.
     *
     * @param clients the clients
     */
    public void setClients(ArrayList<Handler> clients) {
        this.clients = clients;
    }

    /**
     * Game loop.
     */
    public void gameLoop() {
        int i = 1;
        sendToAll(help());
        while (!finish()) {
            sendToAll("-----DAY" + i + "-----");
            file.write("-----DAY" + i + "-----");
            announce();
            if (isRoleInGame("Psychologist")) {
                sendNewsOfPsychologist();
            }
            refreshVotes();
            unMute();

            if (isInquiry) {
                inquiry();
            }
            try {
                Thread.sleep(20000);// time to argue
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            removeUnconnected();
            //voting time
            new Voting(this).startVoting();
            refreshSettings();
            if (isRoleInGame("Mayor")) {
                mayorMove();
            }
            try {
                Thread.sleep(10000);// time for voting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isValidVoting) {
                votedDeath();
            } else {
                sendToAll("mayor ray giri ra molgha kard.");
            }
            if (finish()) {
                break;
            }
            unMutePsychologistRolePlay();
            setGodFatherSuccessor();
            //night
            removeUnconnected();
            sendToAll("-----NIGHT" + i + "-----");
            new Night(this).start();

            i++;
        }
        end();
        stopAll();
    }

    /**
     * Send news of psychologist.
     */
    public void sendNewsOfPsychologist() {
        for (Handler handler : clients) {
            if (handler.getPerson().isPsychologicalSilence()) {
                sendMsg("shoma baraye yek rooz saket shodid.", handler);
                sendToAll(handler, handler.getName() + " baraye yek rooz tavastot ravanshenas saket shod.");
            }
        }
    }


    /**
     * Un mute psychologist role play.
     */
    public void unMutePsychologistRolePlay() {
        for (Handler handler : clients) {
            if (handler.getPerson().isAlive()) {
                handler.getPerson().setPsychologicalSilence(false);
            }
        }
    }

    /**
     * Announce the status of the players.
     */
    public void announce() {
        String lives = "";
        String dead = "";
        for (Handler handler : clients) {
            if (handler.getPerson().isAlive()) {
                lives += handler.getName() + " ,";
            } else {
                dead += handler.getName() + " ,";
            }
        }
        lives += '\b';
        dead += '\b';
        sendToAll("lives : " + lives + '\n' + "dead : " + dead);
    }

    /**
     * Inquiry for die hard role.
     */
    public void inquiry() {
        String lives = "";
        String dead = "";
        for (Person person : persons) {
            if (person.isAlive()) {
                lives += person + " ,";
            } else {
                dead += person + " ,";
            }
        }
        lives += '\b';
        dead += '\b';
        sendToAll("lives : " + lives + '\n' + "dead : " + dead);
    }

    /**
     * Refresh settings.
     */
    public void refreshSettings() {
        isValidVoting = true;
        isInquiry = false;
    }

    /**
     * Un mute players after night.
     */
    public void unMute() {
        for (Handler handler : clients) {
            if (handler.getPerson().isAlive()) {
                handler.getPerson().setMuted(false);
            }
        }
    }

    /**
     * Gets handler by name.
     *
     * @param name the name
     * @return the handler by name
     */
    public Handler getHandlerByName(String name) {
        for (Handler handler : clients) {
            if (handler.getName().equalsIgnoreCase(name)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * Is name in game boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isNameInGame(String name) {
        return getNames().contains(name);
    }


    /**
     * Mute everyone.
     */
    public void muteEvery() {
        for (Handler handler : clients) {
            handler.getPerson().setMuted(true);
        }
    }

    /**
     * Introduction night.
     */
    public void introduction() {
        sendMsgToMafia(mafiaIntroduction());
        if (isRoleInGame("Mayor")) {
            sendMsg("Doctor : " + getRoleHandler("Doctor").getName()
                    , getRoleHandler("Mayor"));
        }
    }

    /**
     * Mafia introduction string.
     *
     * @return the string
     */
    public String mafiaIntroduction() {
        String temp = "Mafia:\n";
        for (Handler handler : clients) {
            if (handler.getPerson() instanceof Mafia) {
                temp += handler.getName() + " : " + handler.getPerson().toString() + '\n';
            }
        }
        return temp;
    }

    /**
     * Send msg to mafia from the server.
     *
     * @param msg the msg
     */
    public void sendMsgToMafia(String msg) {
        for (Handler handler : clients) {
            if (handler.getPerson() instanceof Mafia) {
                sendMsg(msg, handler);
            }
        }
    }

    /**
     * Send msg to mafia from a mafia.
     *
     * @param msg    the msg
     * @param sender the sender
     */
    public void sendMsgToMafia(String msg, Handler sender) {
        for (Handler handler : clients) {
            if (handler.getPerson() instanceof Mafia && !handler.equals(sender)) {
                sendMsg(msg, handler);
            }
        }
    }

    /**
     * Mayor move.
     */
    public void mayorMove() {
        sendToAll("nobat shahrdar ast.");
        Handler mayor = getRoleHandler("Mayor");
        sendMsg("MayorRole", mayor);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(mayor.getPerson()).start();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMsg("spurious choice", mayor);
        mayor.setExit(true);
        mayor = new Handler(mayor);
        new Thread(mayor).start();
    }

    /**
     * Gets role handler.
     *
     * @param nameOfRole the name of role
     * @return the role handler
     */
    public Handler getRoleHandler(String nameOfRole) {
        for (Handler handler : clients) {
            if (handler.getPerson().toString().equals(nameOfRole)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * Is introduction boolean.
     *
     * @return the boolean
     */
    public boolean isIntroduction() {
        return isIntroduction;
    }

    /**
     * Is inquiry boolean.
     *
     * @return the boolean
     */
    public boolean isInquiry() {
        return isInquiry;
    }

    /**
     * Sets introduction.
     *
     * @param introduction the introduction
     */
    public void setIntroduction(boolean introduction) {
        isIntroduction = introduction;
    }

    /**
     * Sets inquiry.
     *
     * @param inquiry the inquiry
     */
    public void setInquiry(boolean inquiry) {
        isInquiry = inquiry;
    }

    /**
     * Is role in game boolean.
     *
     * @param roleName the role name
     * @return the boolean
     */
    public boolean isRoleInGame(String roleName) {
        for (Handler handler : clients) {
            if (handler.getPerson().toString().equals(roleName) && handler.getPerson().isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * End of the game message.
     */
    public void end() {
        sendToAll(namesAndRoles());
        if (winCitizens()) {
            sendToAll("shahr piroz shod !!!");
        }
        if (winMafia()) {
            sendToAll("Mafia shahr ra shekast dad!!!");
        }
    }

    /**
     * Names and roles string.
     *
     * @return the string
     */
    public String namesAndRoles() {
        String temp = "";
        for (Handler handler : clients) {
            temp += handler.getName() + " : " + handler.getPerson().toString() + "\n";
        }
        return temp;
    }

    /**
     * Voted death.
     * To kill someone who voted
     */
    public void votedDeath() {
        ArrayList<Handler> voted = votedClients();
        if (voted.get(0).getPerson().numberOfVotes() == 0) {
            sendToAll("kasi az bazi kharej nashod.");
            return;
        }
        Random random = new Random();
        Handler handler = voted.get(random.nextInt(voted.size()));
        if (handler.getPerson().isAlive()) {
            sendToAll(handler.getName() + " ba ray giri kharej shod.");
            handler.getPerson().setAlive(false);
            sendMsg("baraye khoroj az bazi \"exit\" ra vared konid.", handler);
        }
    }

    /**
     * Voted clients array list.
     *
     * @return the array list of Those who voted
     */
    public ArrayList<Handler> votedClients() {
        ArrayList<Handler> temp = new ArrayList<>();
        int maxVote = 0;

        for (Handler handler : clients) {
            if (handler.getPerson().isAlive()) {
                if (handler.getPerson().numberOfVotes() > maxVote) {
                    maxVote = handler.getPerson().numberOfVotes();
                }
            }
        }
        for (Handler handler : clients) {
            if (handler.getPerson().numberOfVotes() == maxVote && handler.getPerson().isAlive()) {
                temp.add(handler);
            }
        }
        return temp;
    }

    /**
     * Refresh votes.
     */
    public void refreshVotes() {
        for (Handler temp : clients) {
            temp.getPerson().refreshVotes();
        }
    }

    /**
     * Voting.
     */
    public void voting() {
        sendToAll("zamane ray giri fara resid. ");
        sendToAll("players: " + getNames().toString());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printVoteList();
    }

    /**
     * Stop all.
     * and exit from app
     */
    public void stopAll() {
        sendToAll("exit");
        closeAll();
        file.close();
        System.exit(1);
    }


    /**
     * Gets file.
     *
     * @return the file
     */
    public SaveFile getFile() {
        return file;
    }

    /**
     * Remove unconnected plaeyrs.
     */
    public void removeUnconnected() {
        for (Handler handler : clients) {
            if (!handler.isConnected()) {
                handler.getPerson().setAlive(false);
            }
        }
    }

    /**
     * Help string.
     *
     * @return the help message
     */
    public String help() {
        return "az hala be bad shoma be onvoan yek mafia ya shahrvand dar bazi hozor darid\n va bayad talash konid ke" +
                "team shoma barande shavad.";
    }

    /**
     * Sets number of players (for first player).
     *
     * @param firstClient the first client
     */
    public void setNumberOfPlayers(Handler firstClient) {
        sendMsg("tedad bazikon ha ra vared konid: ", firstClient);
        try {
            this.numberOfPlayers = Integer.parseInt(firstClient.getScanner().nextLine());
        } catch (NumberFormatException e) {
            setNumberOfPlayers(firstClient);
        }
    }

    /**
     * Sets god father successor.
     */
    public void setGodFatherSuccessor() {
        if (isRoleInGame("GodFather") || isRoleInGame("GodFatherSuccessor")) {
            return;
        } else {
            for (int i = clients.size() - 1; i >= 0; i--) {
                Handler temp = clients.get(i);
                Person tempPerson = temp.getPerson();
                if (tempPerson instanceof Mafia && tempPerson.isAlive()) {
                    temp.setPerson(new GodFatherSuccessor((Mafia) tempPerson));
                    sendMsg("shoma janeshin GodFather hastid.", temp);
                }
            }
        }
    }

    /**
     * Distribution of roles.
     *
     * @param persons the persons
     */
    public void distributionOfRoles(ArrayList<Person> persons) {
        ArrayList<Person> temp = persons;
        Collections.shuffle(temp);
        Collections.shuffle(clients);

        for (int i = 0; i < persons.size(); i++) {
            Person role = persons.get(i);
            clients.get(i).setPerson(role);
            role.setHandler(clients.get(i));
            sendMsg("naghshe shoma : " + role.toString(), clients.get(i));
        }
        swapGodfatherEndOfMafia();
    }

    /**
     * Swap godfather end of mafia.
     */
    public void swapGodfatherEndOfMafia() {
        int indexFinalMafia = 0;
        int indexGodfather = 0;
        for (int i = numberOfPlayers - 1; i >= 0; i--) {
            if (clients.get(i).getPerson() instanceof Mafia) {
                indexFinalMafia = i;
                break;
            }
        }
        int j = 0;
        for (Handler handler : clients) {
            if (handler.getPerson() instanceof GodFather) {
                indexGodfather = j;
                break;
            }
            j++;
        }
        Collections.swap(clients, indexGodfather, indexFinalMafia);
    }

    /**
     * Gets names.
     *
     * @return the names
     */
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Handler temp : clients) {
            if (temp.getPerson() == null || temp.getPerson().isAlive()) {
                names.add(temp.getName());
            }
        }
        return names;
    }

    /**
     * Print vote list.
     */
    public void printVoteList() {
        sendToAll(getVotes());
    }

    /**
     * Finish boolean.
     *
     * @return the boolean
     */
    public boolean finish() {
        if (winCitizens() || winMafia()) {
            return true;
        }
        return false;
    }

    /**
     * Win citizens boolean.
     *
     * @return the boolean
     */
    public boolean winCitizens() {
        if (numberOfMafia() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Win mafia boolean.
     *
     * @return the boolean
     */
    public boolean winMafia() {
        if (numberOfMafia() >= numberOfCitizen()) {
            return true;
        }
        return false;
    }

    /**
     * Sets valid voting.
     *
     * @param validVoting the valid voting
     */
    public void setValidVoting(boolean validVoting) {
        isValidVoting = validVoting;
    }

    /**
     * Number of citizen int.
     *
     * @return the int
     */
    public int numberOfCitizen() {
        int counter = 0;
        for (Handler temp : clients) {
            if (temp.getPerson() instanceof Citizen && temp.getPerson().isAlive()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Number of mafia int.
     *
     * @return the int
     */
    public int numberOfMafia() {
        int counter = 0;
        for (Handler temp : clients) {
            if (temp.getPerson() instanceof Mafia && temp.getPerson().isAlive()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gets clients.
     *
     * @return the clients
     */
    public ArrayList<Handler> getClients() {
        return clients;
    }

    /**
     * Gets votes.
     *
     * @return the votes
     */
    public synchronized String getVotes() {
        String temp = "";
        for (Handler client : clients) {
            if (client.getPerson().isAlive()) {
                temp += client.getName() + " " + client.getPerson().getVotes() + "\n";
            }
        }
        return temp;
    }

    /**
     * Vote.
     *
     * @param client    the client
     * @param votedName the voted name
     */
    public synchronized void vote(Handler client, String votedName) {
        removeVote(client);
        for (Handler temp : clients) {
            if (temp.getName().equals(votedName)) {
                temp.getPerson().addVote(client.getName());
            }
        }
    }

    /**
     * Remove vote.
     *
     * @param client the client
     */
    public void removeVote(Handler client) {
        for (Handler temp : clients) {
            temp.getPerson().removeVote(client.getName());
        }
    }

    /**
     * Remove client.
     *
     * @param client the client
     */
    public void removeClient(Handler client) {
        clients.remove(client);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            new GameServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


