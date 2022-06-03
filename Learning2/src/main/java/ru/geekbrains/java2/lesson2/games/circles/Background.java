package ru.geekbrains.java2.lesson2.games.circles;

import ru.geekbrains.java2.lesson2.games.common.CommonObject;
import ru.geekbrains.java2.lesson2.games.common.GameCanvas;

import java.awt.*;

public class Background implements CommonObject {

    float changeSpeed = 0.1f;
    float startHue = 0;
    Color color;

    @Override
    public void update(GameCanvas canvas, float deltaTime) {
        startHue += deltaTime*changeSpeed;
        if (startHue>=1){
            startHue = 0;
        }
        color = new Color(Color.HSBtoRGB(startHue,1,1));
//        canvas.setBackground(color);
    }
    @Override
    public void render(GameCanvas c, Graphics g){
        c.setBackground(color);
    }
}
