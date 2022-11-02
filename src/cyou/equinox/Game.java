package cyou.equinox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Game extends JFrame implements Runnable {

    private final int WIDTH;
    private final int HEIGHT;

    // Control classes
    private World world;
    private UIManager uiManager;

    private EGameMode gameMode = EGameMode.NORMAL;
    private Vector2 mousePosition = Vector2.ZERO;

    // Edit mode
    private Block placeholderBlock;

    public Game(int width, int height) {
        WIDTH = width + 16;
        HEIGHT = height + 39;

        setSize(new Dimension(WIDTH, HEIGHT));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - WIDTH / 2, screenSize.height / 2 - HEIGHT / 2);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mario");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        uiManager = new UIManager(WIDTH, HEIGHT, getInsets());
        world = new World(WIDTH, HEIGHT, getInsets());

        setupEventListeners();

        try {
            world.loadWorld("map001");

            while(true){
                tick();
                Thread.sleep(16);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setupEventListeners() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                // Set global mouse position
                mousePosition = new Vector2(e.getX(), e.getY() - getInsets().top);

                // Update position of the placeholder block
                if (gameMode == EGameMode.EDIT) {
                    updatePlaceholderBlock();
                }
            }
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                switch (gameMode) {
                    case NORMAL:
                        break;
                    case EDIT:
                        if (e.getPreciseWheelRotation() < 0)
                            nextPlaceholderBlock();
                        else if (e.getPreciseWheelRotation() > 0)
                            previousPlaceholderBlock();
                        break;
                    case PAUSED:
                        break;
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                switch (gameMode) {
                    case NORMAL:
                        break;
                    case EDIT:
                        handleClickInEdit();
                        break;
                    case PAUSED:
                        break;
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);

                switch (e.getExtendedKeyCode()) {
                    case KeyEvent.VK_PLUS:
                        toggleEditMode();
                        break;
                    case KeyEvent.VK_E:
                        if (gameMode == EGameMode.EDIT)
                            nextPlaceholderBlock();
                        break;
                    case KeyEvent.VK_Q:
                        if (gameMode == EGameMode.EDIT)
                            previousPlaceholderBlock();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void handleClickInEdit() {
        if (placeholderBlock != null) {
            Block clickedBlock = world.getGridBlockAtPosition(placeholderBlock.getPosition());
            if (clickedBlock != null) {
                if (placeholderBlock.getBlockType() == EBlockType.PLACEHOLDER) {
                    world.removeBlock(clickedBlock);
                }
                else {
                    clickedBlock.setBlockType(placeholderBlock.getBlockType());
                }
            }
            else {
                world.placeBlock(placeholderBlock.getBlockType(), placeholderBlock.getPosition());
            }
        }
    }

    private void updatePlaceholderBlock() {
        if (placeholderBlock != null) {
            Vector2 newPosition = world.getClosestGridBlockPosition(mousePosition);
            placeholderBlock.setPosition(newPosition);
        }
    }

    private void toggleEditMode() {
        if (gameMode == EGameMode.NORMAL) {
            placeholderBlock = new Block(EBlockType.BRICK, Vector2.ZERO);
            placeholderBlock.setZIndex(1);
            placeholderBlock.setOpacity(0.6);
            gameMode = EGameMode.EDIT;
        }
        else if (gameMode == EGameMode.EDIT) {
            try {
                placeholderBlock.destroy();
                placeholderBlock = null;
                world.saveWorld("map001");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            gameMode = EGameMode.NORMAL;
        }
    }

    private void tick() {
        update();
        repaint();
    }

    private void update() {
        // Do things
    }

    @Override
    public void paint(Graphics g) {

        // Paint sprites onto buffered image
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Render world
        paintBackground(g2d);
        Sprite.renderAll(g2d, this);

        // Render UI
        uiManager.render(g2d);

        // Render image onto the main canvas
        Graphics2D g2dComponent = (Graphics2D) g;
        g2dComponent.drawImage(bufferedImage, null, 0, 0);
    }

    private void paintBackground(Graphics g) {
        g.setColor(new Color(102, 149, 245));
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void nextPlaceholderBlock() {
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

    private void previousPlaceholderBlock() {
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

    @Override
    public void run() {
        new Thread().start();
    }
}
