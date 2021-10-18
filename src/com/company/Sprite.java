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
    private EVisibility visibility;

    private double opacity = 1.0;

    // Constructors
    public Sprite(Image image, Vector2 position, Vector2 size) {
        _SPRITES.add(this);
        this.spriteImage = image;
        this.position = position;
        this.size = size;

        this.visibility = EVisibility.VISIBLE;

    }
    public Sprite(Vector2 position, Vector2 size) {
        _SPRITES.add(this);
        this.position = position;
        this.size = size;

        this.visibility = EVisibility.INVISIBLE;
    }

    public Sprite() {
        _SPRITES.add(this);

        this.visibility = EVisibility.INVISIBLE;
    }

    // Getters
    public EVisibility getVisibility() {
        return visibility;
    }
    public ECollisionType getCollisionType() {
        return collisionType;
    }
    public Image getSpriteImage() {
        return spriteImage;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getSize() {
        return size;
    }

    // Setters
    public void setVisibility(EVisibility visibility) {
        this.visibility = visibility;
    }
    public void setCollisionType(ECollisionType collisionType) {
        this.collisionType = collisionType;
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
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public void setSize(Vector2 size) {
        this.size = size;
    }

    // Methods

    public void render(Graphics g, JFrame frame) {
        Graphics2D g2d = (Graphics2D) g;
        Insets insets = frame.getInsets();

        if (opacity != 1) {
            Composite prevComposite = g2d.getComposite();
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity);

            g2d.setComposite(ac);
            g2d.drawImage(getSpriteImage(), position.getX() - size.getX() / 2, position.getY() + insets.top - size.getY() / 2, size.getX(), size.getY(), frame);
            g2d.setComposite(prevComposite);
        }
        else {
            g2d.drawImage(getSpriteImage(), position.getX() - size.getX() / 2, position.getY() + insets.top - size.getY() / 2, size.getX(), size.getY(), frame);
        }
    }

    public void destroy() {
        _SPRITES.remove(this);
    }
}
