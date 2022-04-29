package ru.geekbrains.jt.chat.core;

import ru.geekbrains.jt.chat.conn.PostgreSQLConnUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBClient {
    private static Connection conn;
    private static Statement st;

    synchronized static void connect() {
        try {
            conn = PostgreSQLConnUtil.getMyPostgreSQLConnection();
            st = conn.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static void disconnect(){
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static String getNick(String login, String password){
        String sql = String.format("select nickname from users where login = '%s' and password = '%s'",login,password);
        try(ResultSet rs = st.executeQuery(sql)){
            if (rs.next()){
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
