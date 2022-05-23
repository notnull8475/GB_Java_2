package ru.geekbrains.lesson3.homework.task1;

public class ArrayTask {
    public static final String words = "Lorem ipsum dolor ipsum ipsum consectetur consectetur consectetur Blanditiis pariatur numquam aliquam consequuntur Blanditiis eos eos eos aspernatur fuga voluptates";

    public static String[] wordsToArray(String w) {
        return w.split(" ");
    }

// массив оригинальных слов

    public static String[] uniqueWords(String[] words) {
        String[] temp = new String[words.length];
        int u = 0;
        for (int i = 0; i < words.length; i++) {
            boolean flag = true;

            for (String s : temp) {
                if (words[i].equals(s)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                temp[i] = words[i];
                u++;
            }
        }
        String[] resp = new String[u];
        u = 0;
        for (String s : temp) {
            if (s != null) {
                resp[u] = s;
                u++;
            }
        }
        return resp;
    }

// подсчет повторений

    public static int[] countOfWords(String[] uniq, String[] words){
        int[] counts = new int[uniq.length];

        for (int i = 0; i < uniq.length; i++) {

            for (String word : words) {

                if (uniq[i].equals(word)) {
                    counts[i]++;
                }

            }

        }
        return counts;
    }

    public static void main(String[] args) {
        String[] uniq = uniqueWords(wordsToArray(words));
        int[] counts = countOfWords(uniq,wordsToArray(words));

        for (int i = 0; i < uniq.length; i++) {
            System.out.println(i + " " + uniq[i] + " " + counts[i]);
        }

    }

}
