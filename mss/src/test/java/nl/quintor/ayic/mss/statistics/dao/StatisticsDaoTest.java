package nl.quintor.ayic.mss.statistics.dao;

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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsDaoTest {
    @Mock
    Connection mockConn;

    @Mock
    PreparedStatement mockPreparedStmnt;

    @Mock
    ResultSet mockResultSet;

    @Mock
    Database mockDatabase;

    StatisticsDao spyStatisticsDao;

    @Test
    public void testTableCount() throws Exception {
        spyStatisticsDao.tableCount();
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockPreparedStmnt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Before
    public void setUp() throws Exception {

        spyStatisticsDao = spy(new StatisticsDao() {
            @Override
            public void loadDbDriver(Database database) {
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
            public void setDatabase(Database database) {}

            @Override
            public void logAndThrow(SQLException e) throws SQLException {
                throw new SQLException();
            }
        });

        doNothing().when((Dao) spyStatisticsDao).setDatabase(anyObject());
        doNothing().when((Dao) spyStatisticsDao).loadDbDriver(anyObject());
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStmnt.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
    }
}