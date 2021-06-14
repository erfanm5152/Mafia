import java.util.Scanner;

/**
 * The type Detective.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Detective extends Citizen {
    /**
     * Instantiates a new Detective.
     */
    public Detective() {
        super();
    }

    /**
     * move of detective in night
     */
    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        temp.getGameServer().sendMsg("yek nafar ra entekhab konid : " +
                temp.getGameServer().getNames().toString(), temp);
        String chosenName = scanner.nextLine().strip();
        if (!temp.getGameServer().isNameInGame(chosenName)) {
            return;
        }
        Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
        if (chosenPerson instanceof Mafia) {
            if (chosenPerson instanceof GodFather && !(chosenPerson instanceof GodFatherSuccessor)) {
                temp.getGameServer().sendMsg("adam taid.", temp);
            } else {
                temp.getGameServer().sendMsg("taid.", temp);
            }
        } else {
            temp.getGameServer().sendMsg("adam taid.", temp);
        }
        System.out.println(chosenName + "<------" + toString());
    }

    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "Detective";
    }
}
