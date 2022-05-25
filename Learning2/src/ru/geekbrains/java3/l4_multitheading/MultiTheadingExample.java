package ru.geekbrains.java3.l4_multitheading;

import java.util.concurrent.*;

public class MultiTheadingExample {

    public static void main(String[] args) {

//        Thread thread = new Thread(() -> System.out.printf("Hello %s", Thread.currentThread().getName()));
//        Thread thread1 = new Thread();
//        thread.start();

//        Runtime.getRuntime().exec() - запуск внешних прог

//        Thread.getAllStackTraces().forEach((k,v) -> System.out.println(k + " : " + v));

//        futureExample();

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            int j = i;
//            Future<String> f  = executorService.submit()
            executorService.execute(() -> {
                System.out.println("Thread is run "  + Thread.currentThread().getName());
                try {
                    Thread.sleep((long) (2000 * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task is finished");
            });
        }
        System.out.println("All tasks are given");
        executorService.shutdown();

//        ExecutorService es = Executors.newSingleThreadExecutor(new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                return null;
//            }
//        });

    }

    private static void futureExample() {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "Hello from FutureTask";
            }
        });

        new Thread(futureTask).start();

        String result = null;
        try {
            result = futureTask.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }

        System.out.println(result);
    }

}
