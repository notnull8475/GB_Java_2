package ru.geekbrains.java3.hm5;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass implements WinnerSaver {
    public static final int CARS_COUNT = 4;

    static boolean nextFinish = false;
    String winner = null;

    public static void main(String[] args) {
        MainClass mainClass = new MainClass();
        mainClass.startRacing();

    }

    private void startRacing() {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");


        CyclicBarrier startStop = new CyclicBarrier(CARS_COUNT, () -> {
            if (!nextFinish) {
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                nextFinish = true;
            } else {
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
                System.out.println("Победитель - " + winner);
            }
        });
        Semaphore tunnel = new Semaphore(CARS_COUNT / 2);


        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), startStop, tunnel, this);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
    }

    @Override
    public void saveWinner(String winner) {
        this.winner = winner;
    }
}

