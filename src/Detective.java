import java.util.Scanner;

public class Detective extends Citizen{
    public Detective() {
        super();
    }

    @Override
    public void run() {
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        temp.getGameServer().sendMsg("yek nafar ra entekhab konid : "+
                temp.getGameServer().getNames().toString(),temp);
        String chosenName = scanner.nextLine().strip();
        if (!temp.getGameServer().isNameInGame(chosenName)){
            return;
        }
        Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
        if (chosenPerson instanceof Mafia){
            if (chosenPerson instanceof GodFather){
                temp.getGameServer().sendMsg("adam taid.",temp);
            }else {
                temp.getGameServer().sendMsg("taid.",temp);
            }
        }else {
            temp.getGameServer().sendMsg("adam taid.",temp);
        }
        System.out.println(chosenName+"<------"+toString());
    }

    @Override
    public String toString() {
        return "Detective";
    }
}
