/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.registration.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import servlet.folder_adapters.createFolder;
import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.User;

/**
 *
 * @author User
 */
public class FileDAO {
    public boolean addFile(int rootLink, String fileName, String fileHash) throws DBException, AlreadyExistException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO files(root_link, file_name, file_hash) VALUES (?,?,?)");
            int i = 1;
            System.out.println(rootLink);
            System.out.println(fileName);
            System.out.println(fileHash);
            st.setInt(i++, rootLink);
            st.setString(i++, fileName);
            st.setString(i++, fileHash);
            st.execute();
        } catch (SQLException e) {
            throw new DBException();
        }
        
        return false;
    }
}
