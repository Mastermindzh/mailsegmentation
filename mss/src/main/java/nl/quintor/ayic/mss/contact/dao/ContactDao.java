package nl.quintor.ayic.mss.contact.dao;

import com.google.inject.Inject;
import nl.quintor.ayic.mss.category.dao.CategoryDao;
import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.database.dao.Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactDao extends Dao implements IContactDAO {

    @Inject
    CategoryDao categoryDao;

    public ContactDao() throws SQLException, IOException {
        super();
        categoryDao = new CategoryDao();
    }

    public ContactDao(CategoryDao paramdao) throws SQLException,IOException{
        categoryDao = paramdao;
    }

    public void addContact(Contact contact) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) as amount FROM Contact where email = ?");
            stmt.setString(1, contact.getEmail());
            ResultSet result = stmt.executeQuery();
            result.next();
            if (result.getInt("amount") > 0) {
                setActive(contact);
            } else {
                insertContact(contact);
            }
            closeConn(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    public void updateContact(Contact contact, String oldEmail) throws SQLException {
        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement("UPDATE Contact SET email=?, firstname=?, lastname=?, phoneNumber=? WHERE email=?");
            stmt.setString(1, contact.getEmail());
            stmt.setString(2, contact.getFirstName());
            stmt.setString(3, contact.getLastName());
            stmt.setString(4, contact.getPhoneNumber());
            stmt.setString(5, oldEmail);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }


    public void deleteAllCategoriesForContact(String email) throws SQLException {
        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Contact_has_Category WHERE Contact_email=?");
            stmt.setString(1, email);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }


    /**
     * will update a contacts active status to a 1 in the database
     *
     * @param contact contact to be updated
     * @throws SQLException
     */
    protected void setActive(Contact contact) throws SQLException {
        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement("UPDATE Contact SET active='1' WHERE email = ?;");
            stmt.setString(1, contact.getEmail());
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    /**
     * will insert a new row into the database
     *
     * @param contact contact to be inseterd
     * @throws SQLException
     */
    protected void insertContact(Contact contact) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Contact VALUES(?,?,?,?,?,?,?)");
            stmt.setString(1, contact.getEmail());
            stmt.setString(2, contact.getFirstName());
            stmt.setString(3, contact.getLastName());
            stmt.setString(4, contact.getPhoneNumber());
            if (contact.getDateAdded() == null) {
                stmt.setString(5, contact.getDateAdded());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                stmt.setString(5, dateFormat.format(date));
            } else {
                stmt.setString(5, contact.getDateAdded());
            }
            stmt.setBoolean(6, contact.isActive());
            stmt.setString(7, contact.getImportName());
            executeAndClose(conn, stmt);
        } catch (SQLException e) { logAndThrow(e); }
    }

    public void deleteContact(String mail) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Contact SET active='0' WHERE email = ?;");
            stmt.setString(1, mail);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public void permDeleteContact(String mail) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Contact WHERE email = ?;");
            stmt.setString(1, mail);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }


    @Override
    public void addContactToCategories(String mail, ArrayList<String> categories) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT IGNORE INTO Contact_has_Category (Contact_email, Category_name) VALUES (?,?);");
            for (String category : categories) {
                stmt.setString(1, mail);
                stmt.setString(2, category);
                stmt.executeUpdate();
            }
            closeConn(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public List<Contact> getContacts() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT *, (SELECT group_concat(Category_Name) FROM Contact_has_Category WHERE Contact_email = c.email) AS csv FROM Contact AS c");
            getContacts(contacts, conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return contacts;
    }

    @Override
    public Contact getContact(String mail) throws SQLException {
        Contact contact = new Contact();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT email,firstName,lastName,phoneNumber,dateAdded,active,importName FROM Contact WHERE email = ?");
            stmt.setString(1, mail);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Contact c = fillContact(result);
                c.setCategories(categoryDao.findCategories(result.getString("email")));
                contact = c;
            }
            closeConn(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return contact;
    }

    public Contact fillContact(ResultSet result) throws SQLException {
        Contact contact = new Contact(result.getString("email"), result.getString("dateAdded"), result.getBoolean("active"));
        contact.setFirstName(result.getString("firstName"));
        contact.setLastName(result.getString("lastName"));
        contact.setPhoneNumber(result.getString("phoneNumber"));
        contact.setImportName(result.getString("importName"));
        return contact;
    }

    public List<Contact> getContactsInRange(int min, int max,boolean active) throws  SQLException{
        List<Contact> contacts = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT *, (SELECT group_concat(Category_Name) FROM Contact_has_Category WHERE Contact_email = c.email) AS csv FROM Contact AS c WHERE c.active = ? LIMIT ?,?");
            stmt.setBoolean(1,active);
            stmt.setInt(2,min);
            stmt.setInt(3,max);
            getContacts(contacts, conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return contacts;
    }

    public List<Contact> searchContacts(String search, boolean active) throws  SQLException{
        List<Contact> contacts = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT *, (SELECT group_concat(Category_Name) FROM Contact_has_Category WHERE Contact_email = c.email) AS csv FROM Contact AS c HAVING (c.email LIKE ? OR c.firstName LIKE ? OR c.lastName LIKE ? OR c.phoneNumber LIKE ? OR c.dateAdded LIKE ? OR c.importName LIKE ? OR (LOCATE(SUBSTRING(?,2,LENGTH(?)-2),csv) > 0))AND c.active = ?");
            stmt.setString(1,"%"+search+"%");
            stmt.setString(2,"%"+search+"%");
            stmt.setString(3,"%"+search+"%");
            stmt.setString(4,"%"+search+"%");
            stmt.setString(5,"%"+search+"%");
            stmt.setString(6,"%"+search+"%");
            stmt.setString(7,"%"+search+"%");
            stmt.setString(8,"%"+search+"%");
            stmt.setBoolean(9,active);

            getContacts(contacts, conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return contacts;
    }

    private void getContacts(List<Contact> contacts, Connection conn, PreparedStatement stmt) throws SQLException {
        ResultSet result = stmt.executeQuery();
        while (result.next()) {
            Contact c = fillContact(result);
            String csv = result.getString("csv");
            if(!(csv == null)){
                String[] categories = result.getString("csv").split(",");
                for(String s:categories){
                    c.getCategories().add(new Category(s));
                }
            }
            contacts.add(c);
        }
        closeConn(conn, stmt);
    }
}