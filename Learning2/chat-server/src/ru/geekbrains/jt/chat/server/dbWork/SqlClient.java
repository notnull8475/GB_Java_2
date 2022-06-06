package ru.geekbrains.jt.chat.server.dbWork;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SqlClient {
    private static final Logger log = LogManager.getLogger(SqlClient.class);

    public synchronized static Connection connect() throws ClassNotFoundException, SQLException {
        log.trace(" GET SQL CONNECTION ");
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:Learning2/chat-server/clients-db.sqlite");

    }

    public synchronized static void disconnect(Connection conn) {
        try {
            log.trace("CONNECTION CLOSE");
            conn.close();
        } catch (SQLException throwables) {
            log.error("SQL CLOSE ERROR " + throwables.getSQLState() + " ERROR MESSAGE " + throwables.getMessage());
            throw new RuntimeException(throwables);
        }
    }


}
