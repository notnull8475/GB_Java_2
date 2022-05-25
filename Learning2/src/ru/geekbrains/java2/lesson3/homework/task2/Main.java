package ru.geekbrains.java2.lesson3.homework.task2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String template = "Иванов 89064106293\n" +
            "Смирнов 84996775624\n" +
            "Кузнецов 84996720056 89048611998\n" +
            "Попов 89064559359\n" +
            "Васильев 89052026204\n" +
            "Петров 89062297167\n" +
            "Соколов 89052104173\n" +
            "Михайлов 89585380329\n" +
            "Козлов 89033788272\n" +
            "Новиков 89050609891\n" +
            "Федоров 89697657977\n" +
            "Морозов 89061689884\n" +
            "Козлов 89061665057\n" +
            "Волков 89033755608 89048611998\n" +
            "Алексеев 89061663935\n" +
            "Лебедев 89585779300\n" +
            "Семенов 89062604822\n" +
            "Егоров 89014992059\n" +
            "Павлов 89093869743\n" +
            "Иванов 89697659060 +79120995397\n" +
            "Козлов 89014990856\n" +
            "Степанов 89062604591\n" +
            "Николаев 89033769156\n" +
            "Орлов 89062406784\n" +
            "Андреев +37037214033\n" +
            "Зайцев +37031525309\n" +
            "Макаров +37046267662\n" +
            "Никитин +4578737328\n" +
            "Захаров +79033788680\n" +
            "Зайцев +79061797042\n" +
            "Соловьев +79118538201\n" +
            "Иванов +79643903689\n" +
            "Борисов +79650878527\n" +
            "Яковлев +79120990745\n" +
            "Григорьев +79643911310";

    public static String[][] stringToArray(String temp){
        String[] line = temp.split("\n");
        String[][] resp = new String[line.length][];
        for (int i = 0; i < line.length; i++) {
            resp[i] = line[i].split(" ");
        }
        return resp;
    }

    public static void main(String[] args) {
        PhoneList list = new PhoneList();
        list.add(stringToArray(template));
        List<String> phones = list.get("Иванов");
        for (String s:phones) {
            System.out.println(s);
        }

        PhoneListNew phoneListNew  = new PhoneListNew();
        phoneListNew.add(stringToArray(template));
        ArrayList<String> res = phoneListNew.getPhone("Иванов");
        System.out.println("Иванов" + res);
    }




}
