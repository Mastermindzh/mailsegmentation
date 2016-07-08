package nl.quintor.ayic.mss.export.dao;

import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.database.Database;
import nl.quintor.ayic.mss.database.dao.Dao;
import nl.quintor.ayic.mss.domain.domain.Domain;
import nl.quintor.ayic.mss.export.domain.CategoryBox;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExportDaoTest {

    @Mock
    Connection mockConn;

    @Mock
    PreparedStatement mockPreparedStmnt;

    @Mock
    ResultSet mockResultSet;

    ExportDao spyExportDao;

    @Mock
    Database mockDatabase;

    @Mock
    CategoryBox mockCategoryBox;


    ArrayList<Category> mockCategories = new ArrayList<>();

    ArrayList<CategoryBox> mockCategoryBoxes = new ArrayList<>();

    ArrayList<Domain> mockDomains = new ArrayList<>();

    @Mock
    Domain mockDomain;

    @Mock
    Category mockCategory;

    @Before
    public void setUp() throws Exception {
        spyExportDao = spy(new ExportDao() {
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

        mockCategoryBoxes.add(mockCategoryBox);
        mockCategoryBoxes.add(mockCategoryBox);
        mockCategoryBoxes.add(mockCategoryBox);

        mockCategories.add(mockCategory);
        mockCategories.add(mockCategory);
        mockCategories.add(mockCategory);

        mockDomains.add(mockDomain);
        mockDomains.add(mockDomain);
        mockDomains.add(mockDomain);
    }

    @Test
    public void testGetHavingStatementForCategoryBoxQuery() throws Exception {
        when(mockCategoryBox.getCategories()).thenReturn(mockCategories);

        String result = spyExportDao.getHavingStatementForCategoryBoxQuery(mockCategoryBoxes);
        assertEquals(result,"HAVING  (LOCATE(?,categories) > 0 OR LOCATE(?,categories) > 0 OR LOCATE(?,categories) > 0) AND  (LOCATE(?,categories) > 0 OR LOCATE(?,categories) > 0 OR LOCATE(?,categories) > 0) AND  (LOCATE(?,categories) > 0 OR LOCATE(?,categories) > 0 OR LOCATE(?,categories) > 0)");
    }

    @Test
    public void testGetWhereStatementForDomainsPreparedStatement() throws Exception {
        String result = spyExportDao.getWhereStatementForDomainsPreparedStatement(5);

        assertEquals(result, "WHERE email LIKE ? OR email LIKE ? OR email LIKE ? OR email LIKE ? OR email LIKE ?");
    }

    @Test
    public void testGetExportContacts() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);

        when(mockCategoryBox.getCategories()).thenReturn(mockCategories);
        when(mockCategory.getName()).thenReturn("CategoryName");

        when(mockDomain.getIdentifier()).thenReturn("Identifier");

        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockResultSet.getString(anyString())).thenReturn("any");
        doNothing().when(mockDomain).setIdentifier(anyString());

        spyExportDao.getExportContacts(mockCategoryBoxes, mockDomains);

    }

    @Test(expected = SQLException.class)
    public void testGetExportContactsSQLException() throws Exception {
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("123");
        ArrayList<String> domainsList = new ArrayList<>();
        domainsList.add("123");
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        spyExportDao.getExportContacts(mockCategoryBoxes, mockDomains);
        verify(spyExportDao, times(1)).logAndThrow(new SQLException());
    }
}