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
        gameServer.sendToAll("players: "+gameServer.getNames().toString()+"\t(agar ray nemidahid baraye baz shodan" +
                "ghofl safhe enter ra feshar dahid)");
        for (Handler handler: gameServer.getClients()) {
           pool.execute(new VoteThread(handler,gameServer));
        }
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        pool.shutdownNow();
        gameServer.printVoteList();
        gameServer.sendToAll("spurious vote");
        for (Handler temp: gameServer.getClients()) {
            Handler newHandler = new Handler(temp);
            newHandlers.add(newHandler);
            new Thread(newHandler).start();
        }
        gameServer.setClients(newHandlers);
    }
}
