/**
 * The type God father successor.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class GodFatherSuccessor extends GodFather {
    // role of successor of GodFather
    private Mafia successor;

    /**
     * Instantiates a new God father successor.
     *
     * @param mafia role of the successor
     */
    public GodFatherSuccessor(Mafia mafia) {
        super();
        this.successor = mafia;
        setHandler(mafia.getHandler());
    }

    /**
     * move of the GodFatherSuccessor in night.
     */
    @Override
    public void run() {
        // move of successor
        successor.run();
        // move of godFather
        super.run();
    }
    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return successor.toString();
    }
}
