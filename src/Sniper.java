import java.util.Scanner;

/**
 * The type Sniper.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Sniper extends Citizen {

    /**
     * Instantiates a new Sniper.
     */
    public Sniper() {
        super();
    }

    /**
     * move of the sniper in night.
     */
    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        temp.getGameServer().sendMsg("yek nafar ra entekhab konid : (mitavanid kasi ra entekhab nakonid " +
                "baraye in kar chizi vared konid)" +
                temp.getGameServer().getNames().toString(), temp);
        String chosenName = scanner.nextLine().strip();
        if (!temp.getGameServer().isNameInGame(chosenName)) {
            return;
        }
        Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
        if (chosenPerson instanceof Mafia) {
            chosenPerson.decreaseHealth();
        } else {// for incorrect shot in night
            decreaseHealth();
            decreaseHealth();
        }
        System.out.println(chosenName + "<------" + toString());
    }
    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "Sniper";
    }
}
