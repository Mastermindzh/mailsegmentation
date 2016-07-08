package nl.quintor.ayic.mss.statistics.services;

import java.sql.SQLException;

public interface IStatisticsService {
    String tableCount() throws SQLException;
}
