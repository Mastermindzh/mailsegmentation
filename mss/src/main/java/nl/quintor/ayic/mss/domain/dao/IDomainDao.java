package nl.quintor.ayic.mss.domain.dao;

import nl.quintor.ayic.mss.domain.domain.Domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IDomainDao {

    /**
     * will add a domain to the database
     * @param domain to be added
     * @throws SQLException
     */
    void addDomain(Domain domain) throws SQLException;

    /**
     * will set a contact as inactive in the database
     * @param domain address to be inactivated
     * @throws SQLException
     */
    void deleteDomain(String domain) throws SQLException;


    /**
     * Return all contacts
     * @return list of contacts
     * @throws SQLException
     */
    List<Domain> getDomains() throws SQLException;

    /**
     * return a domain by e-mail
     * @param identifier domain identifier
     * @return domain
     * @throws SQLException
     */
    Domain getDomain(String identifier) throws SQLException;

    /**
     * change the domain identifier
     * @param identifier domain to change
     * @param newIdentifier new identifier to be given to the domain
     * @throws SQLException
     */
    void updateDomain(String identifier, String newIdentifier) throws SQLException;

    /**
     * Add a category to a domain
     * @param category category to be added
     * @param domain domain to which the category should be added.
     * @throws SQLException
     */
    void addCategoryToDomain (String category, String domain) throws SQLException;

    /**
     * add multiple categories to a domain at once
     * @param identifier domain to which categories should be added
     * @param categories categories to be added (arraylist<Category>)
     * @throws SQLException
     */
    void addCategoriesToDomain(String identifier, ArrayList<String> categories) throws SQLException;

    /**
     * delete all categories for a specific domain
     * @param identifier domain for which all the domains have to be deleted
     * @throws SQLException
     */
    void deleteAllCategoriesForDomain(String identifier) throws SQLException;

    /**
     * return domains with categorycount
     * @return list of domains
     * @throws SQLException
     */
    List<Domain> getDomainsWithCount() throws SQLException;
}
