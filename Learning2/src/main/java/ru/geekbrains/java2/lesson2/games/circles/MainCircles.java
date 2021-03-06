package ru.geekbrains.java2.lesson2.games.circles;

import ru.geekbrains.java2.lesson2.games.common.CommonObject;
import ru.geekbrains.java2.lesson2.games.common.ControllerInterface;
import ru.geekbrains.java2.lesson2.games.common.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainCircles extends JFrame implements ControllerInterface, MouseListener {
    private static final int POS_X = 400;
    private static final int POS_Y = 200;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private CommonObject[] commonObjects = new CommonObject[1];
    private int objectsCount;

    private void update(GameCanvas c, float deltaTime) {
        for (int i = 0; i < objectsCount; i++) {
            commonObjects[i].update(c, deltaTime);
        }
    }

    private void render(GameCanvas c, Graphics g) {
        for (int i = 0; i < objectsCount; i++) {
            commonObjects[i].render(c, g);
        }
    }

    @Override
    public void onDrawCanvas(GameCanvas c, Graphics g, float deltaTime) {
        update(c, deltaTime);
        render(c, g);
    }

    private void initApplication() {
        addSprite(new Background());
    }

    private void addSprite(CommonObject s) {
        if (objectsCount == commonObjects.length) {
            CommonObject[] temp = new CommonObject[objectsCount * 2];
            System.arraycopy(commonObjects, 0, temp, 0, commonObjects.length);
            commonObjects = temp;
        }
        commonObjects[objectsCount++] = s;
    }

    private void removeSprite() {
        if (objectsCount > 1) {
            objectsCount--;
        }
    }

    private MainCircles() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Circles");
        GameCanvas canvas = new GameCanvas(this);
        add(canvas);

        canvas.addMouseListener(this);


        initApplication();

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainCircles();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            addSprite(new Ball(e.getX(), e.getY()));
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            removeSprite();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
