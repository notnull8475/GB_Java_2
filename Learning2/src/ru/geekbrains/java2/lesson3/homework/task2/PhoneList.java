package ru.geekbrains.lesson3.homework.task2;

import java.util.*;

public class PhoneList {

    private final Map<String, List<String>> phoneMap = new HashMap<>();

    private void putToPhoneMap(String surname, List<String> phones) {

        if (phoneMap.containsKey(surname)) {
            List<String> keyPhones = phoneMap.get(surname);
            phones.addAll(keyPhones);
        }
        phoneMap.put(surname, phones);
    }

    public void add(String surname, List<Object> phones) {
        List<String> temp = new ArrayList<>();

        for (Object o : phones) {
            if (o instanceof Integer) {
                int a = (int) o;
                temp.add(String.valueOf(a));
            }
            if (o instanceof String) {
                String a = (String) o;
                temp.add(a);
            }
        }
        putToPhoneMap(surname, temp);
    }

    public void add(String surname, String[] phones) {
        putToPhoneMap(surname, Arrays.asList(phones));
    }

    public void add(String surname, int[] phones) {
        List<String> temp = new ArrayList<>();
        for (int a : phones) {
            temp.add(String.valueOf(a));
        }
        putToPhoneMap(surname, temp);
    }

    public void add(String surname, Object phone) {
        List<String> temp = new ArrayList<>();
        if (phone instanceof String) {
            temp.add(
                    (String) phone
            );
        } else if (phone instanceof Integer) {
            temp.add(
                    String.valueOf(phone)
            );
        }

        putToPhoneMap(surname, temp);
    }

    /*
     * Предполагается что во входящем массиве нулевым элементом будет фамилия либо иное сопоставление ключа
     * */
    public void add(String[][] phones) {
        for (String[] line : phones) {
            List<String> temp = new ArrayList<>(Arrays.asList(line).subList(1, line.length));
            putToPhoneMap(line[0], temp);
        }
    }

    public List<String> get(String key) {
        return phoneMap.get(key);
    }
}
