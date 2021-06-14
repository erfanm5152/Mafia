import java.util.Scanner;

/**
 * The type Doctor lecter.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class DoctorLecter extends Mafia {
    // for show move of the doctor on self.
    private boolean isSelf;

    /**
     * Instantiates a new Doctor lecter.
     */
    public DoctorLecter() {
        super();
        this.isSelf = false;
    }

    /**
     * move of doctor lecter in night.
     */
    @Override
    public void run() {
        super.run();// move of a mafia in night
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        boolean finish = false;
        do {
            temp.getGameServer().sendMsg("yek nafar ra entekhab konid : " +
                    temp.getGameServer().getNames().toString(), temp);
            String chosenName = scanner.nextLine().strip();
            if (!temp.getGameServer().isNameInGame(chosenName)) {
                return;
            }
            Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
            if (chosenPerson.equals(temp.getPerson())) {
                if (!isSelf) {
                    chosenPerson.increaseHealth();
                    isSelf = true;
                    finish = true;
                }
            } else {
                if (chosenPerson.getSide() == Side.MAFIA) {
                    chosenPerson.increaseHealth();
                }
                finish = true;
            }
            System.out.println(chosenName + "<------" + toString());
        } while (!finish);
    }
    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "DoctorLecter";
    }
}
