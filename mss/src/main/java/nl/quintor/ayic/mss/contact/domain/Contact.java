package nl.quintor.ayic.mss.contact.domain;

import nl.quintor.ayic.mss.category.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateAdded;
    private boolean active = true;
    private String importName;
    private List<Category> categories = new ArrayList<>();

    public Contact() {
        //needed to create a json object
    }

    public Contact(String email, String dateAdded, boolean active) {
        this.email = email;
        this.dateAdded = dateAdded;
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    @Override
    public String toString() {
        return "Contact{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", active=" + active +
                ", importName='" + importName + '\'' +
                '}';
    }

}
