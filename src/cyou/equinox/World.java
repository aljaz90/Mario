package cyou.equinox;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
//        generateGrid();

//        placeholderBlock = new Block(EBlockType.BRICK, new Vector2(0, 0));
//        placeholderBlock.setOpacity(0.4);
    }

    public World(int WIDTH, int HEIGHT, Insets INSETS) {
        blocks = new ArrayList<>();
        closestBlock = null;

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.INSETS = INSETS;

        initialiseWorld();
    }

    public void loadWorld (String mapName) throws Exception {
        String fileName = String.format("maps/%s.json", mapName);
        File mapFile = new File(fileName);
        if (!mapFile.exists()) {
            throw new Exception("Map file not found");
        }

        String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));;

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray serializedBlocks = jsonObject.getJSONArray("blocks");

        for (int i = 0; i < serializedBlocks.length(); i++) {
            JSONObject serializedBlock = serializedBlocks.getJSONObject(i);
            JSONObject serializedPosition = serializedBlock.getJSONObject("position");
            Vector2 blockPosition = new Vector2(serializedPosition.getInt("x"), serializedPosition.getInt("y"));

            blocks.add(new Block(EBlockType.valueOf(serializedBlock.getString("blockType")), blockPosition));
        }
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

    public Vector2 getClosestGridBlockPosition(Vector2 to) {
        int newX = (to.getX() / 32) * 32 + 16 + INSETS.left;
        int newY = (to.getY() / 32) * 32 + 16;

        return new Vector2(newX, newY);
    }

    public Block getGridBlockAtPosition(Vector2 to) {
        for (Block block : blocks) {
            if (block.getPosition() == to) {
                return block;
            }
        }

        return null;
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
        block.destroy();
    }

    public void placeBlock(EBlockType type, Vector2 position) {
        Block newBlock = new Block(type, position);
        blocks.add(newBlock);
    }

    public void updatePlaceholderBlock() {
        placeholderBlock.setPosition(this.closestBlock.getPosition());
    }

    public void drawPlaceholderBlock(Graphics g, JFrame frame) {
        placeholderBlock.render(g, frame);
    }
}
