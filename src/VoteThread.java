import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * class vote thread for vote each player.
 *
 * @author Erfanm5152
 */
public class VoteThread implements Runnable {
    // client that want to vote.
    private Handler client;
    // serve of the game.
    private GameServer gameServer;

    /**
     * create a new Vote thread.
     *
     * @param client     the client that want to vote
     * @param gameServer the server of the game.
     */
    public VoteThread(Handler client, GameServer gameServer) {
        this.client = client;
        this.gameServer = gameServer;
    }

    /**
     * for chose someone to vote.
     */
    @Override
    public void run() {
        try {
            Scanner scanner = client.getScanner();
            String votedName = scanner.nextLine().strip();
            System.out.println(votedName + "\t" + client.getName());
            for (Handler temp : gameServer.getClients()) {
                if (temp.getName().equals(votedName)) {
                    temp.getPerson().addVote(client.getName());
                }
            }
        } catch (NoSuchElementException e) {// for disconnection
            client.getPerson().setAlive(false);
            System.out.println("dead<---");
        }
    }

}
