package nl.quintor.ayic.mss.export.dao;

import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.database.dao.Dao;
import nl.quintor.ayic.mss.domain.domain.Domain;
import nl.quintor.ayic.mss.export.domain.CategoryBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExportDao extends Dao implements IExportDao {

    public ExportDao() throws SQLException, IOException {
        super();
    }

    public List<Contact> getExportContacts(ArrayList<CategoryBox> categoryBoxes, ArrayList<Domain> domains) throws SQLException {
        List<Contact> contacts = new ArrayList<>();

        try {
            Connection conn = getConnection();
            if (!categoryBoxes.isEmpty()) {
                contacts.addAll(getContactsFromCategoryBoxes(conn, categoryBoxes));
                domains.addAll(getDomainsFromCategoryBoxes(conn, categoryBoxes));
            }

            if (!domains.isEmpty()) {
                getContactsFromDomains(conn, domains).stream().filter(contact -> !contacts.contains(contact)).forEach(contacts::add);
            }

            conn.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }

        return contacts;
    }

    /**
     * @param conn          an open database connection.
     * @param categoryBoxes boxes of categories to get domains from. Every box can contain categories. The domains must be linked to at least one category in every box to be get.
     * @return a list of domains
     * @throws SQLException
     */
    private List<Domain> getDomainsFromCategoryBoxes(Connection conn, ArrayList<CategoryBox> categoryBoxes) throws SQLException {
        PreparedStatement stmt = insertCategoryBoxesInPreparedStatement(
                conn.prepareStatement("SELECT DISTINCT domain.identifier, (SELECT  GROUP_CONCAT(domain_category.CategoryName) FROM Domain_has_Category domain_category WHERE domain_category.DomainIdentifier = domain.identifier) as categories FROM Domain domain WHERE domain.active = true " + getHavingStatementForCategoryBoxQuery(categoryBoxes)),
                categoryBoxes
        );
        ArrayList<Domain> domains = getDomainsFromResultSet(stmt.executeQuery());
        stmt.close();
        return domains;
    }

    /**
     * @param conn          an open database connection.
     * @param categoryBoxes boxes of categories to get contacts from. Every box can contain categories. The contacts must be linked to at least one category in every box to be get.
     * @return
     * @throws SQLException
     */
    private List<Contact> getContactsFromCategoryBoxes(Connection conn, ArrayList<CategoryBox> categoryBoxes) throws SQLException {
        PreparedStatement stmt = insertCategoryBoxesInPreparedStatement(
                conn.prepareStatement("SELECT DISTINCT *, (SELECT  GROUP_CONCAT(contact_category.Category_name) FROM Contact_has_Category contact_category WHERE contact_category.Contact_email = contact.email) as categories FROM Contact contact WHERE contact.active = true " + getHavingStatementForCategoryBoxQuery(categoryBoxes)),
                categoryBoxes
        );
        List<Contact> contacts = getContactsFromResultSet(stmt.executeQuery());
        stmt.close();
        return contacts;
    }

    /**
     * gets the Having statement for a query getting domains or contacts for categoryBoxes
     *
     * @param categoryBoxes boxes of categories to get contacts or domains from. Every box can contain categories. The contacts must be linked to at least one category in every box to be get.
     * @return having statement for a query
     */
    protected String getHavingStatementForCategoryBoxQuery(ArrayList<CategoryBox> categoryBoxes) {
        StringBuilder builderCategories = new StringBuilder("HAVING ");

        for (int i = 0; i < categoryBoxes.size(); i++) {
            if (!categoryBoxes.get(i).getCategories().isEmpty()) {
                if (i != 0) {
                    builderCategories.append(" AND ");
                }
                builderCategories.append(" (");
                appendCategories(categoryBoxes, builderCategories, i);
                builderCategories.append(")");
            }
        }

        return builderCategories.toString();
    }

    private void appendCategories(ArrayList<CategoryBox> categoryBoxes, StringBuilder builderCategories, int i) {
        for (int i2 = 0; i2 < categoryBoxes.get(i).getCategories().size(); i2++) {
            if (i2 != 0) {
                builderCategories.append(" OR ");
            }
            builderCategories.append("LOCATE(?,categories) > 0");
        }
    }

    /**
     * Inserts the categories form the categoryBoxes in the Having statement in a PreparedStatement.
     *
     * @param stmt          the statement to be inserted with categories
     * @param categoryBoxes boxes of categories to get contacts or domains from. Every box can contain categories. The contacts must be linked to at least one category in every box to be get.
     * @return the PreparedStatement with inserted categories.
     * @throws SQLException
     */
    private PreparedStatement insertCategoryBoxesInPreparedStatement(PreparedStatement stmt, ArrayList<CategoryBox> categoryBoxes) throws SQLException {
        int index = 0;

        for (CategoryBox categoryBox : categoryBoxes) {
            for (Category category : categoryBox.getCategories()) {
                index++;
                stmt.setString(index, category.getName());
            }
        }

        return stmt;
    }


    /**
     * inserts a list of domains into a PreparedStatement
     *
     * @param domains domains tobe added to a preparedStatement
     * @param stmt    PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement insertDomainInPreparedStatement(PreparedStatement stmt, ArrayList<Domain> domains) throws SQLException {
        int index = 1;
        for (Domain domain : domains) {
            stmt.setString(index++, "%@" + domain.getIdentifier().replaceAll("@", "") + "%");
        }
        return stmt;
    }

    /**
     * Retruns the where statement for the query getting the contacts by domain
     *
     * @param domainsCount count of domains
     * @throws SQLException
     */
    protected String getWhereStatementForDomainsPreparedStatement(int domainsCount) {
        StringBuilder builderCategories = new StringBuilder();
        builderCategories.append("WHERE email LIKE ?");
        for (int i = 1; i < domainsCount; i++) {
            builderCategories.append(" OR email LIKE ?");
        }
        return builderCategories.toString();
    }

    /**
     * @param conn    open connection
     * @param domains domains
     * @return list of contacts by domain
     * @throws SQLException
     */
    private List<Contact> getContactsFromDomains(Connection conn, ArrayList<Domain> domains) throws SQLException {
        PreparedStatement stmt = insertDomainInPreparedStatement(
                conn.prepareStatement("SELECT DISTINCT * FROM quintormss.Contact cont " + getWhereStatementForDomainsPreparedStatement(domains.size())),
                domains
        );
        List<Contact> contacts = getContactsFromResultSet(stmt.executeQuery());
        stmt.close();
        return contacts;
    }


    /**
     * Converts a ResultSet with domains to a list of DomainPojo's
     *
     * @param resultSet ResultSet with domains
     * @return Domain POJO's
     * @throws SQLException
     */
    private ArrayList<Domain> getDomainsFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Domain> domains = new ArrayList<>();
        while (resultSet.next()) {
            Domain domain = new Domain();
            domain.setIdentifier(resultSet.getString("identifier"));
            domains.add(domain);
        }
        return domains;
    }

    /**
     * Converts a ResultSet with contacts to a list of contact Pojo's
     *
     * @param resultSet ResultSet with contacts
     * @return Contact POJO's
     * @throws SQLException
     */
    private List<Contact> getContactsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        while (resultSet.next()) {
            Contact contact = new Contact(resultSet.getString("email"), resultSet.getString("dateAdded"), resultSet.getBoolean("active"));
            String value = resultSet.getString("importName");
            if (value != null) contact.setImportName(value);
            value = resultSet.getString("firstName");
            if (value != null) contact.setFirstName(value);
            value = resultSet.getString("lastName");
            if (value != null) contact.setLastName(value);
            value = resultSet.getString("phoneNumber");
            if (value != null) contact.setPhoneNumber(value);

            contacts.add(contact);
        }
        return contacts;
    }
}