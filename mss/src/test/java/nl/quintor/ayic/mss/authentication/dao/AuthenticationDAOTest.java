package nl.quintor.ayic.mss.authentication.dao;

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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationDAOTest {
    @Mock
    Connection mockConn;

    @Mock
    PreparedStatement mockPreparedStmnt;

    @Mock
    ResultSet mockResultSet;

    AuthenticationDAO spyAuthDao;
    
    @Mock
    Database mockDatabase;
    
    @Before
    public void setUp() throws Exception {
        spyAuthDao = spy(new AuthenticationDAO() {
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

        doNothing().when((Dao) spyAuthDao).setDatabase(anyObject());
        doNothing().when((Dao) spyAuthDao).loadDbDriver(anyObject());

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStmnt.executeUpdate()).thenReturn(1);

        when(mockResultSet.getString(anyString())).thenReturn("test3");
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
    }



    @Test
    public void testInsertToken() throws Exception {
        spyAuthDao.insertToken("user","token");
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }
    @Test(expected = SQLException.class)
    public void testInsertTokenCatch() throws Exception {
        when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());
        spyAuthDao.insertToken(anyString(),anyString());
        verify(spyAuthDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testGetPassword() throws Exception {
        String var = spyAuthDao.getPassword("user");
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
        assertEquals(var,"test3");
    }
    @Test(expected = SQLException.class)
    public void testGetPasswordCatch() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyAuthDao.getPassword(anyString());
        verify(spyAuthDao, times(1)).logAndThrow(new SQLException());
    }
    @Test
    public void testCheckToken() throws Exception {
        spyAuthDao.checkToken("user");
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }
    @Test(expected = SQLException.class)
    public void testCheckTokenCatch() throws Exception {
        when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
        spyAuthDao.checkToken(anyString());
        verify(spyAuthDao, times(1)).logAndThrow(new SQLException());
    }
    @Test
    public void testDeleteToken() throws Exception {
        spyAuthDao.deleteToken("token");
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }
    @Test(expected = SQLException.class)
    public void testDeleteTokenCatch() throws Exception {
        when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());
        spyAuthDao.deleteToken(anyString());
        verify(spyAuthDao, times(1)).logAndThrow(new SQLException());
    }
}