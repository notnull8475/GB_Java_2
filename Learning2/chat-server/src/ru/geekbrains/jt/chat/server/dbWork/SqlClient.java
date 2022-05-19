package ru.geekbrains.jt.chat.server.dbWork;

import java.sql.*;

public class SqlClient {

    public synchronized static Connection connect() throws ClassNotFoundException, SQLException {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:Learning2/chat-server/clients-db.sqlite");

    }

    public synchronized static void disconnect(Connection conn) {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }


}
