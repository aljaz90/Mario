package com.company;

import javax.swing.*;
import java.awt.*;

public class Block extends Sprite {

    private EBlockType blockType;

    public EBlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(EBlockType blockType) {
        this.blockType = blockType;
        assignSpriteImage(blockType);
    }

    public Block(EBlockType type, Vector2 position) {
        super(position, new Vector2(32, 32));

        this.blockType = type;
        assignSpriteImage(type);
    }

    public Block(Vector2 position) {
        super(position, new Vector2(32, 32));

        this.blockType = EBlockType.PLACEHOLDER;
    }

    private void assignSpriteImage(EBlockType type) {
        String imgPath = "";
        switch (type) {
            case BRICK:
                imgPath = "src/resources/sprites/world/brick.png";
                break;
            case BEDROCK:
                imgPath = "src/resources/sprites/world/bedrock.png";
                break;
            case QUESTION:
                imgPath = "src/resources/sprites/world/question.png";
                break;
            case CONCRETE:
                imgPath = "src/resources/sprites/world/concrete.png";
                break;
        }
        setSpriteImage(new ImageIcon(imgPath).getImage());
    }
}
