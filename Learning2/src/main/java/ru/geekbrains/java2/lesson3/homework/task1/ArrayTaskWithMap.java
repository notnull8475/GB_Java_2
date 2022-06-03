package ru.geekbrains.java2.lesson3.homework.task1;

import java.util.*;

import static ru.geekbrains.java2.lesson3.homework.task1.ArrayTask.words;
import static ru.geekbrains.java2.lesson3.homework.task1.ArrayTask.wordsToArray;

public class ArrayTaskWithMap {


    static Map<String, Integer> listOfWords(String[] words) {
        Map<String, Integer> wm = new HashMap<>();

        for (String s : words) {
            if (!wm.containsKey(s)) {
                wm.put(s, 1);
            } else {
                wm.replace(s, wm.get(s) + 1);
            }
        }
        return wm;
    }

    static void printMapByList(Map<String, Integer> map){
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(i + " "+ entry.getKey() + "  " + entry.getValue());
            i++;
        }
    }

    public static void main(String[] args) {
        printMapByList(listOfWords(wordsToArray(words)));
    }
}
