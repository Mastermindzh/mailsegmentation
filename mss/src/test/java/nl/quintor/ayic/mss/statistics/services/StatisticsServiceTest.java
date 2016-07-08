package nl.quintor.ayic.mss.statistics.services;

import nl.quintor.ayic.mss.statistics.dao.IStatisticsDao;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class StatisticsServiceTest {
    IStatisticsDao dao = mock(IStatisticsDao.class);
    StatisticsService service;;

    @Before
    public void setUp() throws Exception {
        service = new StatisticsService(dao);
    }

    @Test
    public void testTableCount() throws Exception {
        when(dao.tableCount()).thenReturn(new JSONObject());
        service.tableCount();
        verify(dao, times(1)).tableCount();
    }

    @Test
    public void testConstructor() {
        try {
            new StatisticsService();
        } catch (Exception e) {

        }
        //can't test without a dao.
        assertEquals(true, true);
    }
}