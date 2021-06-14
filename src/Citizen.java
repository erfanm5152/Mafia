/**
 * The type Citizen.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Citizen extends Person {

    /**
     * Instantiates a new Citizen.
     */
    public Citizen() {
        super(Side.CITIZEN);
    }

    @Override
    public String toString() {
        return "Citizen";
    }

    /**
     * for move of each role in night.
     */
    @Override
    public void run() {

    }
}
