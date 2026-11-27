
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MazeCanvas extends JPanel {

    private static final int TILE = 36;
    private static final int ROWS = 15;
    private static final int COLS = 23;

    private final int[][] maze = new int[ROWS][COLS];

    private final Game game;

    private int playerX = 2;
    private int playerY = 2;

    public MazeCanvas(Game game) {

        this.game = game;

        createMaze();

        setPreferredSize(
                new Dimension(COLS * TILE, ROWS * TILE)
        );

        setBackground(new Color(20, 25, 35));
        setOpaque(true);
    }

    private void createMaze() {

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                maze[y][x] = 1;
            }
        }

        // Sala inicial
        carveArea(1, 1, 3, 3);

        // Corredor principal
        carveArea(4, 1, 7, 3);

        // Sala do Switch
        carveArea(8, 1, 11, 3);

        // Sala do Firewall
        carveArea(9, 4, 11, 7);

        // Sala dos servidores
        carveArea(12, 5, 17, 7);

        // Gateway
        carveArea(18, 5, 21, 7);

        // Pequenas paredes internas para formar o labirinto
        maze[1][2] = 0;
        maze[3][2] = 0;

        maze[2][5] = 1;
        maze[1][6] = 1;
        maze[3][6] = 1;

        maze[2][9] = 1;

        maze[5][10] = 1;
        maze[6][10] = 0;

        maze[6][14] = 1;
        maze[5][16] = 1;

        maze[6][19] = 1;
    }

    private void carveArea(
            int startX,
            int startY,
            int endX,
            int endY
    ) {

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                maze[y][x] = 0;
            }
        }
    }

    private int getRoomAt(int x, int y) {

        if (x >= 1 && x <= 3 && y >= 1 && y <= 3) {
            return 0;
        }

        if (x >= 4 && x <= 7 && y >= 1 && y <= 3) {
            return 1;
        }

        if (x >= 8 && x <= 11 && y >= 1 && y <= 3) {
            return 2;
        }

        if (x >= 9 && x <= 11 && y >= 4 && y <= 7) {
            return 3;
        }

        if (x >= 12 && x <= 17 && y >= 5 && y <= 7) {
            return 4;
        }

        if (x >= 18 && x <= 21 && y >= 5 && y <= 7) {
            return 5;
        }

        return -1;
    }

    public void movePlayer(int dx, int dy) {

        if (game.isFinished()) {
            return;
        }

        int newX = playerX + dx;
        int newY = playerY + dy;

        if (newX < 0 || newX >= COLS ||
                newY < 0 || newY >= ROWS) {
            return;
        }

        if (maze[newY][newX] == 1) {
            return;
        }

        int currentRoom = game.getCurrentRoomIndex();
        int destinationRoom = getRoomAt(newX, newY);

        if (destinationRoom == -1) {
            return;
        }

        if (destinationRoom != currentRoom) {

            boolean entered = game.enterMazeRoom(destinationRoom);

            if (!entered) {
                repaint();
                return;
            }

        } else {

            game.countMovement();

            if (game.isFinished()) {
                repaint();
                return;
            }
        }

        playerX = newX;
        playerY = newY;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        for (int y = 0; y < ROWS; y++) {

            for (int x = 0; x < COLS; x++) {

                int px = x * TILE;
                int py = y * TILE;

                if (maze[y][x] == 1) {

                    g.setColor(new Color(30, 50, 80));
                    g.fillRect(px, py, TILE, TILE);

                    g.setColor(new Color(65, 90, 120));
                    g.drawRect(px, py, TILE, TILE);

                } else {

                    g.setColor(new Color(205, 215, 220));
                    g.fillRect(px, py, TILE, TILE);

                    g.setColor(new Color(175, 190, 200));
                    g.drawRect(px, py, TILE, TILE);
                }
            }
        }

        drawRoomName(g, "INÍCIO", 1, 1);
        drawRoomName(g, "CORREDOR", 4, 1);
        drawRoomName(g, "SWITCH", 8, 1);
        drawRoomName(g, "FIREWALL", 9, 4);
        drawRoomName(g, "SERVIDORES", 12, 5);
        drawRoomName(g, "GATEWAY", 18, 5);

        // Personagem
        g.setColor(new Color(30, 100, 230));

        g.fillOval(
                playerX * TILE + 5,
                playerY * TILE + 5,
                TILE - 10,
                TILE - 10
        );

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 15));

        g.drawString(
                "P",
                playerX * TILE + 13,
                playerY * TILE + 23
        );
    }

    private void drawRoomName(
            Graphics2D g,
            String name,
            int x,
            int y
    ) {

        g.setColor(new Color(20, 30, 45));
        g.setFont(new Font("Arial", Font.BOLD, 9));

        g.drawString(
                name,
                x * TILE + 3,
                y * TILE + 12
        );
    }
}
