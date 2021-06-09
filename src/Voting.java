import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Voting {
    private GameServer gameServer;
    private ArrayList<Handler> newHandlers;

    public Voting(GameServer gameServer) {
        this.gameServer = gameServer;
        this.newHandlers =new ArrayList<>();
    }

    public synchronized void startVoting(){
        ExecutorService pool = Executors.newCachedThreadPool();
        gameServer.sendToAll("zamane ray giri fara resid. ");
        gameServer.sendToLives("players: "+gameServer.getNames().toString());
        for (Handler handler: gameServer.getClients()) {
            if (handler.getPerson().isAlive()) {
                pool.execute(new VoteThread(handler, gameServer));
            }
        }
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        pool.shutdownNow();
        gameServer.printVoteList();
        gameServer.sendToAll("spurious vote");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Handler temp: gameServer.getClients()) {
            temp.setExit(true);
            Handler newHandler = new Handler(temp);
            newHandlers.add(newHandler);
            if (temp.isConnected()){
            new Thread(newHandler).start();
            }
        }
        gameServer.setClients(newHandlers);
    }
}
