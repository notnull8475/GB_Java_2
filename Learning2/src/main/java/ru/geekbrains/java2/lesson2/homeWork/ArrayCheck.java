package ru.geekbrains.java2.lesson2.homeWork;


import java.util.Random;
import java.util.Scanner;

public class ArrayCheck {
    static boolean withError = false;


    public static void fillArrayByInt(String[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (withError) {
                    if (i == 2 && j == 3) {
                        a[i][j] = "h";
                        continue;
                    }
                }
                a[i][j] = String.valueOf(new Random().nextInt(10));
            }
        }
    }

    public static void arrayIn(String[][] arr) throws MyArraySizeException, MyArrayDataException {
        int sum = 0;
        if (arr.length != 4) {
            throw new MyArraySizeException("Массив не соответствует параметрам 4х4");
        }

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length != 4) {
                throw new MyArraySizeException("Массив не соответствует параметрам 4х4");
            }

            for (int j = 0; j < arr[i].length; j++) {
                try {
                    sum += Integer.parseInt(arr[i][j]);
                } catch (RuntimeException e) {
                    throw new MyArrayDataException("Исключение произошло в ячейке " + i + "x" + j);
                }
            }
        }
        System.out.println("сумма элементов массива " + sum);
    }

    public static void main(String[] args) {

        String[][] arr = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите вариант задачи:\n" +
                "1. без ошибок \n" +
                "2. исключение MyArraySizeException\n" +
                "3. исключение MyArrayDataException\n");
        int choise = scanner.nextInt();

        switch (choise) {
            case 1:
                arr = new String[4][4];
                fillArrayByInt(arr);
                break;
            case 2:
                arr = new String[5][4];
                fillArrayByInt(arr);
                break;
            case 3:
                arr = new String[4][4];
                withError = true;
                fillArrayByInt(arr);
        }

        try {
            arrayIn(arr);
        } catch (MyArrayDataException | MyArraySizeException e) {
            e.printStackTrace();
        }
    }
}

class MyArraySizeException extends Exception {

    public MyArraySizeException(String s) {
        super(s);
    }
}

class MyArrayDataException extends Exception {

    public MyArrayDataException(String s) {
        super(s);
    }
}