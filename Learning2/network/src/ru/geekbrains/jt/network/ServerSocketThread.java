package ru.geekbrains.jt.network;

public class ServerSocketThread extends Thread{

    private int port;

    ServerSocketThread(String name, int port){
        super(name);
        this.port = port;
        start();
    }

    @Override
    public void run() {
        System.out.println("Server started");
        while (!isInterrupted()){
            System.out.println("Server in running");
            try{
                sleep(3000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                interrupt();
            }
        }
        System.out.println("server stopped");
    }
}
