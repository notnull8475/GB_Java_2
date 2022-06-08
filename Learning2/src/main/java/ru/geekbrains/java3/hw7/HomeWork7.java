package ru.geekbrains.java3.hw7;

import java.lang.reflect.InvocationTargetException;

public class HomeWork7 {
    @BeforeSuite
    public void startBeforeSuite() {
        System.out.println("BeforeSuite");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("AfterSuite");
    }

    @Test(priority = 1)
    public void forTest1() {
        System.out.println("Test - 1");
    }

    @Test(priority = 10)
    public void forTest2() {
        System.out.println("Test - 2");
    }

    @Test
    public void forTest3() {
        System.out.println("Test - 3");
    }

    @Test
    public void forTest4() {
        System.out.println("Test - 4");
    }

    @Test(priority = 3)
    public void forTest5() {
        System.out.println("Test - 5");
    }


    public static void main(String[] args) {
        Tester tester = new Tester();
        HomeWork7 hm7 = new HomeWork7();
        try {
            tester.start(hm7.getClass());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}