package ru.geekbrains.lesson5.online.chat;

public class ChatServer {
    ServerSocketThread server;

    public void start(int port) {
        System.out.println("Server started at port " + port);
        if (server != null && server.isAlive()) {
            System.out.println("Server is already started");
        } else {
            server = new ServerSocketThread("Chat server", port);
        }
    }

    public void stop() {
        System.out.println("Server stopped");
        if (server != null || server.isAlive()) {
            server.interrupt();
        } else {
            System.out.println("server is not running");
        }
    }

}
