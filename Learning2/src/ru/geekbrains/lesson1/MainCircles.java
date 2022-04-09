package ru.geekbrains.lesson1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Array;

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
        Background.changeColor(c,deltaTime);
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

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton()==1){
                    Ball[] newBalls = new Ball[balls.length+1];
                    System.arraycopy(balls, 0, newBalls, 0, balls.length);
                    newBalls[balls.length] = new Ball();
                    balls = newBalls;
                } else if (e.getButton()==3){
                    Ball[] newBalls = new Ball[balls.length-1];
                    System.arraycopy(balls, 1, newBalls, 0, newBalls.length);
                    balls = newBalls;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        initApplication();

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainCircles();
    }
}
