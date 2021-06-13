public class GodFatherSuccessor extends GodFather{

    private Mafia successor;

    public GodFatherSuccessor(Mafia mafia) {
        super();
        this.successor = mafia;
        setHandler(mafia.getHandler());
    }

    @Override
    public void run() {
        successor.run();
        super.run();
    }

    @Override
    public String toString() {
        return successor.toString();
    }
}
