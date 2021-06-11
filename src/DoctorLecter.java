import java.util.Scanner;

public class DoctorLecter extends Mafia{

    private boolean isSelf;

    public DoctorLecter( ) {
        super();
        this.isSelf =false;
    }

    @Override
    public void run() {
        super.run();
        Handler temp = getHandler();
        Scanner scanner = temp.getScanner();
        boolean finish= false;
        do {
            temp.getGameServer().sendMsg("yek nafar ra entekhab konid : " +
                    temp.getGameServer().getNames().toString(), temp);
            String chosenName = scanner.nextLine().strip();
            if (!temp.getGameServer().isNameInGame(chosenName)){
                return;
            }
            Person chosenPerson = temp.getGameServer().getHandlerByName(chosenName).getPerson();
            if (chosenPerson.equals(temp.getPerson())) {
                if (!isSelf) {
                    chosenPerson.increaseHealth();
                    isSelf = true;
                    finish =true;
                }
            }else {
                if (chosenPerson.getSide()==Side.MAFIA) {
                    chosenPerson.increaseHealth();
                }
                finish = true;
            }
            System.out.println(chosenName+"<------"+toString());
        }while (!finish);
    }

    @Override
    public String toString() {
        return "DoctorLecter";
    }
}
