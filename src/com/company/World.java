package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class World extends JPanel {

    private List<Block> blocks;
    private int WIDTH, HEIGHT;
    private Insets INSETS;

    // Edit mode variables
    private Block placeholderBlock;
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

        this.placeholderBlock = new Block(EBlockType.BRICK, new Vector2(0, 0));
        this.placeholderBlock.setOpacity(0.4);
    }

    private void generateGrid() {
        for (int y = HEIGHT - 16 - INSETS.top; y >= 0; y -= 32) {
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
        closestBlock.setVisibility(EVisibility.VISIBLE);
        closestBlock.setBlockType(placeholderBlock.getBlockType());
    }

    public void nextPlaceholderBlock() {
        EBlockType[] placeholderBlockTypes = { EBlockType.BRICK, EBlockType.BEDROCK, EBlockType.QUESTION, EBlockType.CONCRETE };

        int currentIndex = 0;
        for (EBlockType el : placeholderBlockTypes) {
            if (el == placeholderBlock.getBlockType()) {
                break;
            }
            currentIndex++;
        }

        currentIndex++;
        if (currentIndex == placeholderBlockTypes.length) {
            currentIndex = 0;
        }

        placeholderBlock.setBlockType(placeholderBlockTypes[currentIndex]);
    }

    public void previousPlaceholderBlock() {
        EBlockType[] placeholderBlockTypes = { EBlockType.BRICK, EBlockType.BEDROCK, EBlockType.QUESTION, EBlockType.CONCRETE };

        int currentIndex = 0;
        for (EBlockType el : placeholderBlockTypes) {
            if (el == placeholderBlock.getBlockType()) {
                break;
            }
            currentIndex++;
        }

        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = placeholderBlockTypes.length - 1;
        }

        placeholderBlock.setBlockType(placeholderBlockTypes[currentIndex]);
    }

    public void updatePlaceholderBlock() {
        this.placeholderBlock.setPosition(this.closestBlock.getPosition());
    }

    public void drawPlaceholderBlock(Graphics g, JFrame frame) {
        placeholderBlock.render(g, frame);
    }
}
