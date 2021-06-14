import java.util.Objects;

/**
 * class Vote.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class Vote {
    // name of voter
    private String voterName;

    /**
     * create a new Vote by voter name.
     *
     * @param voterName the voter name
     */
    public Vote(String voterName) {
        this.voterName = voterName;
    }

    /**
     * Gets voter name.
     *
     * @return the voter name
     */
    public String getVoterName() {
        return voterName;
    }

    /**
     * @return voter name.
     */

    @Override
    public String toString() {
        return voterName;
    }

    /**
     * for check equality of two object.
     * @param o second object
     * @return true or false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(voterName, vote.voterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voterName);
    }
}
