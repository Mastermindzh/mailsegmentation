package nl.quintor.ayic.mss.category.dao;

import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ICategoryDAO {

    /**
     * Will return a list of categories.
     * @return list of categories
     * @throws SQLException
     */
    List<Category> findAll() throws SQLException;

    /**
     * return a list of categories
     * @param email email to search for
     * @return list of categories
     * @throws SQLException
     */
    List<Category> findCategories(String email) throws SQLException;

    /**
     * Will return categories which match the search term
     * @param name searchterm
     * @return list of categories which match the search term
     * @throws SQLException
     */
    List<Category> findByName(String name) throws SQLException;

    /**
     * Add a category to the database
     * @param category category added
     * @throws SQLException
     */
    void addCategory(Category category) throws SQLException;

    /**
     * change a category name
     * @param oldName name to be changed
     * @param newName name to be changed to
     * @throws SQLException
     */
    void editCategory(String oldName, String newName) throws SQLException;

    /**
     * returns all related categories for a specific category
     * @param name
     * @return
     * @throws SQLException
     */
    List<Category> findRelated(String name) throws SQLException;

    /**
     * returns all related categories for the categories
     * @param categories
     * @return related categories
     * @throws SQLException
     */
    ArrayList<Category> findRelated(ArrayList<Category> categories) throws SQLException;

    /**
     * add a related category to a category.
     * @param category
     * @param subCategory
     * @throws SQLException
     */
    void addRelatedCategory(String category, String subCategory) throws SQLException;

    void deleteCategory(String category) throws SQLException;

    /**
     * get contacts belonging to a category
     * @param category category info
     * @return list of contacts matching category
     * @throws SQLException
     */
    List<Contact> getContactsInCategory(String category) throws SQLException;
    /**
     * returns categories which match the domain email
     * @param domain
     * @return
     * @throws SQLException
     */
    List<Category> findCategoriesWithDomain(String domain) throws SQLException;

    /**
     * get category with number of contacts+domains
     * @return list of categories
     * @throws SQLException
     */
    List<Category> getCategoriesWithCount() throws  SQLException;
}
