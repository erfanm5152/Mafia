import java.util.Scanner;

/**
 * The type Mayor.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Mayor extends Citizen {


    /**
     * Instantiates a new Mayor.
     */
    public Mayor() {
        super();
    }

    /**
     * move of the mayor in voting.
     */
    @Override
    public void run() {
        Handler mayor = getHandler();
        Scanner scanner = getHandler().getScanner();
        mayor.getGameServer().sendMsg("aya ray giri molgha shavad ? (Y/N)", mayor);
        String choice = scanner.nextLine().strip();
        System.out.println(choice + "<-------Mayor");
        if (choice.equalsIgnoreCase("Y")) {
            mayor.getGameServer().setValidVoting(false);
        } else {
            mayor.getGameServer().setValidVoting(true);
        }
    }

    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "Mayor";
    }
}
