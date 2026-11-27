
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {

    private final Game game;

    private MazeCanvas mazeCanvas;

    private JLabel infoLabel;
    private JLabel statusLabel;
    private JLabel roomLabel;

    private Timer timer;

    private boolean resultShown;

    public GamePanel(Game game) {

        this.game = game;
        resultShown = false;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(25, 30, 40));

        createHeader();
        createMap();
        createControls();
        createFooter();

        configureKeyboard();

        timer = new Timer(500, e -> updateScreen());
        timer.start();

        updateScreen();
    }

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 35, 65));

        header.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel title = new JLabel("LABIRINTO DAS REDES");

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 23));

        infoLabel = new JLabel();

        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        header.add(title, BorderLayout.WEST);
        header.add(infoLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void createMap() {

        mazeCanvas = new MazeCanvas(game);

        JPanel mapContainer = new JPanel(new GridBagLayout());

        mapContainer.setBackground(new Color(25, 30, 40));

        mapContainer.add(mazeCanvas);

        add(mapContainer, BorderLayout.CENTER);
    }

    private void createControls() {

        JPanel controls = new JPanel();

        controls.setLayout(
                new GridLayout(7, 1, 8, 8)
        );

        controls.setPreferredSize(new Dimension(185, 0));

        controls.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        JButton inventoryButton = new JButton("Inventário");
        JButton takeButton = new JButton("Pegar item");
        JButton dropButton = new JButton("Soltar item");
        JButton pingButton = new JButton("Usar Ping");
        JButton challengeButton = new JButton("Resolver desafio");
        JButton helpButton = new JButton("Ajuda");
        JButton lookButton = new JButton("Olhar sala");

        inventoryButton.addActionListener(e -> showInventory());
        takeButton.addActionListener(e -> takeItem());
        dropButton.addActionListener(e -> dropItem());
        pingButton.addActionListener(e -> usePing());
        challengeButton.addActionListener(e -> solveChallenge());
        helpButton.addActionListener(e -> showHelp());
        lookButton.addActionListener(e -> showRoom());

        controls.add(inventoryButton);
        controls.add(takeButton);
        controls.add(dropButton);
        controls.add(pingButton);
        controls.add(challengeButton);
        controls.add(lookButton);
        controls.add(helpButton);

        add(controls, BorderLayout.EAST);
    }

    private void createFooter() {

        JPanel footer = new JPanel(new BorderLayout(5, 5));

        footer.setBackground(new Color(15, 35, 65));

        footer.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        roomLabel = new JLabel();
        roomLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel();
        statusLabel.setForeground(Color.WHITE);

        footer.add(roomLabel, BorderLayout.NORTH);
        footer.add(statusLabel, BorderLayout.SOUTH);

        add(footer, BorderLayout.SOUTH);
    }

    private void configureKeyboard() {

        bindKey("UP", 0, -1);
        bindKey("W", 0, -1);

        bindKey("DOWN", 0, 1);
        bindKey("S", 0, 1);

        bindKey("LEFT", -1, 0);
        bindKey("A", -1, 0);

        bindKey("RIGHT", 1, 0);
        bindKey("D", 1, 0);
    }

    private void bindKey(String key, int dx, int dy) {

        String actionName = "move_" + key;

        getInputMap(WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key), actionName);

        getActionMap().put(actionName, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!game.isFinished()) {
                    mazeCanvas.movePlayer(dx, dy);
                    updateScreen();
                }
            }
        });
    }

    private void showInventory() {

        JOptionPane.showMessageDialog(
                this,
                game.getPlayer().getInventoryString(),
                "Inventário",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void takeItem() {

        String available = game.getPlayer()
                .getCurrentRoom()
                .getItemString();

        String item = JOptionPane.showInputDialog(
                this,
                "Itens disponíveis: " + available +
                        "\n\nDigite o nome do item:"
        );

        if (item != null && !item.trim().isEmpty()) {
            game.takeItem(item.trim().toLowerCase());
        }

        updateScreen();
    }

    private void dropItem() {

        String item = JOptionPane.showInputDialog(
                this,
                "Digite o nome do item que deseja soltar:"
        );

        if (item != null && !item.trim().isEmpty()) {
            game.dropItem(item.trim().toLowerCase());
        }

        updateScreen();
    }

    private void usePing() {

        game.usePing();
        updateScreen();
    }

    private void solveChallenge() {

        NetworkChallenge challenge =
                game.getPlayer().getCurrentRoom().getChallenge();

        if (challenge == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não existe desafio nesta sala."
            );
            return;
        }

        String answer = JOptionPane.showInputDialog(
                this,
                challenge.getQuestion()
        );

        if (answer != null) {
            game.solveChallenge(answer);
        }

        updateScreen();
    }

    private void showHelp() {

        JOptionPane.showMessageDialog(
                this,
                "CONTROLES DO JOGO\n\n" +
                        "Setas ou W, A, S, D: movimentar personagem\n" +
                        "Inventário: consultar itens coletados\n" +
                        "Pegar item: coletar item da sala\n" +
                        "Soltar item: devolver item à sala\n" +
                        "Usar Ping: testar conexão\n" +
                        "Resolver desafio: responder ao desafio da rede\n\n" +
                        "Objetivo: chegar ao Gateway com a chave de segurança.",
                "Ajuda",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showRoom() {

        JOptionPane.showMessageDialog(
                this,
                game.getPlayer().getCurrentRoom().getLongDescription(),
                "Informações da sala",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateScreen() {

        infoLabel.setText(
                "Movimentos: " + game.getCurrentSteps() +
                        "/" + game.getMaxSteps() +
                        " | Tempo: " + game.getRemainingSeconds() + "s" +
                        " | Peso: " + game.getInventoryWeight() + "/50"
        );

        roomLabel.setText(
                "Local atual: " +
                        game.getPlayer().getCurrentRoom().getDescription()
        );

        statusLabel.setText("Mensagem: " + game.getStatus());

        mazeCanvas.repaint();

        if (game.isFinished() && !resultShown) {

            resultShown = true;

            if (timer != null) {
                timer.stop();
            }

            JOptionPane.showMessageDialog(
                    this,
                    game.getStatus(),
                    "Fim de jogo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
