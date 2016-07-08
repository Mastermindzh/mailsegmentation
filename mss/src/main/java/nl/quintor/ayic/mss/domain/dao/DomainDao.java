package nl.quintor.ayic.mss.domain.dao;

import nl.quintor.ayic.mss.database.dao.Dao;
import nl.quintor.ayic.mss.domain.domain.Domain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DomainDao extends Dao implements IDomainDao {

    public static final String constActive = "active";
    public static final String constIdentifier = "identifier";

    public DomainDao() throws SQLException, IOException {
        super();
    }

    @Override
    public void addDomain(Domain domain) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) as amount FROM Domain where Identifier = ?");
            stmt.setString(1, domain.getIdentifier());
            ResultSet result = stmt.executeQuery();
            result.next();
            if (result.getInt("amount") > 0) {
                setActive(domain);
            } else {
               insertDomain(domain);
            }
            closeConn(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    /**
     * update a domain to become active again
     *
     * @param domain update a domain to be active again
     * @throws SQLException
     */
    protected void setActive(Domain domain) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Domain SET active='1' WHERE identifier = ?;");
            stmt.setString(1, domain.getIdentifier());
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    /**
     * add a new domain
     *
     * @param domain domain te be added
     * @throws SQLException
     */
    protected void insertDomain(Domain domain) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Domain VALUES(?,?)");
            stmt.setString(1, domain.getIdentifier());
            stmt.setBoolean(2, domain.isActive());
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public void deleteDomain(String domain) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Domain SET active='0' WHERE identifier = ?;");
            stmt.setString(1, domain);
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public List<Domain> getDomains() throws SQLException {
        List<Domain> domains = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT identifier,active FROM Domain");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                domains.add(new Domain(result.getString(constIdentifier), result.getBoolean(constActive)));
            }
            closeConn(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return domains;
    }

    @Override
    public Domain getDomain(String identifier) throws SQLException {
        Domain domain = new Domain();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT identifier,active FROM Domain where identifier = ?");
            stmt.setString(1, identifier);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                domain.setIdentifier(result.getString(constIdentifier));
                domain.setActive(result.getBoolean(constActive));
            }
            closeConn(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return domain;
    }

    @Override
    public void updateDomain(String identifier, String newIdentifier) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Domain SET identifier=? WHERE identifier = ?;");
            stmt.setString(1, newIdentifier);
            stmt.setString(2, identifier);
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public void addCategoryToDomain(String category, String domain) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Domain_has_Category (DomainIdentifier, CategoryName) VALUES (?,?);");
            stmt.setString(1, domain);
            stmt.setString(2, category);
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public void addCategoriesToDomain(String identifier, ArrayList<String> categories) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT IGNORE INTO Domain_has_Category (DomainIdentifier, CategoryName) VALUES (?,?);");
            for (String category : categories) {
                stmt.setString(1, identifier);
                stmt.setString(2, category);
                stmt.executeUpdate();
            }
            closeConn(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    public void deleteAllCategoriesForDomain(String identifier) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Domain_has_Category WHERE DomainIdentifier=?");
            stmt.setString(1, identifier);
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public List<Domain> getDomainsWithCount() throws SQLException {
        Connection conn = getConnection();
        List<Domain> domains = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("select identifier,active, (select count(*) from Contact as c where LOCATE(d.identifier,substring(c.email,instr(c.email, '@'))) > 0) as uses from Domain as d");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Domain domain = new Domain(resultSet.getString(constIdentifier),resultSet.getBoolean(constActive));
                domain.setCountContacts(resultSet.getInt("uses"));
                domains.add(domain);
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }finally {
            conn.close();
        }
        return domains;
    }

}
