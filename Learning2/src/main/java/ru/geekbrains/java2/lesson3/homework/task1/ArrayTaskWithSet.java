package ru.geekbrains.java2.lesson3.homework.task1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArrayTaskWithSet {

    public static Set<String> stringSet(String[] array) {
        return new HashSet<>(Arrays.asList(array));
    }

    public static void printWordCount(Set<String> set, String[] wa) {

        for (String s : set) {
            int i = 0;
            for (String s1 : wa) {
                if (s.equals(s1)) {
                    i++;
                }
            }
            System.out.printf("%s : %d\n", s,i);
        }

    }

    public static void main(String[] args) {
        String[] wordsArray = ArrayTask.wordsToArray(ArrayTask.words);

        Set<String> set = stringSet(wordsArray);

        printWordCount(set, wordsArray);

    }

}
