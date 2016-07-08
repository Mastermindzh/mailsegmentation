package nl.quintor.ayic.mss.category.domain;

public class Category {
    private String name;
    private int countContacts;

    public Category() {
        //needed to create a json object
    }

    public int getCountContacts() {
        return countContacts;
    }

    public void setCountContacts(int countContacts) {
        this.countContacts = countContacts;
    }

    public Category(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", countContacts=" + countContacts +
                '}';
    }
}
