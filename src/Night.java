import java.util.ArrayList;


/**
 * The type Night.
 * for do moves in night.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Night {
    // server of the game
    private GameServer gameServer;
    // new handlers that to be created
    private ArrayList<Handler> newHandlers;

    /**
     * Instantiates a new Night.
     *
     * @param gameServer the game server
     */
    public Night(GameServer gameServer) {
        this.gameServer = gameServer;
        this.newHandlers = new ArrayList<>();
    }

    /**
     * Start do moves.
     */
    public void start() {
        refreshHealth();
        gameServer.muteEvery();
        gameServer.sendToAll("shab mishavad\n" +
                "va hame be khab miravand.");
        if (gameServer.isIntroduction()) {
            gameServer.introduction();
            gameServer.setIntroduction(false);
        }
        for (Handler handler : gameServer.getClients()) {
            if (handler.getPerson().isAlive()) {
                gameServer.sendMsg("***PlayRole***", handler);
                if (!(handler.getPerson() instanceof Mayor)) {
                    new Thread(handler.getPerson()).start();
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gameServer.sendMsg("spurious role", handler);
                    gameServer.sendMsg("spurious role", handler);
                    gameServer.sendMsg("spurious role", handler);
                }
            }
        }
        nightDie();
        for (Handler temp : gameServer.getClients()) {//todo neveshtan method baraye in dar server.
            temp.setExit(true);
            Handler newHandler = new Handler(temp);
            newHandlers.add(newHandler);
            if (temp.isConnected()) {
                new Thread(newHandler).start();
            }
        }
        gameServer.setClients(newHandlers);
    }

    /**
     * Night die.
     * Killing people who have lost their lives.
     */
    public void nightDie() {
        for (Handler handler : gameServer.getClients()) {
            if (handler.getPerson().getHealth() <= 0) {
                handler.getPerson().setAlive(false);
                gameServer.sendMsg("shoma dar sgab koshte shodid.",handler);
            }
        }
    }

    /**
     * Refresh health after shot of mafia and save of the doctor.
     */
    public void refreshHealth() {
        for (Handler handler : gameServer.getClients()) {
            if (handler.getPerson().isAlive()) {
                if (handler.getPerson().getHealth() >= 2) {
                    if (handler.getPerson() instanceof DieHard) {
                        if (((DieHard) handler.getPerson()).isInjured()) {
                            handler.getPerson().setHealth(1);
                        } else {
                            handler.getPerson().setHealth(2);
                        }
                    } else {
                        handler.getPerson().setHealth(1);
                    }
                }
            }
        }
    }
}
