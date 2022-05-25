package ru.geekbrains.java2.lesson5.online;

public class ThreadMain {

    static class MyThread extends Thread {
        private final int sleep;

        MyThread(String name, int sleep) {
            super(name);
            this.sleep = sleep;
        }

        void hello() {
            System.out.println("Hello " + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            System.out.println("Hello " + Thread.currentThread().getName());
            while (!isInterrupted()) {
                try {
                    sleep(sleep);
                    interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread("name1", 1000);
        MyThread myThread1 = new MyThread("name2", 3000);
        MyThread myThread2 = new MyThread("name3", 5000);
        MyThread myThread3 = new MyThread("name4", 2000);
        myThread.start();
        myThread1.start();
        myThread2.start();
        myThread3.start();
        new MyThread("name5", 1000).start();
        System.out.println("finish");

    }
}
