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
                () -> assertFalse(hw.checkOneFour(new int[]{1,22,222,444}))
        );
    }

    @Test
    void getIntsTest() {
        assertAll(
                () -> assertArrayEquals(new int[]{1, 2, 3}, hw.getInts(new int[]{1, 1, 1, 1, 4, 1, 2, 3})),
                () -> assertArrayEquals(new int[]{},hw.getInts(new int[]{4})),
                () -> assertArrayEquals(new int[]{},hw.getInts(new int[]{0,0,0,0,4})),
                () -> assertArrayEquals(new int[]{0,0,0,0},hw.getInts(new int[]{4,0,0,0,0})),
                () -> assertArrayEquals(new int[]{},hw.getInts(new int[]{}))

        );
    }
}