import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class Handler implements Runnable {
    private Socket socket;
    private Person person;
    private String name;
    private GameServer gameServer;
    private Scanner scanner;
    private PrintWriter printWriter;
    private boolean isStart;
    private boolean exit;
    private boolean isScan;
    private boolean isFirst;
    private boolean isConnected;

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

    public Handler(Handler handler) {
        this(handler.getSocket(), handler.getName(), handler.gameServer);
        this.person = handler.person;
        this.person.setHandler(this);
        this.isStart = true;
        this.isFirst = false;
        this.isConnected = handler.isConnected();
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
        if (msg.equals("VoTe") || msg.equals("MayorRole") || msg.equals("***PlayRole***")) {
            isScan = false;
        } else if (msg.equals("spurious vote") || msg.equals("spurious choice") || msg.equals("spurious role")) {

        } else if (msg.equals("exit")) {
//            gameServer.removeVote(this);
            printWriter.println("exit");
            gameServer.sendToAll(name + " az bazi kharej shod.");
            person.setAlive(false);
            closAll();
        } else if (msg.equals("history")) {
            gameServer.sendMsg(gameServer.getFile().read(), this);
        } else {
            if (msg.equalsIgnoreCase("start")) {
                this.isStart = true;
            }
            if (person == null || (person.isAlive() && !person.isMuted() && !person.isPsychologicalSilence())) {
                Date date = new Date();
                String newMsg = name + " : " + msg + "\t(" + (("" + date.getHours()).length() < 2 ? ("0" + date.getHours()) : date.getHours())
                        + ":" + (("" + date.getMinutes()).length() < 2 ? ("0" + date.getMinutes()) : date.getMinutes()) + ")";
                gameServer.sendToAll(this, newMsg);
                gameServer.getFile().write(newMsg);
            }
        }
    }

    public void vote(String msg) {
        String votedName = votedName(msg);
        gameServer.vote(this, votedName);
    }

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

    public boolean isConnected() {
        if (!socket.isConnected()) {
            return false;
        }
        return isConnected;
    }

    public boolean isStart() {
        return isStart;
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

    public void closeScanner() {
        scanner.close();
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }


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

    public GameServer getGameServer() {
        return gameServer;
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