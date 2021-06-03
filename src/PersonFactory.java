import java.util.ArrayList;

public class PersonFactory {
    public static ArrayList<Person> createPersons(int number) {
        ArrayList<Person> temp = new ArrayList<>();
        switch (number) {
                case 5:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Citizen());
                    temp.add(new Mafia());
                    break;
                case 6:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Citizen());
                    temp.add(new Mafia());
                    temp.add(new Citizen());
                    break;
                case 7:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Citizen());
                    temp.add(new Citizen());
                    temp.add(new Mafia());
                    temp.add(new Sniper());
                    break;

                case 8:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Citizen());
                    temp.add(new Citizen());
                    temp.add(new Mafia());
                    temp.add(new Sniper());
                    temp.add(new DieHard());
                    break;

                case 9:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Citizen());
                    temp.add(new Citizen());
                    temp.add(new Mafia());
                    temp.add(new Sniper());
                    temp.add(new DoctorLecter());
                    temp.add(new Psychologist());
                    temp.add(new DieHard());
                    break;

                case 10:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Citizen());
                    temp.add(new Citizen());
                    temp.add(new Mafia());
                    temp.add(new Sniper());
                    temp.add(new DoctorLecter());
                    temp.add(new Psychologist());
                    temp.add(new DieHard());
                    temp.add(new Mayor());
                    break;

                default:
                    temp.add(new Detective());
                    temp.add(new Doctor());
                    temp.add(new GodFather());
                    temp.add(new Sniper());
                    temp.add(new DoctorLecter());
                    temp.add(new Psychologist());
                    temp.add(new DieHard());
                    for (int i = 0; i <(number/3)-2 ; i++) {
                        temp.add(new Mafia());
                    }
                    for (int i = 0; i <number - temp.size() ; i++) {
                        temp.add(new Citizen());
                    }
                    break;
            }
    return temp;
    }
}