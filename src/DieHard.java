import java.util.Scanner;

public class DieHard extends Citizen {
    private boolean isInjured;
    private int limitations;

    public DieHard() {
        super();
        increaseHealth();
        isInjured = false;
        this.limitations = 2;
    }

    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        String choice = "";
        if (limitations > 0) {
            temp.getGameServer().sendMsg("aya estelam mikhahid ? (Y/N)", temp);
            choice = scanner.nextLine().strip();
            if (choice.equalsIgnoreCase("y")) {
                temp.getGameServer().setInquiry(true);// dar halghe bazi dar yek ja bayad false shavad
                limitations--;
            } else {
                temp.getGameServer().setInquiry(false);
            }
        } else {
            temp.getGameServer().sendMsg("tedad estlam ha tamam shod.", temp);
        }
        System.out.println(choice + "<------" + toString());
    }

    @Override
    public String toString() {
        return "DieHard";
    }

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    public int getLimitations() {
        return limitations;
    }

    public void setLimitations(int limitations) {
        this.limitations = limitations;
    }
}
