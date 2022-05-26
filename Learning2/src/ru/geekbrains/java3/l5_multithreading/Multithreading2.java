package ru.geekbrains.java3.l5_multithreading;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Multithreading2 {
    public static void main(String[] args) {

//        reentrantLockExample();



    }

    private static void reentrantLockExample() {
        Lock lock = new ReentrantLock();

        new Thread(() -> {
            System.out.println("Before lock 1");
            try {
                lock.lock();
                System.out.println("got lock 1");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("releasing lock 1");
                lock.unlock();
            }
        }).start();
//        new Thread(() -> {
//            System.out.println("Before lock 2");
//            try{
//                lock.lock();
//                System.out.println("got lock 2");
//                Thread.sleep(3500);
//            } catch (InterruptedException e){
//                e.printStackTrace();
//            } finally {
//                System.out.println("releasing lock 2");
//                lock.unlock();
//            }
//        }).start();
        new Thread(() -> {
            System.out.println("Before lock 3");

            try {
                if (lock.tryLock(4, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("got lock 3");
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("releasing lock 3");
                        lock.unlock();
                    }
                } else System.out.println("Do nothing");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
}
