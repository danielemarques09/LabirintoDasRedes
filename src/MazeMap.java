public class MazeMap {
    public static final int ROWS = 13;
    public static final int COLS = 25;
    private final int[][] roomAt = new int[ROWS][COLS];

    public MazeMap() {
        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++)
                roomAt[y][x] = -1;

        // Sala inicial
        fill(1, 1, 3, 3, 0);

        // Corredor principal e ligação com o desvio
        fill(4, 2, 7, 2, 1);
        roomAt[3][6] = 4;
        roomAt[4][6] = 4;

        // Sala do Switch
        fill(8, 1, 11, 3, 2);

        // Firewall
        fill(9, 4, 11, 6, 3);

        // Sala de manutenção / roteador defeituoso (beco sem saída)
        fill(5, 5, 7, 7, 4);

        // Servidores
        fill(12, 5, 16, 7, 5);

        // Gateway e corredor de ligação
        roomAt[6][17] = 5;
        fill(18, 5, 21, 7, 6);

        // Paredes internas: mantêm passagens conectadas entre as áreas.
        roomAt[1][1] = -1;
        roomAt[3][3] = -1;
        roomAt[1][9] = -1;
        roomAt[5][9] = -1;
        roomAt[5][5] = -1;
        roomAt[5][7] = -1;
        roomAt[7][5] = -1;
        roomAt[7][7] = -1;
        roomAt[5][13] = -1;
        roomAt[7][15] = -1;
        roomAt[5][19] = -1;
    }

    private void fill(int x1, int y1, int x2, int y2, int roomIndex) {
        for (int y = y1; y <= y2; y++)
            for (int x = x1; x <= x2; x++)
                roomAt[y][x] = roomIndex;
    }

    public boolean isWalkable(int x, int y) {
        return x >= 0 && x < COLS && y >= 0 && y < ROWS && roomAt[y][x] >= 0;
    }

    public int getRoomIndex(int x, int y) {
        return isWalkable(x, y) ? roomAt[y][x] : -1;
    }

    public int getRows() { return ROWS; }
    public int getCols() { return COLS; }
}
