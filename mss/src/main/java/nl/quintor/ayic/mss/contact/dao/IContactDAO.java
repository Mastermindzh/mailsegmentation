package nl.quintor.ayic.mss.contact.dao;

import nl.quintor.ayic.mss.contact.domain.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IContactDAO {

    /**
     * will add a contact to the database
     * @param contact contact to be added
     * @throws SQLException
     */
    void addContact(Contact contact) throws SQLException;

    /**
     * will set a contact as inactive in the database
     * @param mail address to be inactivated
     * @throws SQLException
     */
    void deleteContact(String mail) throws SQLException;

    /**
     * will actually delete a user from the database
     * @param mail address to be inactivated
     * @throws SQLException
     */
    void permDeleteContact(String mail) throws SQLException;

    /**
     * Will delete all linked categories for contact in the database
     *
     * @param email contact to be decoupled from categories
     * @throws SQLException
     */
    void deleteAllCategoriesForContact(String email) throws SQLException;

    /**
     * Will update a contact in the database
     *
     * @param contact contact to be updated
     * @throws SQLException
     */
    void updateContact(Contact contact, String oldEmail) throws SQLException;

    /**
     * Will add a contact to a category
     * @param mail contact to be added
     * @param categories category to which the contact will be added
     * @throws SQLException
     */
    void addContactToCategories(String mail, ArrayList<String> categories) throws SQLException;

    /**
     * Return all contacts
     * @return list of contacts
     * @throws SQLException
     */
    List<Contact> getContacts() throws SQLException;

    /**
     * return a contact by mail address
     * @param mail
     * @return
     * @throws SQLException
     */
    Contact getContact(String mail) throws SQLException;

    /**
     * get contacts in a range (pagination)
     * @param min lower limit of the range
     * @param max upper limit of the range
     * @return list of contacts
     * @throws SQLException
     */
    List<Contact> getContactsInRange(int min, int max, boolean active) throws  SQLException;



    List<Contact> searchContacts(String search, boolean active) throws  SQLException;
}
