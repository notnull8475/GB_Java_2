package ru.geekbrains.java2.lesson3.homework.task2;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneListNew {

    public static final HashMap<String, ArrayList<Person>> phoneMapNew = new HashMap<>();

    private void putToPhoneMap(String key, Person person) {
        ArrayList<Person> keyPerson = new ArrayList<>();
        if (phoneMapNew.containsKey(key)) {
            keyPerson.addAll(phoneMapNew.get(key));
        }
        keyPerson.add(person);
        phoneMapNew.put(key, keyPerson);
    }


    public void add(String[][] phones) {
        for (String[] line : phones) {
            for (int i = 1; i < line.length; i++) {
                Person person = new Person(line[0], line[i]);
                putToPhoneMap(line[0], person);
            }
        }
    }


    public ArrayList<String> getPhone(String key) {
        if (!phoneMapNew.containsKey(key)) return null;
        ArrayList<Person> persons = phoneMapNew.get(key);
        ArrayList<String> phones = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            phones.add(persons.get(i).phone);
        }
        return phones;
    }

}
