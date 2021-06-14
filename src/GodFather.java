import java.util.Scanner;

/**
 * The type God father.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class GodFather extends Mafia {

    /**
     * Instantiates a new God father.
     */
    public GodFather() {
        super();
    }

    /**
     * move of the godFather in night
     */
    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        temp.getGameServer().sendMsg("yek nafar ra baraye koshtan entekhab konid :" +
                temp.getGameServer().getNames().toString(), temp);
        String chosenName = scanner.nextLine().strip();
        if (!temp.getGameServer().isNameInGame(chosenName)) {
            return;
        }
        Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
        if (temp.getGameServer().isNameInGame(chosenName)) {
            chosenPerson.decreaseHealth();
            if (chosenPerson instanceof DieHard) {
                ((DieHard) chosenPerson).setInjured(true);
            }
        }
        System.out.println(chosenName + "<------" + toString());
    }
    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "GodFather";
    }
}
