package ru.geekbrains.java3.hw6;

import java.util.Arrays;

public class HomeWork6 {
/*
2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
 Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
  идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку,
  иначе в методе необходимо выбросить RuntimeException. Написать набор тестов для этого метода
  (по 3-4 варианта входных данных).
  Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].

3. Написать метод, который проверяет состав массива из чисел 1 и 4.
Если в нем нет хоть одной четверки или единицы, то метод вернет false;
 Написать набор тестов для этого метода (по 3-4 варианта входных данных).
*/

    public static void main(String[] args) {
        int[] inArr = {1, 2, 4, 4, 2, 3, 1, 7, 6, 7, 8, 9};

        System.out.println(checkOneFour(inArr));
        System.out.println(Arrays.toString(getInts(inArr)));

    }

    private static boolean checkOneFour(int[] inArr) {
        boolean f1 = false;
        boolean f4 = false;
        for (int i : inArr) {
            if (i == 1) f1 = true;
            if (i == 4) f4 = true;
        }
        return f1 && f4;
    }

    private static int[] getInts(int[] inArr) {
        int count = 0;
        for (int i = inArr.length - 1; i > -1; i--) {
            if (inArr[i] != 4) {
                count++;
            } else break;
        }
        int[] outArr = new int[count];
        if (count != 0) {
            System.arraycopy(inArr, (inArr.length - count), outArr, 0, count);
        }
        return outArr;
    }

}
