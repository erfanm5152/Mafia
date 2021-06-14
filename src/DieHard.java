import java.util.Scanner;

/**
 * The type Die hard.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class DieHard extends Citizen {
    // for check injured
    private boolean isInjured;
    // number of inquiries
    private int limitations;

    /**
     * Instantiates a new Die hard.
     */
    public DieHard() {
        super();
        increaseHealth();
        isInjured = false;
        this.limitations = 2;
    }

    /**
     * move of die hard in night
     */
    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        String choice = "";
        if (limitations > 0) {
            temp.getGameServer().sendMsg("aya estelam mikhahid ? (Y/N)", temp);
            choice = scanner.nextLine().strip();
            if (choice.equalsIgnoreCase("y")) {
                temp.getGameServer().setInquiry(true);
                limitations--;
            } else {
                temp.getGameServer().setInquiry(false);
            }
        } else {
            temp.getGameServer().sendMsg("tedad estlam ha tamam shod.", temp);
        }
        System.out.println(choice + "<------" + toString());
    }

    /**
     * @return name of the role
     */
    @Override
    public String toString() {
        return "DieHard";
    }

    /**
     * Is injured boolean.
     *
     * @return the boolean
     */
    public boolean isInjured() {
        return isInjured;
    }

    /**
     * Sets injured.
     *
     * @param injured the injured
     */
    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    /**
     * Gets limitations.
     *
     * @return the limitations
     */
    public int getLimitations() {
        return limitations;
    }

    /**
     * Sets limitations.
     *
     * @param limitations the limitations
     */
    public void setLimitations(int limitations) {
        this.limitations = limitations;
    }
}
