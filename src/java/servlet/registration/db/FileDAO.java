package servlet.registration.db;

import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileDAO {

    public boolean addFile(File file) throws DBException, AlreadyExistException {
        Connection conn = DataSource.getConnection();
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO products(user_id, root_link, name) VALUES (?,?,?)");
                int i = 1;

                st.setInt(i++, file.getUser_id());
                st.setString(i++, file.getRoot_link());
                st.setString(i++, file.getName());
                st.execute();
            } catch (SQLException e) {
                throw new DBException();
            }

        return false;
    }
}
