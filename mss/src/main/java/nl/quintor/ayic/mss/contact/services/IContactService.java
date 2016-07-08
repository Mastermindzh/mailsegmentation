package nl.quintor.ayic.mss.contact.services;

import nl.quintor.ayic.mss.contact.domain.Contact;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

public interface IContactService {

    /**
     * will set a group of contacts active status to 0 in the database
     * @param str contact to be deleted
     * @return http Response
     * @throws SQLException
     */
    Response deleteContacts(String str) throws SQLException;

    /**
     * will delete group of contacts from the database
     * @param str contact to be deleted
     * @return http Response
     * @throws SQLException
     */
    Response permDeleteContacts(String str) throws SQLException;

    /**
     * will set a contacts active status to 0 in the database
     * @param str
     * @return http Response
     * @throws SQLException
     */
    Response deleteContact(String str) throws SQLException;

    /**
     * Will add a contact to the database
     * @param contact contact to be added
     * @return http Response
     * @throws SQLException
     */
    Response addContact(Contact contact) throws SQLException;

    /**
     * Will add a contact to a category
     * @param str contact to be added
     * @return http response
     * @throws SQLException
     */
    Response addContactToCategories(String str) throws SQLException;

    /**
     * Will edit a contact
     * @param str contact to be edited with new values and the old email
     * @return http response
     * @throws SQLException
     */
    Response updateContact(String str) throws SQLException;

    /**
     * Will return a list of contacts
     * @return List of contacts
     * @throws SQLException
     */
    List<Contact> getContacts() throws SQLException;

    /**
     * will return a single contact
     * @param str email to return
     * @return contact with all info
     * @throws SQLException
     */
    Contact getContact(String str) throws SQLException;

    /**
     * will update the coupled categories
     * @param str email and the categories to update
     * @return http response
     * @throws SQLException
     */
    Response updateCategoriesForContact(String str) throws SQLException;

    /**
     * Import a CSV file.
     * @param str csv file as plain text
     * @return A list of contacts
     * @throws SQLException
     */
    List<Contact> importCSV(String str) throws SQLException;

    /**
     * returns contacts in a specific range. include categories
     * @return List of contacts
     * @throws SQLException
     */
    List<Contact> getContactsInRange(String json) throws SQLException;

    List<Contact> searchContacts(String json) throws SQLException;
}
