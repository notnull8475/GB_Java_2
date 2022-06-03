package ru.geekbrains.java2.lesson2.games.common;

import java.awt.*;

public interface ControllerInterface {
    void onDrawCanvas(GameCanvas c, Graphics g, float deltaTime);
}
