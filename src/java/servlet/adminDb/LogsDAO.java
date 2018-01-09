/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.adminDb;

import FileAdapter.WriteMapToFile;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import servlet.registration.db.DataSource;
import servlet.registration.exceptions.DBException;

/**
 *
 * @author User
 */
public class LogsDAO {

    public String getAllLogs() throws DBException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT l.text,l.date FROM logs l");
            ResultSet resultSet = st.executeQuery();
            HashMap logs = new HashMap<String, Date>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    logs.put(resultSet.getString("text"), resultSet.getDate("date"));
                }
                WriteMapToFile logd = new WriteMapToFile();
                return logd.write(logs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }

        return null;
    }
}
