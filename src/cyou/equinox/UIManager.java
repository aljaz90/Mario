package cyou.equinox;

import java.awt.*;

public class UIManager {

    private final int WIDTH, HEIGHT;
    private final Insets INSETS;

    private EUIManagerState uiState;

    public UIManager(int WIDTH, int HEIGHT, Insets INSETS) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.INSETS = INSETS;

        uiState = EUIManagerState.MAIN_MENU;
    }



}
