package nl.quintor.ayic.mss.domain.services;


import nl.quintor.ayic.mss.domain.domain.Domain;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

public interface IDomainService {

    /**
     * Will delete a domain
     * @param str domain te be deleted
     * @return HTTP response
     * @throws SQLException
     */
    Response deleteDomain(String str) throws SQLException;

    /**
     * Will delete domains
     * @param str domain te be deleted
     * @return HTTP response
     * @throws SQLException
     */
    Response deleteDomains(String str) throws SQLException;

    /**
     * Will add a domain
     * @param domain domain to add
     * @return HTTP response
     * @throws SQLException
     */
    Response addDomain(Domain domain) throws SQLException;

    /**
     * return all domains
     * @return return all domains
     * @throws SQLException
     */
    List<Domain> getDomains() throws SQLException;

    /**
     * return all info of a domain
     * @param str domain to return
     * @return domain
     * @throws SQLException
     */
    Domain getDomain(String str) throws SQLException;

    /**
     * change the identifier of a domain
     * @param str json with information which domain has to be edited
     * @return HTTP response
     * @throws SQLException
     */
    Response updateDomain(String str) throws SQLException;

    /**
     * add a category to a domain
     * @param json json with information about which category to add to which domain
     * @return HTTP response
     * @throws SQLException
     */
    Response addCategoryToDomain(String json) throws SQLException;

    /**
     * add multiple categories to a domain
     * @param json json with information about which category to add to which domain
     * @return HTTP response
     * @throws SQLException
     */
    Response addCategoriesToDomain(String json) throws SQLException;

    /**
     * update categories for a domain
     * @param str json formatted with a domain and new categories
     * @return Response
     * @throws SQLException
     */
    Response updateCategoriesForDomain(String str) throws SQLException;

    /**
     * returns all domains and counts contacts in them
     * @return
     * @throws SQLException
     */
    List<Domain> getDomainsWithCount() throws SQLException;

}
