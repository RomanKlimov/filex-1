/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.registration.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;

public class FeedbackDAO {

    public boolean addFeedback(String firstName, String lastName, String email, String message) throws DBException, AlreadyExistException, IOException {
        Connection conn = DataSource.getConnection();

        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO feedback(firstname, lastname, email, message) VALUES (?,?,?,?)");
            int i = 1;
            st.setString(i++, firstName);
            st.setString(i++, lastName);
            st.setString(i++, email);
            st.setString(i++, message);
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
        return false;
    }
}
