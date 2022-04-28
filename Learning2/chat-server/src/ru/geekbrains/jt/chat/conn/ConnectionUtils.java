package ru.geekbrains.jt.chat.conn;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection getConnection()
            throws ClassNotFoundException, SQLException {

        return PostgreSQLConnUtil.getMyPostgreSQLConnection();

    }

    public static void closeQuietly(Connection conn) {
        try {
            conn.close();
        } catch (Exception ignored) {
        }
    }

    public static void rollbackQuietly(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception ignored) {
        }
    }
}