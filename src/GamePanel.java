import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {
    private final Game game;
    private final MazeCanvas mazeCanvas;
    private JLabel metricsLabel;
    private JLabel roomLabel;
    private JLabel statusLabel;
    private JTextField commandField;
    private Timer timer;
    private boolean resultShown;

    public GamePanel(Game game) {
        this.game = game;
        this.resultShown = false;
        setLayout(new BorderLayout(8, 8));
        setBackground(new Color(20, 26, 38));
        mazeCanvas = new MazeCanvas(game);

        createHeader();
        createMap();
        createControls();
        createFooter();
        configureKeyboard();

        timer = new Timer(500, e -> refresh());
        timer.start();
        refresh();
    }

    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 34, 63));
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("LABIRINTO DAS REDES");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        metricsLabel = new JLabel();
        metricsLabel.setForeground(Color.WHITE);
        metricsLabel.setFont(new Font("Arial", Font.BOLD, 13));

        header.add(title, BorderLayout.WEST);
        header.add(metricsLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void createMap() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(20, 26, 38));
        center.add(mazeCanvas);
        add(center, BorderLayout.CENTER);
    }

    private void createControls() {
        JPanel controls = new JPanel(new GridLayout(7, 1, 7, 7));
        controls.setPreferredSize(new Dimension(175, 0));
        controls.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        addButton(controls, "Inventário", this::showInventory);
        addButton(controls, "Pegar item", this::takeItem);
        addButton(controls, "Soltar item", this::dropItem);
        addButton(controls, "Usar Ping", () -> execute("usar ping"));
        addButton(controls, "Resolver desafio", this::solveChallenge);
        addButton(controls, "Olhar sala", this::showRoom);
        addButton(controls, "Ajuda", this::showHelp);

        add(controls, BorderLayout.EAST);
    }

    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private void createFooter() {
        JPanel footer = new JPanel(new BorderLayout(6, 6));
        footer.setBackground(new Color(13, 34, 63));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel info = new JPanel(new GridLayout(2, 1, 2, 2));
        info.setBackground(new Color(13, 34, 63));
        roomLabel = new JLabel();
        roomLabel.setForeground(Color.WHITE);
        statusLabel = new JLabel();
        statusLabel.setForeground(Color.WHITE);
        info.add(roomLabel);
        info.add(statusLabel);

        JPanel command = new JPanel(new BorderLayout(8, 0));
        command.setBackground(new Color(13, 34, 63));
        JLabel label = new JLabel("Comando:");
        label.setForeground(Color.WHITE);
        commandField = new JTextField();
        commandField.addActionListener(e -> {
            execute(commandField.getText());
            commandField.setText("");
        });
        command.add(label, BorderLayout.WEST);
        command.add(commandField, BorderLayout.CENTER);

        footer.add(info, BorderLayout.NORTH);
        footer.add(command, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);
    }

    private void configureKeyboard() {
        bind("UP", 0, -1); bind("W", 0, -1);
        bind("DOWN", 0, 1); bind("S", 0, 1);
        bind("LEFT", -1, 0); bind("A", -1, 0);
        bind("RIGHT", 1, 0); bind("D", 1, 0);
    }

    private void bind(String key, int dx, int dy) {
        String name = "move_" + key;
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), name);
        getActionMap().put(name, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (!game.isFinished()) {
                    mazeCanvas.movePlayer(dx, dy);
                    refresh();
                }
            }
        });
    }

    private void execute(String command) {
        game.executeCommand(command);
        refresh();
    }

    private void showInventory() {
        JOptionPane.showMessageDialog(this, game.getPlayer().getInventoryString(),
                "Inventário", JOptionPane.INFORMATION_MESSAGE);
    }

    private void takeItem() {
        String available = game.getPlayer().getCurrentRoom().getItemString();
        String item = JOptionPane.showInputDialog(this,
                "Itens nesta sala: " + available + "\nDigite o nome do item:");
        if (item != null && !item.isBlank()) execute("pegar " + item.trim());
    }

    private void dropItem() {
        String item = JOptionPane.showInputDialog(this, "Qual item deseja soltar?");
        if (item != null && !item.isBlank()) execute("solta " + item.trim());
    }

    private void solveChallenge() {
        String answer = JOptionPane.showInputDialog(this,
                "Desafio: qual é o endereço IPv4 do Gateway?\nConsulte o log com 'usar log'.");
        if (answer != null) {
            game.solveChallenge(answer);
            refresh();
        }
    }

    private void showRoom() {
        JOptionPane.showMessageDialog(this,
                game.getPlayer().getCurrentRoom().getLongDescription(),
                "Sala atual", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                "OBJETIVO\nEncontre os itens e alcance o Gateway antes do ransomware.\n\n" +
                        "MOVIMENTO\nSetas ou W/A/S/D.\n\n" +
                        "COMANDOS\nir norte | ir sul | ir leste | ir oeste\n" +
                        "pegar ping | pegar token | pegar log | pegar chave\n" +
                        "solta nome | inventario | usar ping | usar log\n" +
                        "usar 192.168.1.1 | olhar | ajuda | sair\n\n" +
                        "DICA\nO log está no beco sem saída. O Firewall exige token e o Gateway exige chave.",
                "Ajuda", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refresh() {
        metricsLabel.setText("Saltos: " + game.getHops() + "/" + game.getMaxHops()
                + "   |   Tempo: " + game.getRemainingSeconds() + "s"
                + "   |   MTU: " + game.getPlayer().getCurrentWeight()
                + "/" + game.getPlayer().getMtu() + " bytes");

        roomLabel.setText("Local: " + game.getPlayer().getCurrentRoom().getName());
        statusLabel.setText("Mensagem: " + game.getStatus());
        mazeCanvas.repaint();

        if (game.isFinished() && !resultShown) {
            resultShown = true;
            if (timer != null) timer.stop();
            JOptionPane.showMessageDialog(this, game.getStatus(),
                    game.isVictory() ? "Vitória!" : "Fim de jogo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
