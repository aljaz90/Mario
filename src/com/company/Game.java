package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame implements Runnable {

    private int WIDTH = 800;
    private int HEIGHT = 500;
    private World world;
    private EGameMode gameMode = EGameMode.NORMAL;
    private Vector2 mousePosition = Vector2.ZERO;

    public Game() {
        setLocationRelativeTo(null);

        setSize(new Dimension(WIDTH, HEIGHT));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mario");
        setResizable(false);
        setVisible(true);

        world = new World(WIDTH, HEIGHT, getInsets());

        setupEventListeners();

        try {
            while(true){
                update();
                Thread.sleep(16);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setupEventListeners() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mousePosition = new Vector2(e.getX(), e.getY() - getInsets().top);

                Block prevClosestBlock = world.getClosestBlock();
                Block closestBlock = world.findClosestBlock(mousePosition);

                if (prevClosestBlock != null && prevClosestBlock != closestBlock) {
                    world.updatePlaceholderBlock();
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                switch (gameMode) {
                    case NORMAL:
                        break;
                    case EDIT:
                        world.placeBlock();
                        break;
                    case PAUSED:
                        break;
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                switch (e.getExtendedKeyCode()) {
                    case KeyEvent.VK_PLUS:
                        toggleEditMode();
                        break;
                    case KeyEvent.VK_E:
                        if (gameMode == EGameMode.EDIT)
                            world.nextPlaceholderBlock();
                        break;
                    case KeyEvent.VK_Q:
                        if (gameMode == EGameMode.EDIT)
                            world.previousPlaceholderBlock();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void toggleEditMode() {
        if (gameMode == EGameMode.NORMAL) {
            gameMode = EGameMode.EDIT;
        }
        else if (gameMode == EGameMode.EDIT) {
            gameMode = EGameMode.NORMAL;
        }
    }

    private void update() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);

        g.clearRect(0, 0, WIDTH, HEIGHT);

        paintBackground(g);

        Sprite._SPRITES.forEach(sprite -> {
            if (sprite.getVisibility() == EVisibility.VISIBLE && sprite.getSpriteImage() != null) {
                sprite.render(g, this);
            }
        });

        paintUI(g);
    }

    private void paintBackground(Graphics g) {
        g.setColor(new Color(102, 149, 245));
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void paintUI(Graphics g) {
        switch (gameMode) {
            case NORMAL:
                break;
            case EDIT:
                world.drawPlaceholderBlock(g, this);
                break;
            case PAUSED:
                break;
        }
    }

    private void drawEditModeUI(Graphics g) {
        Block closestBlock = world.getClosestBlock();

        if (closestBlock != null) {
            g.setColor(Color.RED);
            g.fillRect(
                    closestBlock.getPosition().getX() - closestBlock.getSize().getX() / 2,
                    closestBlock.getPosition().getY() - closestBlock.getSize().getY() / 2,
                    closestBlock.getSize().getX(),
                    closestBlock.getSize().getY()
            );
        }
    }

    @Override
    public void run() {
        new Thread().start();
    }
}
