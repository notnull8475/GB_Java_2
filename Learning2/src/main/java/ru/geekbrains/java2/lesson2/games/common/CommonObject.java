package ru.geekbrains.java2.lesson2.games.common;

import java.awt.*;

public interface CommonObject {
    default void update(GameCanvas canvas, float deltaTime) {}
    default void render(GameCanvas canvas, Graphics g) {}
}
