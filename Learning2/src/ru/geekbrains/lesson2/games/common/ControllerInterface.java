package ru.geekbrains.lesson2.games.common;

import java.awt.*;

public interface ControllerInterface {
    void onDrawCanvas(GameCanvas c, Graphics g, float deltaTime);
}
