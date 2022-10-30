package cyou.equinox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Game extends JFrame implements Runnable {

    private final int WIDTH;
    private final int HEIGHT;
    private World world;
    private EGameMode gameMode = EGameMode.NORMAL;
    private Vector2 mousePosition = Vector2.ZERO;

    public Game(int width, int height) {
        WIDTH = width;
        HEIGHT = height;


        setSize(new Dimension(WIDTH, HEIGHT));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - WIDTH / 2, screenSize.height / 2 - HEIGHT / 2);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mario");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        world = new World(WIDTH, HEIGHT, getInsets());
        try {
            world.loadMap("map001");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupEventListeners();

        try {
            while(true){
                update();
                Thread.sleep(16);
            }
        }
        catch (InterruptedException e) {
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
                            world.nextPlaceholderBlock();
                        else if (e.getPreciseWheelRotation() > 0)
                            world.previousPlaceholderBlock();
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
                        world.placeBlock();
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
                            world.nextPlaceholderBlock();
                        break;
                    case KeyEvent.VK_Q:
                        if (gameMode == EGameMode.EDIT)
                            world.previousPlaceholderBlock();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void updatePlaceholderBlock() {
        Block prevClosestBlock = world.getClosestBlock();
        Block closestBlock = world.findClosestBlock(mousePosition);

        if (prevClosestBlock != null && prevClosestBlock != closestBlock) {
            world.updatePlaceholderBlock();
        }
    }

    private void toggleEditMode() {
        if (gameMode == EGameMode.NORMAL) {
            gameMode = EGameMode.EDIT;
        }
        else if (gameMode == EGameMode.EDIT) {
            try {
                world.saveWorld("map001");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            gameMode = EGameMode.NORMAL;
        }
    }

    private void update() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);

        // Paint sprites onto buffered image
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        paintBackground(g2d);

        Sprite.renderAll(g2d, this);

        paintUI(g2d);

        // Render image onto the main canvas
        Graphics2D g2dComponent = (Graphics2D) g;
        g2dComponent.drawImage(bufferedImage, null, 0, 0);
    }

    private void paintBackground(Graphics g) {
        g.setColor(new Color(102, 149, 245));
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void paintUI(Graphics g) {
        switch (gameMode) {
            case NORMAL:
                break;
            case EDIT:
                world.drawPlaceholderBlock(g, this);
                break;
            case PAUSED:
                break;
        }
    }

    private void drawEditModeUI(Graphics g) {
        Block closestBlock = world.getClosestBlock();

        if (closestBlock != null) {
            g.setColor(Color.RED);
            g.fillRect(
                    closestBlock.getPosition().getX() - closestBlock.getSize().getX() / 2,
                    closestBlock.getPosition().getY() - closestBlock.getSize().getY() / 2,
                    closestBlock.getSize().getX(),
                    closestBlock.getSize().getY()
            );
        }
    }

    @Override
    public void run() {
        new Thread().start();
    }
}
