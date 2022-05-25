package ru.geekbrains.java2.lesson1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainCircles extends JFrame {
    private static final int POS_X = 400;
    private static final int POS_Y = 200;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    Ball[] balls = new Ball[10];
    private Sprite[] sprites = new Sprite[1];
    private int sprintCount;

    private void update(GameCanvas c, float deltaTime) {
        for (int i = 0; i < sprintCount; i++) {
            sprites[i].update(c, deltaTime);
        }
    }

    private void render(GameCanvas c, Graphics g) {
        for (int i = 0; i < balls.length; i++) {
            sprites[i].render(c, g);
        }
    }

    void onDrawCanvas(GameCanvas c, Graphics g, float deltaTime) {
        update(c, deltaTime);
        render(c, g);
    }

    private void initApplication() {
        addSprite(new Background());
    }

    private void addSprite(Sprite s) {
        if (sprintCount == sprites.length) {
            Sprite[] temp = new Sprite[sprintCount * 2];
            System.arraycopy(sprites, 0, temp, 0, sprites.length);
            sprites = temp;
        }
        sprites[sprintCount++] = s;
    }

    private void removeSprite() {
       if (sprintCount > 1){
           sprintCount --;
       }
    }

    private MainCircles() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Circles");
        GameCanvas canvas = new GameCanvas(this);
        add(canvas);

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    addSprite(new Ball(e.getX(), e.getY()));
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    removeSprite();
                }
            }
        });


        initApplication();

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainCircles();
    }
}
