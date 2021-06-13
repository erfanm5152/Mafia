import java.util.ArrayList;


public class Night {
    private GameServer gameServer;
    private ArrayList<Handler> newHandlers;

    public Night(GameServer gameServer) {
        this.gameServer = gameServer;
        this.newHandlers = new ArrayList<>();
    }

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
        for (Handler temp : gameServer.getClients()) {
            temp.setExit(true);
            Handler newHandler = new Handler(temp);
            newHandlers.add(newHandler);
            if (temp.isConnected()) {
                new Thread(newHandler).start();
            }
        }
        gameServer.setClients(newHandlers);
    }

    public void nightDie() {
        for (Handler handler : gameServer.getClients()) {
            if (handler.getPerson().getHealth() == 0) {
                handler.getPerson().setAlive(false);
            }
        }
    }

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
