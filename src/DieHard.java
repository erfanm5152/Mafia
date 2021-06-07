public class DieHard extends Citizen implements Capable{
    public DieHard() {
        super();
        increaseHealth();
    }

    @Override
    public void move() {

    }

    @Override
    public String toString() {
        return "DieHard";
    }
}
