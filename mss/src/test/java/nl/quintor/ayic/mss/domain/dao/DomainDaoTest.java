package nl.quintor.ayic.mss.domain.dao;

import nl.quintor.ayic.mss.database.Database;
import nl.quintor.ayic.mss.domain.domain.Domain;
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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DomainDaoTest {


    @Mock
    Connection mockConn;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    DomainDao spyDomainDao;

    @Mock
    Domain mockDomain;

    @Mock
    Database mockDatabase;

    @Before
    public void setUp() throws Exception {
        spyDomainDao = spy(new DomainDao() {
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

        doNothing().when(spyDomainDao).setDatabase(anyObject());
        doNothing().when(spyDomainDao).loadDbDriver(anyObject());

        when(spyDomainDao.getConnection()).thenReturn(mockConn);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStmnt.executeUpdate()).thenReturn(1);

        when(mockResultSet.getString(anyString())).thenReturn("test2");
        when(mockResultSet.getBoolean(anyString())).thenReturn(true);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(0);

        when(mockDomain.getIdentifier()).thenReturn("test");
        doNothing().when(spyDomainDao).logAndThrow(new SQLException());
    }

    @Test
    public void testAddDomain() throws Exception {
        doNothing().when(spyDomainDao).setActive(anyObject());
        doNothing().when(spyDomainDao).insertDomain(anyObject());

        spyDomainDao.addDomain(mockDomain);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();

        when(mockResultSet.getInt(anyString())).thenReturn(0);
        spyDomainDao.addDomain(mockDomain);

        verify(spyDomainDao, times(1)).insertDomain(mockDomain);
    }

    @Test(expected = SQLException.class)
    public void testAddContactSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.addDomain(mockDomain);
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testSetActive() throws Exception {
        spyDomainDao.setActive(mockDomain);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testSetActiveSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.setActive(mockDomain);
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testInsertDomain() throws Exception {
        spyDomainDao.insertDomain(mockDomain);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setBoolean(anyInt(), anyBoolean());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testInsertDomainSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.insertDomain(mockDomain);
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testDeleteDomain() throws Exception {
        spyDomainDao.deleteDomain(anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDomainSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.deleteDomain(anyString());
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetDomains() throws Exception {
        spyDomainDao.getDomains();

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetDomainsSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.getDomains();
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetDomain() throws Exception {
        spyDomainDao.getDomain(anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetDomainSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.getDomain(anyString());
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testChange() throws Exception {
        spyDomainDao.updateDomain(anyString(), anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testChangeSQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.updateDomain(anyString(), anyString());
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testAddCategoryToDomain() throws Exception {
        spyDomainDao.addCategoryToDomain(anyString(), anyString());

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testAddCategorySQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.addCategoryToDomain(anyString(), anyString());
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testAddCategoriesToDomain() throws Exception {
        ArrayList<String> categories = new ArrayList<>();
        categories.add(anyString());
        spyDomainDao.addCategoriesToDomain(anyString(), categories);

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }


    @Test(expected = SQLException.class)
    public void testAddCategoriesToDomainCatch() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.addCategoriesToDomain(anyString(), anyObject());
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testDeleteAllCategoriesForDomain() throws Exception {
        spyDomainDao.deleteAllCategoriesForDomain(anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteAllCategoriesForDomainCatch() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.deleteAllCategoriesForDomain(anyString());
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetDomainsWithCount() throws Exception {
        spyDomainDao.getDomainsWithCount();

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();

        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetDomainsWithCountCatch() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyDomainDao.getDomainsWithCount();
        verify(spyDomainDao, times(1)).logAndThrow(new SQLException());
    }
}