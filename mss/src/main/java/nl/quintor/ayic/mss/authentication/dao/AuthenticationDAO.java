package nl.quintor.ayic.mss.authentication.dao;

import nl.quintor.ayic.mss.database.dao.Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationDAO extends Dao implements  IAuthenticationDAO {

    public AuthenticationDAO() throws SQLException, IOException {
        super();
    }

    @Override
    public void insertToken(String username, String token) throws SQLException {
        Connection conn = getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Tokens(token, expirationDate,User_username) VALUES(?,now(),?)");
            stmt.setString(1,token);
            stmt.setString(2,username);
            executeAndClose(conn,stmt);
        } catch (SQLException e) {
            logAndThrow(e);
        }
    }

    @Override
    public String getPassword(String username) throws SQLException {
        Connection conn = getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM User where username=?");
            stmt.setString(1,username);
            ResultSet result = stmt.executeQuery();
            result.next();
            String password = result.getString("password");
            stmt.close();
            return password;
        } catch (SQLException e) {
            logAndThrow(e);
        }finally {
            conn.close();
        }
        return null;
    }

    @Override
    public boolean checkToken(String token) throws SQLException {
        Connection conn = getConnection();
        boolean returnValue = false;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) as tokens FROM Tokens where token = ?");
            stmt.setString(1,token);
            ResultSet result = stmt.executeQuery();
            result.next();
            if(result.getInt("tokens") > 0){
                returnValue = true;
            }
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }finally {
            conn.close();
        }
        return returnValue;
    }

    @Override
    public void deleteToken(String token) throws SQLException {
        Connection conn = getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Tokens where token = ?");
            stmt.setString(1,token);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logAndThrow(e);
        }finally {
            conn.close();
        }
    }
}
