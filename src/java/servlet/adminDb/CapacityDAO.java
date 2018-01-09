/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.adminDb;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import servlet.registration.db.DataSource;
import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.Key;
import servlet.registration.models.User;

/**
 *
 * @author User
 */
public class CapacityDAO {

    public boolean addKeys(String key, int memory, Date validUntil) throws DBException, AlreadyExistException, IOException {
        Connection conn = DataSource.getConnection();

        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO purchases_keys(key, memory, valid_until) VALUES (?,?,?)");
            int i = 1;
            st.setString(i++, key);
            st.setInt(i++, memory);
            st.setDate(i++, validUntil);
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
        return false;
    }
    
    public boolean setKeyToUser(String email,String key) throws DBException, AlreadyExistException, IOException {
        Connection conn = DataSource.getConnection();

        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO purchases(_key, email) VALUES (?,?)");
            int i = 1;
            st.setString(i++, key);
            st.setString(i++, email);
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
        return false;
    }

    public Map getAllUsersCapacity() throws DBException, IOException, IOException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM view_users_capacity");
            ResultSet resultSet = st.executeQuery();
            Map users = new HashMap<String, Integer>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    users.put(resultSet.getString("email"), resultSet.getInt("sum"));
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }

        return null;
    }

    public ArrayList<Key> getAllKeys() throws DBException, SQLException, IOException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM purchases_keys");
            ResultSet resultSet = st.executeQuery();
            ArrayList<Key> keys = new ArrayList<>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    keys.add(new Key(resultSet.getString("key"), resultSet.getInt("memory"), resultSet.getDate("valid_until")));
                }
                return keys;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }

        return null;
    }

    public Integer getUsersCapacity(User user) throws DBException, IOException {
        Connection conn = DataSource.getConnection();
        Integer cap = 0;
        try {
            PreparedStatement st = conn.prepareStatement("SELECT cap.capacity FROM users_capacity cap WHERE email=?");
            st.setString(1, user.getEmail());
            ResultSet resultSet = st.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    cap = resultSet.getInt("capacity");
                }
                return cap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
        return null;
    }

    public boolean setCapacity(String email, int capacity) throws DBException, IOException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE users_capacity SET capacity=? WHERE email=?");
            int i = 1;
            st.setInt(i++, capacity);
            st.setString(i++, email);

            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                return false;
            } else {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    public boolean delUser(String email) throws DBException, IOException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM users WHERE email=?");
            int i = 1;
            st.setString(i++, email);
            st.execute();
            return true;
        } catch (SQLException e) {
            throw new DBException();
        }

    }
}
