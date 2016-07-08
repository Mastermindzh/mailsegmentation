package nl.quintor.ayic.mss.statistics.dao;


import org.json.JSONObject;

import java.sql.SQLException;

public interface IStatisticsDao {
    /**
     * returns counts for all relevant tables
     * @return json formatted response
     * @throws SQLException
     */
    JSONObject tableCount() throws SQLException;
}
