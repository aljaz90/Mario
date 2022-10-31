package cyou.equinox;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sprite implements Serializable {

    private static List<Sprite> _SPRITES = new ArrayList();
    private static HashMap<String, Image> _IMAGE_BUFFER = new HashMap<String, Image>();

    private String spriteImage;
    protected Vector2 position;
    protected Vector2 size;

    private ECollisionType collisionType;
    private EVisibility visibility;

    private double opacity = 1.0;

    // Constructors
    public Sprite(String image, Vector2 position, Vector2 size) {
        _SPRITES.add(this);
        this.position = position;
        this.size = size;

        this.visibility = EVisibility.VISIBLE;

        setSpriteImage(image);
    }
    public Sprite(Vector2 position, Vector2 size) {
        _SPRITES.add(this);
        this.position = position;
        this.size = size;

        this.visibility = EVisibility.VISIBLE;
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
        if (_IMAGE_BUFFER.containsKey(spriteImage)) {
            return _IMAGE_BUFFER.get(spriteImage);
        }

        return null;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getSize() {
        return size;
    }
    public double getOpacity() {
        return opacity;
    }

    // Setters
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }
    public void setVisibility(EVisibility visibility) {
        this.visibility = visibility;
    }
    public void setCollisionType(ECollisionType collisionType) {
        this.collisionType = collisionType;
    }
    public void setSpriteImage(String spriteImage) {
        if (!_IMAGE_BUFFER.containsKey(spriteImage)) {
            Image img = new ImageIcon(spriteImage).getImage();
            _IMAGE_BUFFER.put(spriteImage, img);
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

    public JSONObject toJsonObject() {
        JSONObject objectToReturn = new JSONObject();

        objectToReturn.put("spriteImage", spriteImage);
        objectToReturn.put("opacity", opacity);
        objectToReturn.put("position", position.toJsonObject());
        objectToReturn.put("size", size.toJsonObject());
        objectToReturn.put("collisionType", collisionType.name());
        objectToReturn.put("visibility", visibility.name());

        return objectToReturn;
    }

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

    public static void renderAll(Graphics2D g2d, JFrame frame) {
        _SPRITES.forEach(sprite -> {
            if (sprite.getVisibility() == EVisibility.VISIBLE && sprite.getSpriteImage() != null) {
                sprite.render(g2d, frame);
            }
        });
    }

    public void destroy() {
        _SPRITES.remove(this);
    }
}
