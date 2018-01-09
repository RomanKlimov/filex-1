package servlet.registration.db;

import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.UserFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import servlet.registration.models.User;
import servlet.folder_adapters.createFolder;

public class FolderDAO {
    
    public boolean addFolder(User user) throws DBException, AlreadyExistException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO folders(email, root_link) VALUES (?,?)");
            int i = 1;
            System.out.println(user.getEmail());
            System.out.println(user.getFolder());
            st.setString(i++, user.getEmail());
            st.setInt(i++, user.getFolder());
            
            st.execute();
            
            new createFolder().create(user.getFolder());
        } catch (SQLException e) {
            throw new DBException();
        }
        
        return false;
    }
}
