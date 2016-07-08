package nl.quintor.ayic.mss.category.services;



import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

public interface ICategoryService {

    /**
     * Add a category to the Database
     * @param category category to be added
     * @return Response
     * @throws SQLException
     */
    Response addCategory(Category category) throws SQLException;

    /**
     * returns all categories
     * @return list of categories
     * @throws SQLException
     */
    List<Category> findAll() throws SQLException;

    /**
     * returns all categories with the occurence count
     * @return list of categories
     * @throws SQLException
     */
    List<Category> findAllWithCount() throws SQLException;

    /**
     * Will return categories which match the search term
     * @param name searchterm
     * @return list of categories which match the search term
     * @throws SQLException
     */
    List<Category> findByName(String name) throws SQLException;

    /**
     * Edit the name of a category
     * @param jsonData json formatted changes
     * @return Response
     * @throws SQLException
     */
    Response editCategory(String jsonData) throws SQLException;

    /**
     * find categories matching search string
     * @param category string to match
     * @return list of categories
     * @throws SQLException
     */
    List<Category> findCategories(String category) throws SQLException;

    /**
     * returns related categories
     * @param jsonData category to match against
     * @return list of categories
     * @throws SQLException
     */
    List<Category> findRelated(String jsonData) throws SQLException;

    /**
     * add a category to another category. creating the link "Category has Category"
     * @param jsonData json object with a category and sub category
     * @return HTTP response
     * @throws SQLException
     */
    Response addRelatedCategory(String jsonData) throws  SQLException;

    /**
     * add multiple categories to another category. creating the link "Category has Category"
     * @param jsonData json object with a category and multiple sub categories.
     * @return HTTP response
     * @throws SQLException
     */
    Response addRelatedCategories(String jsonData) throws  SQLException;

    /**
     * delete a category
     * @param jsonData category to remove
     * @return Response
     * @throws SQLException
     */
    Response deleteCategory(String jsonData) throws  SQLException;


    /**
     * get contacts belonging to a category
     * @param jsonData json data with category info
     * @return list of contacts matching category
     * @throws SQLException
     */
    List<Contact> getContactsInCategory(String jsonData) throws SQLException;

    /**
     * returns all subcategories from a maincategory
     * @param jsonData
     * @return
     * @throws SQLException
     */
    List<Category> getRelatedCategories(String jsonData) throws SQLException;

    /**
     * add multiple categories to another category. creating the link "Category has Category"
     * @param jsonData json object with a category and multiple sub categories.
     * @return HTTP response
     * @throws SQLException
     */
    List<Category> getCategoriesWithDomain(String jsonData) throws  SQLException;
}
