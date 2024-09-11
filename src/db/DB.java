package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection connection = null;

    /**
     * Load properties like user, password and dburl from the file db.properties
     * @return
     */
    private static Properties loadProperties(){
        try(FileInputStream fileInputStream = new FileInputStream("db.properties")){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }
        catch (IOException ioException){
            throw new DbException(ioException.getMessage());
        }
    }

    /**
     * Establishes a connection with the dabase from db.properties file
     * @return
     */
    public static Connection getConnection() {
        if(connection == null){
            Properties properties = loadProperties();
            String url = properties.getProperty("dburl");

            try {
                connection = DriverManager.getConnection(url, properties);
            }
            catch (SQLException sqlException){
                throw new DbException(sqlException.getMessage());
            }
        }

        return connection;
    }

    /**
     * Closes the connection with the dabase from db.properties file
     * @return
     */
    public static void closeConnection(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException exception) {
                throw new DbException(exception.getMessage());
            }
        }
    }

    /**
     * Closes a Result Set if open.
     * @param resultSet
     */
    public static void closeResultSet(ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException exception) {
                throw new DbException(exception.getMessage());
            }
        }
    }

    /**
     * Closes a Statement if open.
     * @param statement
     */
    public static void closeStatement(Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException exception) {
                throw new DbException(exception.getMessage());
            }
        }
    }
}
