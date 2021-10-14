package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Sprite {

    public static List<Sprite> _SPRITES = new ArrayList();
    private Image spriteImage;
    private Vector2 position;
    private Vector2 size;
    private ECollisionType collisionType;

    public Sprite(Image image, Vector2 position, Vector2 size) {
        _SPRITES.add(this);
        this.spriteImage = image;
        this.position = position;
        this.size = size;
    }
    public Sprite(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }
    public Sprite() {}

    public ECollisionType getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(ECollisionType collisionType) {
        this.collisionType = collisionType;
    }

    public Image getSpriteImage() {
        return spriteImage;
    }

    public void setSpriteImage(Image spriteImage) {
        if (spriteImage == null && this.spriteImage != null) {
            _SPRITES.remove(this);
        }
        else if (this.spriteImage == null) {
            _SPRITES.add(this);
        }

        this.spriteImage = spriteImage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void render(Graphics g, JFrame frame) {
        Graphics2D g2d = (Graphics2D) g;
        Insets insets = frame.getInsets();
        g2d.drawImage(getSpriteImage(), position.getX() - size.getX() / 2, position.getY() + insets.top - size.getY() / 2, size.getX(), size.getY(), frame);
    }

    public void destroy() {
        _SPRITES.remove(this);
    }
}
