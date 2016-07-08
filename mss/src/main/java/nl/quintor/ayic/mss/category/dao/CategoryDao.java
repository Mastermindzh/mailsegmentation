package nl.quintor.ayic.mss.category.dao;

import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.database.dao.Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends Dao implements ICategoryDAO {

    public CategoryDao() throws SQLException, IOException {
        super();
    }

    public List<Category> findAll() throws SQLException {
        Connection conn = getConnection();
        ArrayList<Category> categories = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT name from `Category`");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                categories.add(getCategoryFromResultset(resultSet));
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }
        conn.close();
        return categories;
    }

    public List<Category> findCategories(String email) throws SQLException {
        Connection conn = getConnection();
        ArrayList<Category> categories = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT Category_Name from Contact_has_Category WHERE Contact_email=(?)");
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                categories.add(new Category(resultSet.getString("Category_name")));
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }
        conn.close();
        return categories;
    }


    /**
     * Upon receiving a resultset this will try and create a category by using the resultset name
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    protected Category getCategoryFromResultset(ResultSet resultSet) throws SQLException {
        return new Category(resultSet.getString("name"));
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        Connection conn = getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Category VALUES(?)");
            statement.setString(1, category.getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }
        conn.close();
    }

    public List<Category> findByName(String name) throws SQLException {
        Connection conn = getConnection();

        List<Category> categories = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Category where name like ? ");
            stmt.setString(1, name + "%");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                categories.add(new Category(result.getString("name")));
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }
        conn.close();
        return categories;
    }

    public void editCategory(String oldName, String newName) throws SQLException {
        Connection conn = getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Category SET name=(?) WHERE name=(?)");
            stmt.setString(1, newName);
            stmt.setString(2, oldName);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public List<Category> findRelated(String name) throws SQLException {
        List<Category> categories = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT SubCategory from Category_has_Category where Category = ?");
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                categories.add(new Category(result.getString("SubCategory")));
            }
            closeConn(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return categories;
    }

    @Override
    public ArrayList<Category> findRelated(ArrayList<Category> categories) throws SQLException {
        ArrayList<Category> relatedCategories = new ArrayList<>();
        try {
            Connection connection = getConnection();
            StringBuilder questionMarks = new StringBuilder("?");
            for (int i = 1; i < categories.size(); i++) {
                questionMarks.append(", ?");
            }
            PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM quintormss.Category_has_Category where Category in (%s)", questionMarks.toString()));

            for (int i = 0; i < categories.size(); i++) {
                statement.setString(i + 1, categories.get(i).getName());
            }
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                relatedCategories.add(new Category(result.getString("SubCategory")));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return relatedCategories;
    }

    @Override
    public void addRelatedCategory(String category, String subCategory) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Category_has_Category (Category, SubCategory)  VALUES (?,?)");
            stmt.setString(1, category);
            stmt.setString(2, subCategory);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public void deleteCategory(String category) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE from Category where name = (?)");
            stmt.setString(1, category);
            executeAndClose(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public List<Contact> getContactsInCategory(String category) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT c.email,c.firstName,c.lastName,c.phoneNumber,c.dateAdded,c.active,c.importName FROM Contact_has_Category as cc left join Contact as c on cc.Contact_email = c.email where cc.Category_name = ?");
            stmt.setString(1, category);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Contact contact = new Contact(result.getString("email"), result.getString("dateAdded"), result.getBoolean("active"));
                contact.setImportName(result.getString("importName"));
                contact.setLastName(result.getString("lastName"));
                contact.setFirstName(result.getString("firstName"));
                contact.setPhoneNumber(result.getString("phoneNumber"));
                contacts.add(contact);
            }
            closeConn(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return contacts;
    }

    @Override
    public List<Category> findCategoriesWithDomain(String domain) throws SQLException {
        Connection conn = getConnection();
        ArrayList<Category> categories = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT CategoryName from Domain_has_Category WHERE DomainIdentifier=(?)");
            stmt.setString(1, domain);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                categories.add(new Category(resultSet.getString("CategoryName")));
            }
            closeConn(conn, stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return categories;
    }

    @Override
    public List<Category> getCategoriesWithCount() throws SQLException {
        Connection conn = getConnection();
        ArrayList<Category> categories = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("select c.name, IFNULL(uses,0) as uses from Category as c left join (select t1.name, IFNULL(contactCount, 0)+IFNULL(domainCount, 0) as uses from (SELECT name, count(*) as contactCount FROM Category as c right join Contact_has_Category as cc on c.name = Category_name GROUP BY name) as t1 left join (SELECT name, count(*) as domainCount FROM Category as c right join Domain_has_Category as dc on c.name = dc.CategoryName GROUP BY name) as t2 on t1.name = t2.name group by t1.name) as countTable on countTable.name = c.name");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Category category = getCategoryFromResultset(resultSet);
                category.setCountContacts(resultSet.getInt("uses"));
                categories.add(category);
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }finally {
            conn.close();
        }
        return categories;
    }

}
