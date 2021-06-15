import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * The type Handler for handle connection between server & clients in multi thread mode.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Handler implements Runnable {
    // socket of the connection
    private Socket socket;
    // role of the client
    private Person person;
    // name of the client
    private String name;
    // server of the game
    private GameServer gameServer;
    // scanner for read messages
    private Scanner scanner;
    // writer for write messages
    private PrintWriter printWriter;
    // for check readiness for start the game
    private boolean isStart;
    // for exit from game
    private boolean exit;
    // for check scanning
    private boolean isScan;
    // for check getName method
    private boolean isFirst;
    // for check connection
    private boolean isConnected;

    /**
     * Instantiates a new Handler.
     *
     * @param socket     the socket
     * @param name       the name
     * @param gameServer the game server
     */
    public Handler(Socket socket, String name, GameServer gameServer) {
        this.socket = socket;
        this.name = name;
        this.gameServer = gameServer;
        this.isStart = false;
        this.exit = false;
        this.isScan = true;
        this.isFirst = true;
        this.isConnected = true;
        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (SocketException e) {
            isConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiates a new Handler.
     *
     * @param handler the handler
     */
    public Handler(Handler handler) {
        this(handler.getSocket(), handler.getName(), handler.gameServer);
        this.person = handler.person;
        this.person.setHandler(this);
        this.isStart = true;
        this.isFirst = false;
        this.isConnected = handler.isConnected();
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets person.
     *
     * @param person the person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Gets name from client.
     */
    public void getNameFromClient() {
        String name = null;
        do {
            printWriter.println("name ra vared konid : ");
            name = scanner.nextLine().strip();
        } while (gameServer.getNames().contains(name));
        setName(name);
    }

    /**
     * for receive messages from the client.
     */
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

    /**
     * Check msg & execute the corresponding command..
     *
     * @param msg the msg
     */
    public void checkMsg(String msg) {
        if (msg.equals("VoTe") || msg.equals("MayorRole") || msg.equals("***PlayRole***")) {// to turn off the scanner
            isScan = false;
        } else if (msg.equals("spurious vote") || msg.equals("spurious choice") || msg.equals("spurious role")) {
            // receive fake choices
        } else if (msg.equals("exit")) {// for exit from server
//            gameServer.removeVote(this);
            printWriter.println("exit");
            gameServer.sendToAll(name + " az bazi kharej shod.");
            person.setAlive(false);
            closAll();
        } else if (msg.equals("history")) {// for read from file and show history of messages
            gameServer.sendMsg(gameServer.getFile().read(), this);
        } else {// this part for messages that should send to all
            if (msg.equalsIgnoreCase("start")) { // To declare readiness
                this.isStart = true;
            }
            if (person == null || (person.isAlive() && !person.isMuted() && !person.isPsychologicalSilence())) {
                // send message to all
                Date date = new Date();
                String newMsg = name + " : " + msg + "\t(" + (("" + date.getHours()).length() < 2 ? ("0" + date.getHours()) : date.getHours())
                        + ":" + (("" + date.getMinutes()).length() < 2 ? ("0" + date.getMinutes()) : date.getMinutes()) + ")";
                gameServer.sendToAll(this, newMsg);
                gameServer.getFile().write(newMsg);
            }
        }
    }

    /**
     * Vote.
     *
     * @param msg the msg
     */
    public void vote(String msg) {
        String votedName = votedName(msg);
        gameServer.vote(this, votedName);
    }

    /**
     * Voted name string.
     *
     * @param msg the msg
     * @return the string
     */
    public String votedName(String msg) {
        String temp = "";
        String[] msgParts = msg.split(" ");
        ArrayList<String> parts = new ArrayList<String>(Arrays.asList(msgParts));
        parts.remove("vote");
        parts.remove("to");
        for (String string : parts) {
            temp += string + " ";
        }
        temp += "\b";
        System.out.println(temp);
        return temp;
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected() {
        if (!socket.isConnected()) {
            return false;
        }
        return isConnected;
    }

    /**
     * Is start boolean.
     *
     * @return the boolean
     */
    public boolean isStart() {
        return isStart;
    }

    /**
     * Gets scanner.
     *
     * @return the scanner
     */
    public Scanner getScanner() {
        return scanner;
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
     * Sets exit.
     *
     * @param exit the exit
     */
    public synchronized void setExit(boolean exit) {
        this.exit = exit;
    }

    /**
     * Is scan boolean.
     *
     * @return the boolean
     */
    public boolean isScan() {
        return isScan;
    }

    /**
     * Sets is scan.
     *
     * @param scan the scan
     */
    public void setIsScan(boolean scan) {
        isScan = scan;
    }

    /**
     * Gets person.
     *
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets print writer.
     *
     * @return the print writer
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    /**
     * Close scanner.
     */
    public void closeScanner() {
        scanner.close();
    }

    /**
     * Sets scanner.
     *
     * @param scanner the scanner
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }


    /**
     * Clos all streams.
     */
    public void closAll() {
        this.isConnected = false;
        setExit(true);
        if (scanner != null) {
            scanner.close();
        }

        if (printWriter != null) {
            printWriter.close();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets game server.
     *
     * @return the game server
     */
    public GameServer getGameServer() {
        return gameServer;
    }

    /**
     * check equality of two handlers
     * @param o second handler
     * @return true or false
     */
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