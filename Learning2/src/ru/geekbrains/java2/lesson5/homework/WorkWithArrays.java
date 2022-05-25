package ru.geekbrains.java2.lesson5.homework;

public class WorkWithArrays {
    static final int size = 10000000;
    static final int h = size / 2;

    public static void fillArray(float[] arr) {
        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }
    }

    // arr[i] = arr[i] * sin(0.2 + i / 5) * cos(0.2 + i / 5) * Math.cos(0.4f + i / 2));
// 2 * sin(0.2 + 0.2*i) * cos(0.2 + 0.2*i) = sin(2*(0.2 + 0.2*i))
// arr[i] = arr[i] *0.5* sin(0.4 + 0.4*i) * Math.cos(0.4f + i / 2));
    static void calcArray(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * 0.5f * Math.sin((0.4 + 0.4 * i)) * Math.cos(0.4 + 0.5 * i));
        }
    }

    static void reCalcArray(float[] arr) throws InterruptedException {

        long t = System.nanoTime();
        float[] arr1 = new float[h];
        float[] arr2 = new float[h];
        System.arraycopy(arr, 0, arr1, 0, h);
        System.arraycopy(arr, h, arr2, 0, h);
        long delta = (System.nanoTime() - t);
        System.out.printf("Время деления:  %f\n", delta*1e-9f);

        ArrayThread thread1 = new ArrayThread(arr1);
        ArrayThread thread2 = new ArrayThread(arr2);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        t = System.nanoTime();
        System.arraycopy(arr1, 0, arr, 0, h);
        System.arraycopy(arr2, 0, arr, h, h);
        delta = (System.nanoTime() - t);
        System.out.printf("Время склейки: %f\n", delta*1e-9f);

    }

    static class ArrayThread extends Thread {
        private float[] arr;

        ArrayThread(float[] arr) {
            this.arr = arr;
        }

        @Override
        public void run() {
            long t = System.nanoTime();
            calcArray(arr);
            long delta = (System.nanoTime() - t);
            System.out.printf("Время расчета в потоке %s: %f\n", Thread.currentThread().getName(), delta*1e-9f);
        }
    }

    public static void main(String[] args) {
        float[] arr = new float[size];
        float[] arr1 = new float[size];
        fillArray(arr);
        fillArray(arr1);
        long t = System.nanoTime();
        calcArray(arr);
        long delta = (System.nanoTime() - t);
        System.out.printf("Время расчета элементов массива: %f\n", delta*1e-9f);
        try {
            reCalcArray(arr1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (arr.equals(arr1)){
            System.out.println("is equal" );
        } else {
            System.out.println("not equal");
        }
    }
}
