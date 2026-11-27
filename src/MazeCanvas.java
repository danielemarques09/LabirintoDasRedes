import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MazeCanvas extends JPanel {
    private static final int TILE = 32;
    private final Game game;

    public MazeCanvas(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(
                game.getMazeMap().getCols() * TILE,
                game.getMazeMap().getRows() * TILE
        ));
        setBackground(new Color(18, 24, 36));
        setOpaque(true);
    }

    public void movePlayer(int dx, int dy) {
        game.movePlayer(dx, dy);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        MazeMap map = game.getMazeMap();

        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getCols(); x++) {
                int px = x * TILE;
                int py = y * TILE;
                int room = map.getRoomIndex(x, y);

                if (room < 0) {
                    g.setColor(new Color(28, 48, 76));
                    g.fillRect(px, py, TILE, TILE);
                    g.setColor(new Color(54, 79, 111));
                } else {
                    g.setColor(room % 2 == 0
                            ? new Color(205, 218, 225)
                            : new Color(190, 207, 218));
                    g.fillRect(px, py, TILE, TILE);
                    g.setColor(new Color(150, 170, 185));
                }
                g.drawRect(px, py, TILE, TILE);
            }
        }

        label(g, "INICIO", 1, 1);
        label(g, "CORREDOR", 4, 2);
        label(g, "SWITCH", 8, 1);
        label(g, "FIREWALL", 9, 4);
        label(g, "ROTEADOR", 5, 5);
        label(g, "SERVIDORES", 12, 5);
        label(g, "GATEWAY", 18, 5);

        int px = game.getPlayerX() * TILE;
        int py = game.getPlayerY() * TILE;
        g.setColor(new Color(35, 105, 230));
        g.fillOval(px + 5, py + 5, TILE - 10, TILE - 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("P", px + 12, py + 21);
    }

    private void label(Graphics2D g, String text, int x, int y) {
        g.setColor(new Color(25, 38, 55));
        g.setFont(new Font("Arial", Font.BOLD, 9));
        g.drawString(text, x * TILE + 2, y * TILE + 11);
    }
}
