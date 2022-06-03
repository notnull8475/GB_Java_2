package ru.geekbrains.java2.lesson1;

import java.awt.*;

public class Background extends Sprite {

    float changeSpeed = 0.1f;
    float startHue = 0;
    Color color;

    @Override
    void update(GameCanvas canvas, float deltaTime) {
        startHue += deltaTime*changeSpeed;
        if (startHue>=1){
            startHue = 0;
        }
        color = new Color(Color.HSBtoRGB(startHue,1,1));
//        canvas.setBackground(color);
    }
    @Override
    void render(GameCanvas c, Graphics g){
        c.setBackground(color);
    }
}
