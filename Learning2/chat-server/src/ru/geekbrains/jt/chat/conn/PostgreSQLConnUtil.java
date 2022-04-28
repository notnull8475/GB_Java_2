package ru.geekbrains.jt.chat.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnUtil {

    public static Connection getMyPostgreSQLConnection() throws ClassNotFoundException, SQLException {
        String hostname = "localhost";
        String dbName = "chatserver";
        String userName = "chatserver";
        String password = "chatserver";
        return getMyPostgreSQLConnection(hostname, dbName, userName, password);
    }

    public static Connection getMyPostgreSQLConnection(String hostname, String dbName, String userName, String password) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver" );
        String connectionURL = "jdbc:postgresql://" + hostname + ":5432/" + dbName;
        return DriverManager.getConnection(connectionURL, userName, password);
    }
}
