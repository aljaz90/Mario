package cyou.equinox;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class World extends JPanel {

    private List<Block> blocks;
    private final int WIDTH, HEIGHT;
    private final Insets INSETS;

    // Edit mode variables
    private Block placeholderBlock;
    private Block closestBlock;

    public Block getClosestBlock() {
        return closestBlock;
    }

    private void initialiseWorld() {
        generateGrid();

        placeholderBlock = new Block(EBlockType.BRICK, new Vector2(0, 0));
        placeholderBlock.setOpacity(0.4);
    }

    public World(int WIDTH, int HEIGHT, Insets INSETS) {
        blocks = new ArrayList<>();
        closestBlock = null;

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.INSETS = INSETS;

        initialiseWorld();
    }

    public void loadMap (String mapName) throws Exception {
        // Load map
        File mapFile = new File(String.format("maps/%s.json", mapName));
        if (!mapFile.exists()) {
            throw new Exception("Map file not found");
        }

        FileInputStream fileInputStream = new FileInputStream(mapFile);
//        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

//        List<Block> newBlocks = (ArrayList<Block>) objectInputStream.readObject();
//        System.out.println("SIZED");
//        System.out.println(newBlocks.size());
//        blocks = newBlocks;
//        Sprite.rebufferImages();
    }
    public void saveWorld (String mapName) throws Exception {
        File mapFile = new File(String.format("maps/%s.json", mapName));
        if (!mapFile.exists()) {
            mapFile.createNewFile();
        }

        // Stringifying blocks
        JSONArray serializedBlocks = new JSONArray();
        for (Block block : blocks) {
            serializedBlocks.put(block.toJsonObject());
        }

        // Create json object and add data
        JSONObject objectToWrite = new JSONObject();
        objectToWrite.put("name", mapName);
        objectToWrite.put("blocks", serializedBlocks);

        // Write json string to file
        FileOutputStream fileOutputStream = new FileOutputStream(mapFile);
        fileOutputStream.write(objectToWrite.toString().getBytes());
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
        EBlockType[] placeholderBlockTypes = { EBlockType.BRICK, EBlockType.BEDROCK, EBlockType.QUESTION, EBlockType.CONCRETE, EBlockType.PLACEHOLDER };

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
        EBlockType[] placeholderBlockTypes = { EBlockType.BRICK, EBlockType.BEDROCK, EBlockType.QUESTION, EBlockType.CONCRETE, EBlockType.PLACEHOLDER };

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
        placeholderBlock.setPosition(this.closestBlock.getPosition());
    }

    public void drawPlaceholderBlock(Graphics g, JFrame frame) {
        placeholderBlock.render(g, frame);
    }
}
