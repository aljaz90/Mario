package cyou.equinox;

import java.awt.*;

public class UIManager {

    private final int WIDTH, HEIGHT;
    private final Insets INSETS;

    private EUIManagerScene scene;

    public UIManager(int WIDTH, int HEIGHT, Insets INSETS) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.INSETS = INSETS;

        scene = EUIManagerScene.MAIN_MENU;
    }

    public void render(Graphics2D g2d) {

    }

    public void show(EUIManagerScene scene) {
        this.scene = scene;
    }
    public void hide() {
        scene = null;
    }
}
