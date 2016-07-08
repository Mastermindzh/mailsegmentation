package nl.quintor.ayic.mss.statistics.dao;


import nl.quintor.ayic.mss.database.dao.Dao;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsDao extends Dao implements IStatisticsDao{

    public StatisticsDao() throws SQLException, IOException {
        super();
    }

    public JSONObject tableCount() throws SQLException {
        JSONObject json = new JSONObject();
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT (SELECT count(*) FROM Contact) AS contactCount, \n" +
                    "\t\t(SELECT count(*) FROM Domain) AS domainCount,\n" +
                    "        (SELECT count(*) FROM Category) AS categoryCount,\n" +
                    "        (SELECT count(*) FROM User) AS userCount");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                json.put("contactCount",resultSet.getString("contactCount"));
                json.put("domainCount",resultSet.getString("domainCount"));
                json.put("categoryCount",resultSet.getString("categoryCount"));
                json.put("userCount",resultSet.getString("userCount"));
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }
        return json;
    }
}
