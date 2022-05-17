package ru.geekbrains.jt.chat.server.dbWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final String GET_NICK = "select nickname from users where login=? and password=?";
    private static final String REGISTER_USER = "insert into users(login, password,nickname) values(?,?,?)";
    private static final String UPDATE_USER_NICK = "update users set nickname=? where login=?";

    public synchronized static String getNick(Connection conn, String login, String password) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(GET_NICK);
        ps.setString(1, login);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("nickname");
        } else return null;
    }

    public synchronized static boolean registerUser(Connection conn, String login, String password, String nick) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(REGISTER_USER);
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setString(3, nick);
        return ps.execute();
    }

    public synchronized static int updateUserNick(Connection conn, String login, String nick) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_NICK);
        ps.setString(1, nick);
        ps.setString(2, login);
        return ps.executeUpdate();
    }
}
