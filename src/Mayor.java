import java.util.Scanner;

public class Mayor extends Citizen {


    public Mayor() {
        super();
    }

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

    @Override
    public String toString() {
        return "Mayor";
    }
}
