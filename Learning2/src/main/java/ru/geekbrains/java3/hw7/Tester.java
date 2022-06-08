package ru.geekbrains.java3.hw7;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    /* 1. Создать класс, который может выполнять «тесты»,
     * в качестве тестов выступают классы с наборами методов с аннотациями @Test.
     * Для этого у него должен быть статический метод start(),
     * которому в качестве параметра передается или объект типа Class, или имя класса.
     * Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite,
     * если такой имеется, далее запущены методы с аннотациями @Test,
     * а по завершению всех тестов – метод с аннотацией @AfterSuite.
     * К каждому тесту необходимо также добавить приоритеты (int числа от 1 до 10),
     * в соответствии с которыми будет выбираться порядок их выполнения,
     * если приоритет одинаковый, то порядок не имеет значения.
     * Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать
     * в единственном экземпляре, иначе необходимо бросить RuntimeException при запуске «тестирования».
     **/

    public void start(Class<?> c) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Constructor<?> constructor = c.getDeclaredConstructor();
        Object o = constructor.newInstance();

        Method before = null;
        Method after = null;

//        Method[] methods = o.getDeclaredMethods();
        Method[] methods = o.getClass().getMethods();
        List<Method> methodList = new ArrayList<>();

        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                int priority = m.getAnnotation(Test.class).priority();
                if (priority < 1 || priority > 10) {
                    throw new RuntimeException("Priority exception");
                }
                methodList.add(m);
            } else if (m.isAnnotationPresent(BeforeSuite.class)) {
                if (before != null) {
                    throw new RuntimeException("BeforeSuite exception");
                }
                before = m;
            } else if (m.isAnnotationPresent(AfterSuite.class)) {
                if (after != null) {
                    throw new RuntimeException("AfterSuite exception!");
                }
                after = m;
            }
        }
        methodList.sort((m1, m2) -> m2.getAnnotation(Test.class).priority() - m1.getAnnotation(Test.class).priority());


        if (before != null) methodList.add(0, before);
        if (after != null) methodList.add(after);
        for (Method method : methodList) {
            method.invoke(o);
        }
    }
}
