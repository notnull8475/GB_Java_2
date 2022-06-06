package ru.geekbrains.jt.chat.server.dbWork;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.jt.chat.server.core.ChatServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {
    private static final Logger log = LogManager.getLogger(DBUtils.class);
    private static final String GET_NICK = "select nickname from users where login=? and password=?";
    private final String REGISTER_USER = "insert into users(login, password,nickname) values(?,?,?)";
    private final String UPDATE_USER_NICK = "update users set nickname=? where login=?";

    public synchronized String getNick(Connection conn, String login, String password) throws SQLException {
        log.debug("GET NICK REQUEST FROM DB \nCONN: " + conn + " LOGIN: " + login + " PASSWORD: " + password+ "\nSQL - " + GET_NICK);
        PreparedStatement ps = conn.prepareStatement(GET_NICK);
        ps.setString(1, login);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            log.debug("REQUEST IS DONE, RESULT - " + rs.getString("nickname"));
            return rs.getString("nickname");
        } else {
            log.debug("REQUEST NOT RESPONSE");
            return null;
        }
    }

    public synchronized int registerUser(Connection conn, String login, String password, String nick) throws SQLException {
        log.debug("REGISTER NICK REQUEST \n CONN: " + conn + "\n LOGIN: " + login + " PASSWORD: " + password + " NICK: " + nick + "\nSQL: " + REGISTER_USER);
        PreparedStatement ps = conn.prepareStatement(REGISTER_USER);
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setString(3, nick);
        return ps.executeUpdate();
    }

    public synchronized int updateUserNick(Connection conn, String login, String nick) throws SQLException {
        log.debug("UPDATE NICK REQUEST  \nCONN: " + conn + " LOGIN: " + login + " NICK: " + nick + "\nSQL - " + UPDATE_USER_NICK);
        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_NICK);
        ps.setString(1, nick);
        ps.setString(2, login);
        return ps.executeUpdate();
    }
}
