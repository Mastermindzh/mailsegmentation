package nl.quintor.ayic.mss.category.dao;

import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.database.Database;
import nl.quintor.ayic.mss.database.dao.Dao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryDaoTest {

    @Mock
    Connection mockConn;

    @Mock
    PreparedStatement mockPreparedStmnt;

    @Mock
    ResultSet mockResultSet;

    CategoryDao spyCategoryDao;

    @Mock
    Category mockCategory;

    @Mock
    Database mockDatabase;

    @Before
    public void setUp() throws Exception {
        spyCategoryDao = spy(new CategoryDao() {
            @Override
            public void loadDbDriver(Database database) { }

            @Override
            public Database getDatabase() {
                return mockDatabase;
            }

            @Override
            public void setDatabase(Database database) { }

            @Override
            public Connection getConnection() {
                return mockConn;
            }

            @Override
            public void logAndThrow(SQLException e) throws SQLException {
                throw new SQLException();
            }
        });

        doNothing().when((Dao) spyCategoryDao).setDatabase(anyObject());
        doNothing().when((Dao) spyCategoryDao).loadDbDriver(anyObject());
        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStmnt.executeUpdate()).thenReturn(1);
        when(mockResultSet.getString(anyString())).thenReturn("test3");
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
    }

    @Test
    public void testFindAll() throws Exception {
        spyCategoryDao.findAll();

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testFindAllSQLException() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.findAll();
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testFindCategories() throws Exception {
        spyCategoryDao.findCategories(anyString());

        verify(mockResultSet, times(1)).getString(anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testFindCategoriesSQLException() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.findCategories(anyString());
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetCategoryFromResultset() throws Exception {
        spyCategoryDao.getCategoryFromResultset(mockResultSet);

        verify(mockResultSet, times(1)).getString(anyString());
    }

    @Test
    public void testAddCategory() throws Exception {
        spyCategoryDao.addCategory(mockCategory);

        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testAddCategorySQLException() throws Exception {
        when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());
        spyCategoryDao.addCategory(mockCategory);
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testFindByName() throws Exception {
        spyCategoryDao.findByName(anyString());

        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).getString(anyString());

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testFindByNameSQLException() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.findByName(anyString());
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testEditCategory() throws Exception {
        spyCategoryDao.editCategory(anyString(), anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testEditCategorySQLException() throws Exception {
        when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());
        spyCategoryDao.editCategory(anyString(), anyString());
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testFindRelated() throws Exception {
        spyCategoryDao.findRelated(anyString());
        verifyOnce();
    }

    public void verifyOnce() throws SQLException {
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }
    @Test
    public void testAddRelatedCategory() throws Exception {
        spyCategoryDao.addRelatedCategory(anyString(),anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test
    public void testFindCategoriesWithDomain() throws Exception {
        spyCategoryDao.findCategoriesWithDomain(anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testFindRelatedCatch() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.findRelated(anyString());
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test(expected = SQLException.class)
    public void testAddRelatedCategoryCatch() throws Exception {
        when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());
        spyCategoryDao.addRelatedCategory(anyString(),anyString());
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test(expected = SQLException.class)
    public void testFindCategoriesWithDomainCatch() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.findCategoriesWithDomain(anyString());
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetCategoriesWithCount() throws Exception {
        spyCategoryDao.getCategoriesWithCount();
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetCategoriesWithCountSQLException() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.getCategoriesWithCount();
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testFindRelatedList() throws Exception {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("test"));
        categories.add(new Category("test"));
        spyCategoryDao.findRelated(categories);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testFindRelatedListCatch() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("test"));
        spyCategoryDao.findRelated(categories);
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        spyCategoryDao.deleteCategory("test");
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteCategoryCatch() throws Exception {
        when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());
        spyCategoryDao.deleteCategory("html");
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetContactsInCategory() throws Exception {
        spyCategoryDao.getContactsInCategory("test");
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }
    @Test(expected = SQLException.class)
    public void testGetContactsInCategoryCatch() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyCategoryDao.getContactsInCategory("html");
        verify(spyCategoryDao, times(1)).logAndThrow(new SQLException());
    }
}