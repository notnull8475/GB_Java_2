package ru.geekbrains.java2.lesson6;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        try (Socket s = new Socket("127.0.0.1", 8189)) {
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            Scanner sc = new Scanner(System.in);
            while (true) {
                String msg = sc.nextLine();
                System.out.println("test");
                out.writeUTF(msg);
                System.out.println(in.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
