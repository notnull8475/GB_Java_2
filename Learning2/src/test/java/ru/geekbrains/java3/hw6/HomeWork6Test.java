package ru.geekbrains.java3.hw6;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeWork6Test {
    private static HomeWork6 hw;

    @BeforeAll
    static void init() {
        hw = new HomeWork6();
    }

    @Test
    void checkOneFourTest() {
        assertAll(
                () -> assertTrue(hw.checkOneFour(new int[]{1, 4})),
                () -> assertFalse(hw.checkOneFour(new int[]{1, 1})),
                () -> assertFalse(hw.checkOneFour(new int[]{4, 4})),
                () -> assertFalse(hw.checkOneFour(new int[]{2, 2}))
        );
    }

    @Test
    void getIntsTest() {
        int[] inArr = {1, 1, 1, 1, 4, 1, 2, 3};
        int[] resArr = {1, 2, 3};
        assertArrayEquals(resArr, hw.getInts(inArr));
    }
}