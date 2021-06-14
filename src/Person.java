import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * The type Person.
 * As the role of each player.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public abstract class Person implements Runnable {
    // for check mute of role.
    private boolean isMuted;
    // for check that the role is alive.
    private boolean isAlive;
    // for check the ability of psychologist
    private boolean isPsychologicalSilence;
    // health of each role (use in night)
    private int health;
    // side of the role
    private Side side;
    // list of voters
    private ArrayList<Vote> votes;
    // handler of this role
    private Handler handler;

    /**
     * Instantiates a new Person.
     *
     * @param side the side of the role
     */
    public Person(Side side) {
        this.side = side;
        this.health = 1;
        this.votes = new ArrayList<>();
        this.isMuted = false;
        this.isAlive = true;
        this.isPsychologicalSilence = false;
        this.handler = null;
    }

    /**
     * Sets health.
     *
     * @param health the health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Increase health.
     */
    public synchronized void increaseHealth() {
        this.health++;
    }

    /**
     * Decrease health.
     */
    public synchronized void decreaseHealth() {
        this.health--;
    }


    /**
     * Is psychological silence boolean.
     *
     * @return the boolean
     */
    public boolean isPsychologicalSilence() {
        return isPsychologicalSilence;
    }

    /**
     * Sets psychological silence.
     *
     * @param psychologicalSilence the psychological silence
     */
    public void setPsychologicalSilence(boolean psychologicalSilence) {
        isPsychologicalSilence = psychologicalSilence;
    }

    /**
     * Gets health.
     *
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets side.
     *
     * @return the side
     */
    public Side getSide() {
        return side;
    }

    /**
     * Add vote.
     *
     * @param voterName the voter name
     */
    public synchronized void addVote(String voterName) {
        votes.add(new Vote(voterName));
    }

    /**
     * Remove vote.
     *
     * @param voterName the voter name
     */
    public void removeVote(String voterName) {
        Iterator<Vote> iterator = votes.iterator();
        while (iterator.hasNext()) {
            Vote vote = iterator.next();
            if (vote.getVoterName().equals(voterName)) {
                iterator.remove();
            }
        }
    }

    /**
     * Gets votes.
     *
     * @return the votes
     */
    public ArrayList<Vote> getVotes() {
        return votes;
    }

    /**
     * Size of votes int.
     *
     * @return the int
     */
    public int sizeOfVotes() {
        return votes.size();
    }

    /**
     * Refresh votes.
     */
    public void refreshVotes() {
        Iterator<Vote> iterator = votes.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * Gets handler.
     *
     * @return the handler
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * check equality of two person
     * @param o second person
     * @return true or false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return health == person.health && side == person.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(health, side);
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Sets alive.
     *
     * @param alive the alive
     */
    public synchronized void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * Number of votes int.
     *
     * @return the int
     */
    public int numberOfVotes() {
        return votes.size();
    }

    /**
     * Is muted boolean.
     *
     * @return the boolean
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Is alive boolean.
     *
     * @return the boolean
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Sets muted.
     *
     * @param muted the muted
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}
