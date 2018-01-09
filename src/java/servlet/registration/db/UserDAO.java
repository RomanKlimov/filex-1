package servlet.registration.db;

import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;
import servlet.registration.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean addUser(User user) throws DBException, AlreadyExistException {
        Connection conn = DataSource.getConnection();
        if (!containsUser(user)) {
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO users(name, email, password, phonenumber) VALUES (?,?,?,?)");
                int i = 1;
                st.setString(i++, user.getName());
                st.setString(i++, user.getEmail());
                st.setString(i++, user.getPassword());
                st.setString(i++, user.getPhoneNumber());
                st.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DBException();
            }
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO roles(email) VALUES (?)");
                int i = 1;
                st.setString(i++, user.getEmail());
                st.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DBException();
            }
        } else {
            throw new AlreadyExistException();
        }
        return false;
    }

    public User findByEmailAndPass(String email, String password) throws DBException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
            int i = 1;
            st.setString(i++, email);
            st.setString(i++, password);
            ResultSet resultSet = st.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    if (resultSet.getString("password").equals(password)) {
                        User user = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("phonenumber")
                        );
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
        return null;
    }

    public ArrayList findAllUsers() throws DBException {
        Connection conn = DataSource.getConnection();
        ArrayList<User> users = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM view_allusers");
            ResultSet resultSet = st.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phonenumber"),
                            resultSet.getBoolean("admin_role")
                    ));
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
        return null;
    }

    public boolean checkRole(User user) throws DBException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM roles WHERE email=? AND admin_role='true'");
            int i = 1;
            st.setString(i++, user.getEmail());
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

    public boolean setRole(String email, boolean role) throws DBException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE roles SET admin_role=? WHERE email=?");
            int i = 1;
            st.setBoolean(i++, role);
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

    public boolean containsUser(User user) throws DBException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE email=?");
            int i = 1;
            st.setString(i++, user.getEmail());
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

    public boolean delUser(String email) throws DBException {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM purchases WHERE email=?");
            int i = 1;
            st.setString(i++, email);
            st.execute();        } catch (SQLException e) {
            throw new DBException();
        }
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM roles WHERE email=?");
            int i = 1;
            st.setString(i++, email);
            st.execute();
        } catch (SQLException e) {
            throw new DBException();
        }
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
