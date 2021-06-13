import java.util.Scanner;

public class Mafia extends Person {

    public Mafia() {
        super(Side.MAFIA);
    }

    @Override
    public String toString() {
        return "Mafia";
    }

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
