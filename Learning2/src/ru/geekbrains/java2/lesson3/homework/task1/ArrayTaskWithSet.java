package ru.geekbrains.lesson3.homework.task1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ru.geekbrains.lesson3.homework.task1.ArrayTask.words;
import static ru.geekbrains.lesson3.homework.task1.ArrayTask.wordsToArray;

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
        String[] wordsArray = wordsToArray(words);

        Set<String> set = stringSet(wordsArray);

        printWordCount(set, wordsArray);

    }

}
