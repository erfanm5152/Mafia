import java.util.Scanner;

/**
 * The type Mafia.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Mafia extends Person {

    /**
     * Instantiates a new Mafia.
     */
    public Mafia() {
        super(Side.MAFIA);
    }

    @Override
    public String toString() {
        return "Mafia";
    }

    /**
     * for move of the mafia in night.
     */
    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        temp.getGameServer().sendMsg("yek nafar ra entekhab konid :(tanha yek payam baraye Mafia) " +
                temp.getGameServer().getNames().toString(), temp);
        String msg = scanner.nextLine().strip();
        temp.getGameServer().sendMsgToMafia(temp.getName() + " : " + msg, temp);
        System.out.println(msg + "<------" + toString());
    }
}
