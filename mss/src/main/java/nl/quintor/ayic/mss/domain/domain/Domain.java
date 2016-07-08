package nl.quintor.ayic.mss.domain.domain;

public class Domain {

    private String identifier;
    private boolean active;

    public int getCountContacts() {
        return countContacts;
    }

    public void setCountContacts(int countContacts) {
        this.countContacts = countContacts;
    }

    private int countContacts;

    /**
     * Constructor
     * @param identifier address to recognize this specific domain
     * @param active represents whether the domain is active or not
     */
    public Domain(String identifier, boolean active) {
        this.identifier = identifier;
        this.active = active;
    }

    /**
     * Empty constructor for json
     */
    public Domain() {
        // Do nothing
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "identifier='" + identifier + '\'' +
                ", active=" + active +
                '}';
    }
}
