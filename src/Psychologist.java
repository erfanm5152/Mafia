import java.util.Scanner;

/**
 * The type Psychologist.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Psychologist extends Citizen {
    /**
     * Instantiates a new Psychologist.
     */
    public Psychologist() {
        super();
    }

    /**
     * move of the psychologist in night.
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
        chosenPerson.setPsychologicalSilence(true);
        System.out.println(chosenName + "<------" + toString());
    }

    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "Psychologist";
    }
}
