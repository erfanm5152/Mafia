import java.util.Scanner;

public class Sniper extends Citizen{

    public Sniper() {
        super();
    }

    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        temp.getGameServer().sendMsg("yek nafar ra entekhab konid : (mitavanid kasi ra entekhab nakonid " +
                "baraye in kar chizi vared konid)"+
                temp.getGameServer().getNames().toString(),temp);
        String chosenName = scanner.nextLine().strip();
        Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
        if (chosenPerson instanceof Mafia){
            chosenPerson.setAlive(false);
        }else if (temp.getGameServer().isNameInGame(chosenName)){
            setAlive(false);
        }
    }

    @Override
    public String toString() {
        return "Sniper";
    }
}
