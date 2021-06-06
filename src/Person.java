import jdk.jshell.PersistentSnippet;

import java.util.ArrayList;
import java.util.Iterator;

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
    public void addVote(String voterName){
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

    public void setAlive(boolean alive) {
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
