import java.util.Objects;

public class Vote {

    private String voterName;

    public Vote(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterName() {
        return voterName;
    }

    @Override
    public String toString() {
        return voterName;
    }

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
