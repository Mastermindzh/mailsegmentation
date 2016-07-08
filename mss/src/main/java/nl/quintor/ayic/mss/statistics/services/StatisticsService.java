package nl.quintor.ayic.mss.statistics.services;

import nl.quintor.ayic.mss.authentication.domain.Secured;
import nl.quintor.ayic.mss.statistics.dao.IStatisticsDao;
import nl.quintor.ayic.mss.statistics.dao.StatisticsDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@Path("/statistics")
public class StatisticsService implements IStatisticsService {

    private IStatisticsDao dao;
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(StatisticsService.class));

    public StatisticsService(IStatisticsDao paramDao) throws SQLException {
        dao = paramDao;
    }

    public StatisticsService() throws SQLException, IOException {
        dao = new StatisticsDao();
    }

    @GET
    @Path("/tableCount")
    @Produces("text/plain")
    @Secured
    public String tableCount() throws SQLException {
        return dao.tableCount().toString();
    }
}
