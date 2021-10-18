package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class World extends JPanel {

    private List<Block> blocks;
    private int WIDTH, HEIGHT;
    private Insets INSETS;
    private Block closestBlock;

    public Block getClosestBlock() {
        return closestBlock;
    }

    public World(int WIDTH, int HEIGHT, Insets INSETS) {
        blocks = new ArrayList<>();
        closestBlock = null;

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.INSETS = INSETS;

        generateGrid();
    }

    private void generateGrid() {
        System.out.println(INSETS.top);
        System.out.println(HEIGHT);
        for (int y = HEIGHT - 16 - INSETS.top; y >= INSETS.top + 16; y -= 32) {
            for (int x = 16; x <= WIDTH - 16; x += 32) {
                blocks.add(new Block(new Vector2(x, y)));
            }
        }
    }

    public Block findClosestBlock(Vector2 position) {
        double smallestDistance = Double.MAX_VALUE;
        Block closestBlock = null;

        for (Block block : blocks) {
            double distance = Vector2.distance(block.getPosition(), position);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestBlock = block;
            }
        }

        this.closestBlock = closestBlock;
        return closestBlock;
    }

    public void placeBlock() {

    }
}
