import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * class for voting time to start vote thread for each player.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Voting {
    // The server where the game is being played.
    private GameServer gameServer;


    /**
     * create new voting by GameServer
     *
     * @param gameServer the game server where game is playing.
     */
    public Voting(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    /**
     * for start voting in server.
     */
    public synchronized void startVoting() {
        ExecutorService pool = Executors.newCachedThreadPool();
        gameServer.sendToAll("zamane ray giri fara resid. ");
        gameServer.sendToLives("players: " + gameServer.getNames().toString());
        for (Handler handler : gameServer.getClients()) {
            if (handler.getPerson().isAlive()) {
                pool.execute(new VoteThread(handler, gameServer));
            }
        }
        try {//time for voting
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdownNow();
        gameServer.printVoteList();
        gameServer.sendToAll("spurious vote"); // sending for those who did not vote.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameServer.setNewHandlers();
    }
}
