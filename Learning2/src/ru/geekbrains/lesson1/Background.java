package ru.geekbrains.lesson1;

import java.awt.*;

public class Background {

    static float changeSpeed = 0.1f;
    static float startHue = 0;
    static Color color;

    static void changeColor(GameCanvas c,float time) {

        startHue += time*changeSpeed;
        if (startHue>=1){
            startHue = 0;
        }
        color = new Color(Color.HSBtoRGB(startHue,1,1));
        c.setBackground(color);
    }
}
