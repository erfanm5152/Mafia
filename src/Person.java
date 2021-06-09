import java.util.ArrayList;
import java.util.Iterator;

public abstract class Person implements Runnable{

    private boolean isMuted;
    private boolean isAlive;
    private int health;
    private Side side;
    private ArrayList<Vote> votes;
    private Handler handler;

    public Person(Side side) {
        this.side = side;
        this.health = 1;
        this.votes = new ArrayList<>();
        this.isMuted = false;
        this.isAlive = true;
        this.handler = null;
    }

    public synchronized void increaseHealth(){
        this.health++;
    }

    public synchronized void decreaseHealth(){
        this.health--;
    }


    public int getHealth() {
        return health;
    }

    public Side getSide() {
        return side;
    }
    public synchronized void addVote(String voterName){
        votes.add(new Vote(voterName));
    }

    public void removeVote(String voterName){
        Iterator<Vote> iterator = votes.iterator();
        while (iterator.hasNext()){
            Vote vote = iterator.next();
            if (vote.getVoterName().equals(voterName)){
                iterator.remove();
            }
        }
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public int sizeOfVotes(){
        return votes.size();
    }
    public void refreshVotes(){
        Iterator<Vote> iterator = votes.iterator();
        while (iterator.hasNext()){
            iterator.next();
            iterator.remove();
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public synchronized void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int numberOfVotes(){
        return votes.size();
    }

    public boolean isMuted() {
        return isMuted;
    }

    public boolean isAlive() {
        return isAlive;
    }


}
