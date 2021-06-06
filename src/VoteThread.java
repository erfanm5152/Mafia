import java.util.NoSuchElementException;
import java.util.Scanner;

public class VoteThread implements Runnable{
    private Handler client;
    private GameServer gameServer;

    public VoteThread(Handler client, GameServer gameServer) {
        this.client = client;
        this.gameServer = gameServer;
    }

    @Override
    public void run() {
        Scanner scanner = client.getScanner();
        try {
            String votedName = scanner.nextLine().strip();
            System.out.println(votedName+"\t"+client.getName());
            for (Handler temp : gameServer.getClients()) {
                if (temp.getName().equals(votedName)) {
                    temp.getPerson().addVote(client.getName());
                }
            }
        }catch (NoSuchElementException e){
            System.out.println();
        }
        finally {
            client.setExit(true);
        }
    }

}
