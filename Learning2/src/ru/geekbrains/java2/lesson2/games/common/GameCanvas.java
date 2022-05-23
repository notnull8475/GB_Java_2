package ru.geekbrains.lesson2.games.common;

import ru.geekbrains.lesson2.games.circles.MainCircles;

import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JPanel {
    private long lastFrameTime;
    private ControllerInterface controller;

    public GameCanvas(ControllerInterface controller) {
        lastFrameTime = System.nanoTime();
        this.controller = controller;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long currentTime = System.nanoTime();
        float deltaTIme = (currentTime-lastFrameTime)*0.000000001f;
        lastFrameTime= currentTime;
        controller.onDrawCanvas(this,g,deltaTIme);
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    public int getLeft() {
        return 0;
    }

    public int getRight() {
        return getWidth() - 1;
    }

    public int getTop() {
        return 0;
    }

    public int getBottom() {
        return getHeight() - 1;
    }

}
