package servlet.registration.db;

import servlet.registration.exceptions.DBException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private final static String DRIVER = "org.postgresql.Driver";
    private final static String CONNECTION_URI = "jdbc:postgresql://localhost:5432/dropbox_db";
    private final static String LOGIN = "postgres";
    private final static String PASSWORD = "1234";
    private static Connection connection;

    public static Connection getConnection() throws DBException {
        if(connection == null){
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(CONNECTION_URI, LOGIN, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new DBException("Can't find Database driver.");
            } catch (SQLException ex) {
                throw new DBException("Can't connect to Database (" + ex.getErrorCode() + ": " + ex.getMessage() + ").");
            }
        }
        return connection;

    }
}
