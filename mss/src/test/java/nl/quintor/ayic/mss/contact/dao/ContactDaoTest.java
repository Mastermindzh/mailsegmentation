package nl.quintor.ayic.mss.contact.dao;

import nl.quintor.ayic.mss.category.dao.CategoryDao;
import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.database.Database;
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
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContactDaoTest {

    @Mock
    Connection mockConn;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    ContactDao spyContactDao;
    CategoryDao spyCategoryDao;

    @Mock
    Contact mockContact;

    @Mock
    Database mockDatabase;

    @Before
    public void setUp() throws Exception {

        spyCategoryDao = spy(new CategoryDao() {
            @Override
            public void loadDbDriver(Database database) {
            }

            @Override
            public void setDatabase(Database database) {

            }

            @Override
            public Database getDatabase() {
                return mockDatabase;
            }

            @Override
            public Connection getConnection() {
                return mockConn;
            }

            @Override
            public void logAndThrow(SQLException e) throws SQLException {
                throw new SQLException();
            }
            @Override
            public List<Category> findCategories(String email) throws SQLException{
                return new ArrayList<Category>();
            }
        });
        spyContactDao = spy(new ContactDao(spyCategoryDao) {
            @Override
            public void loadDbDriver(Database database) {
            }

            @Override
            public void setDatabase(Database database) {

            }

            @Override
            public Database getDatabase() {
                return mockDatabase;
            }

            @Override
            public Connection getConnection() {
                return mockConn;
            }

            @Override
            public void logAndThrow(SQLException e) throws SQLException {
                throw new SQLException();
            }
        });

        doNothing().when(spyCategoryDao).setDatabase(anyObject());
        doNothing().when(spyCategoryDao).loadDbDriver(anyObject());

        doNothing().when(spyContactDao).setDatabase(anyObject());
        doNothing().when(spyContactDao).loadDbDriver(anyObject());

        when(spyContactDao.getConnection()).thenReturn(mockConn);
        when(spyCategoryDao.getConnection()).thenReturn(mockConn);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStmnt.executeUpdate()).thenReturn(1);
        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());

        when(mockResultSet.getString(anyString())).thenReturn("test2");
        when(mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(0);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        when(mockContact.getEmail()).thenReturn("test2");

        doNothing().when(spyContactDao).logAndThrow(new SQLException());
        doNothing().when(spyCategoryDao).logAndThrow(new SQLException());


    }

    @Test(expected = SQLException.class)
    public void testAddContactSQLException() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyContactDao.addContact(mockContact);
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }


    @Test
    public void testAddContact() throws Exception {
        doNothing().when(spyContactDao).setActive(anyObject());
        doNothing().when(spyContactDao).insertContact(anyObject());

        spyContactDao.addContact(mockContact);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();

        when(mockResultSet.getInt(anyString())).thenReturn(0);
        spyContactDao.addContact(mockContact);

        verify(spyContactDao, times(1)).insertContact(mockContact);

    }

    @Test
    public void testUpdateContact() throws Exception {
        spyContactDao.updateContact(mockContact, "test");

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(5)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testUpdateContactSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.updateContact(mockContact, "test");
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testDeleteAllCategoriesForContact() throws Exception {
        spyContactDao.deleteAllCategoriesForContact(anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteAllCategoriesForContactSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.deleteAllCategoriesForContact("test");
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testDeleteContact() throws Exception {
        spyContactDao.deleteContact(anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteContactThrowSQLExeption() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.deleteContact(anyString());
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testAddContactToCategories() throws Exception {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            strings.add("test");
        }

        spyContactDao.addContactToCategories("test", strings);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(4)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(2)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testAddContactToCategoriesThrowSQLExeption() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.addContactToCategories("test", new ArrayList<>());
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetContacts() throws Exception {
        spyContactDao.getContacts();
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetContactsThrowSQLExeption() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.getContacts();
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetContact() throws Exception {
        spyContactDao.getContact(anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetContactThrowSQLExeption() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.getContact("test");
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testSetActive() throws Exception {
        spyContactDao.setActive(mockContact);
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testSetActiveSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.setActive(mockContact);
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testInsertContact() throws Exception {
        spyContactDao.insertContact(mockContact);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(7)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setBoolean(anyInt(), anyBoolean());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test
    public void testInsertContactDateAddedNotNull() throws Exception {
        when(mockContact.getDateAdded()).thenReturn("test");
        spyContactDao.insertContact(mockContact);
        verify(mockPreparedStmnt, times(6)).setString(anyInt(), anyString());
    }

    @Test(expected = SQLException.class)
    public void testInsertContactThrowSQLExeption() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.insertContact(mockContact);
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testPermDeleteContact() throws Exception {
        spyContactDao.permDeleteContact(anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testPermDeleteContactCatch() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.permDeleteContact(anyString());
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetContactsInRange() throws Exception {
        spyContactDao.getContactsInRange(anyInt(),anyInt(),anyBoolean());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetContactsInRangeCatch() throws Exception{
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.getContactsInRange(anyInt(),anyInt(),anyBoolean());
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testSearchContacts() throws Exception{
        spyContactDao.searchContacts(anyString(),anyBoolean());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testSearchContactsCatch() throws Exception{
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyContactDao.searchContacts(anyString(),anyBoolean());
        verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }
}