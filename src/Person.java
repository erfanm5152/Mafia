import java.util.ArrayList;

public class Person {

    private boolean isMuted;
    private boolean isAlive;
    private int health;
    private Side side;
    private ArrayList<Vote> votes;

    public Person(Side side) {
        this.side = side;
        this.health = 1;
        this.votes = new ArrayList<>();
        this.isMuted = false;
        this.isAlive = true;
    }

    public void increaseHealth(){
        this.health++;
    }

    public void decreaseHealth(){
        this.health--;
    }


    public int getHealth() {
        return health;
    }

    public Side getSide() {
        return side;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }
}
