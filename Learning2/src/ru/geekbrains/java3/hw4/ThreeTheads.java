package ru.geekbrains.java3.hw4;

public class ThreeTheads {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("A");
                System.out.print("B");
                System.out.print("C");
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("A");
                System.out.print("B");
                System.out.print("C");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("A");
                System.out.print("B");
                System.out.print("C");
            }
        });


        thread.start();
        thread1.start();
        thread2.start();

    }



}
