package ru.geekbrains.lesson1;

import javax.swing.*;
import java.awt.*;

public class MainCircles extends JFrame {
    private static final int POS_X = 400;
    private static final int POS_Y = 200;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    Ball[] balls = new Ball[10];

    private void update(GameCanvas c,float deltaTime){
        for (int i = 0; i < balls.length; i++) {
            balls[i].update(c,deltaTime);
        }
    }
    private void render(GameCanvas c,Graphics g){
        for (int i = 0; i < balls.length; i++) {
            balls[i].render(c,g);
        }
    }

    void onDrawCanvas(GameCanvas c, Graphics g, float deltaTime){
        Background.changeColor(c);
        update(c,deltaTime);
        render(c,g);
    }

    private void initApplication(){
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Ball();
        }
    }

    private MainCircles() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Circles");

        GameCanvas canvas = new GameCanvas(this);
        add(canvas);
        initApplication();

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainCircles();
    }
}
