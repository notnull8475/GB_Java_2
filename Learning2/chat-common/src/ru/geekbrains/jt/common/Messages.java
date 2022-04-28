package ru.geekbrains.jt.common;

public class Messages {
    public static final String DELIMITER = "";

    public static String AUTH_REQUEST = "/auth_request";
    public static String AUTH_ACCEPT = "/auth_accept";
    public static String AUTH_DENY = "/auth_deny";

    public static String MSG_BROADCAST = "/bcast";
    public static String MSG_FORMAT_ERROR = "/msg_error";

    public static String getAuthRequest(String login,String password){
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }

    public static String getAuthAccept() {
        return AUTH_ACCEPT;
    }

    public static String getAuthDeny() {
        return AUTH_DENY;
    }

    public static String getMsgFormatError(String msg) {
        return msg;
    }
}
