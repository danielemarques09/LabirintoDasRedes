
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame {

    private final int TILE_SIZE = 70;

    // Cores do jogo
    private final Color FUNDO = new Color(10, 15, 30);
    private final Color PAREDE = new Color(35, 55, 100);
    private final Color CHAO = new Color(12, 18, 35);

    // Mapa: 1 = parede | 0 = caminho
    private final int[][] mapa = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,0,0,0,1},
            {1,0,1,0,1,0,1,1,0,1},
            {1,0,1,0,0,0,0,1,0,1},
            {1,0,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,1,0,0,0,1},
            {1,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,0,1,0,1},
            {1,1,1,1,1,1,1,1,1,1}
    };

    // Posição do jogador
    private int jogadorX = 1;
    private int jogadorY = 1;

    // Recursos
    private int bytes = 50;
    private int saltos = 20;

    // Inventário
    private boolean chaveColetada = false;
    private boolean tokenColetado = false;

    // Desafios
    private boolean firewallSuperado = false;
    private boolean portaAberta = false;

    // Controle
    private boolean jogoFinalizado = false;

    // Interface
    private JPanel painelJogo;
    private JLabel status;
    private JLabel inventario;

    public Game() {

        setTitle("Labirinto das Redes");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        criarInterface();
        configurarTeclas();
    }

    // ==============================
    // INTERFACE
    // ==============================

    private void criarInterface() {

        setLayout(new BorderLayout());

        status = new JLabel("", SwingConstants.CENTER);

        status.setFont(new Font("Arial", Font.BOLD, 20));
        status.setForeground(Color.WHITE);
        status.setBackground(FUNDO);
        status.setOpaque(true);
        status.setPreferredSize(new Dimension(850, 55));

        inventario = new JLabel("", SwingConstants.CENTER);

        inventario.setFont(new Font("Arial", Font.BOLD, 16));
        inventario.setForeground(Color.CYAN);
        inventario.setBackground(new Color(20, 30, 55));
        inventario.setOpaque(true);
        inventario.setPreferredSize(new Dimension(850, 55));

        painelJogo = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                desenharMapa(g);
                desenharItens(g);
                desenharJogador(g);
            }
        };

        painelJogo.setPreferredSize(new Dimension(700, 700));
        painelJogo.setBackground(FUNDO);

        add(status, BorderLayout.NORTH);
        add(painelJogo, BorderLayout.CENTER);
        add(inventario, BorderLayout.SOUTH);

        atualizarInterface();
    }

    // ==============================
    // DESENHAR MAPA
    // ==============================

    private void desenharMapa(Graphics g) {

        for (int y = 0; y < mapa.length; y++) {

            for (int x = 0; x < mapa[y].length; x++) {

                if (mapa[y][x] == 1) {
                    g.setColor(PAREDE);
                } else {
                    g.setColor(CHAO);
                }

                g.fillRect(
                        x * TILE_SIZE,
                        y * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );

                g.setColor(new Color(45, 65, 90));

                g.drawRect(
                        x * TILE_SIZE,
                        y * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );
            }
        }
    }

    // ==============================
    // DESENHAR ITENS E DESAFIOS
    // ==============================

    private void desenharItens(Graphics g) {

        // Chave de acesso
        if (!chaveColetada) {

            g.setColor(Color.YELLOW);

            g.fillOval(
                    3 * TILE_SIZE + 22,
                    1 * TILE_SIZE + 22,
                    25,
                    25
            );

            g.fillRect(
                    3 * TILE_SIZE + 40,
                    1 * TILE_SIZE + 30,
                    18,
                    8
            );
        }

        // Firewall
        if (!firewallSuperado) {

            g.setColor(Color.RED);

            g.fillRect(
                    4 * TILE_SIZE + 8,
                    6 * TILE_SIZE + 8,
                    54,
                    54
            );

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 13));

            g.drawString(
                    "FW",
                    4 * TILE_SIZE + 23,
                    6 * TILE_SIZE + 41
            );
        }

        // Porta de segurança
        if (!portaAberta) {

            g.setColor(new Color(180, 120, 20));

            g.fillRect(
                    8 * TILE_SIZE + 8,
                    7 * TILE_SIZE + 8,
                    54,
                    54
            );

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 13));

            g.drawString(
                    "LOCK",
                    8 * TILE_SIZE + 13,
                    7 * TILE_SIZE + 40
            );
        }

        // Token de segurança
        if (!tokenColetado) {

            g.setColor(Color.CYAN);

            g.fillOval(
                    6 * TILE_SIZE + 18,
                    7 * TILE_SIZE + 18,
                    34,
                    34
            );

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 15));

            g.drawString(
                    "T",
                    6 * TILE_SIZE + 30,
                    7 * TILE_SIZE + 41
            );
        }

        // Gateway
        g.setColor(Color.GREEN);

        g.fillRect(
                8 * TILE_SIZE + 10,
                8 * TILE_SIZE + 10,
                50,
                50
        );

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));

        g.drawString(
                "GW",
                8 * TILE_SIZE + 23,
                8 * TILE_SIZE + 40
        );
    }

    // ==============================
    // DESENHAR JOGADOR
    // ==============================

    private void desenharJogador(Graphics g) {

        g.setColor(Color.CYAN);

        g.fillOval(
                jogadorX * TILE_SIZE + 15,
                jogadorY * TILE_SIZE + 15,
                40,
                40
        );

        g.setColor(Color.WHITE);

        g.fillOval(
                jogadorX * TILE_SIZE + 24,
                jogadorY * TILE_SIZE + 25,
                7,
                7
        );

        g.fillOval(
                jogadorX * TILE_SIZE + 40,
                jogadorY * TILE_SIZE + 25,
                7,
                7
        );
    }

    // ==============================
    // CONTROLES DO TECLADO
    // ==============================

    private void configurarTeclas() {

        InputMap mapaTeclas = getRootPane().getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        ActionMap acoes = getRootPane().getActionMap();

        mapaTeclas.put(KeyStroke.getKeyStroke("UP"), "cima");
        mapaTeclas.put(KeyStroke.getKeyStroke("DOWN"), "baixo");
        mapaTeclas.put(KeyStroke.getKeyStroke("LEFT"), "esquerda");
        mapaTeclas.put(KeyStroke.getKeyStroke("RIGHT"), "direita");

        mapaTeclas.put(KeyStroke.getKeyStroke("W"), "cima");
        mapaTeclas.put(KeyStroke.getKeyStroke("S"), "baixo");
        mapaTeclas.put(KeyStroke.getKeyStroke("A"), "esquerda");
        mapaTeclas.put(KeyStroke.getKeyStroke("D"), "direita");

        acoes.put("cima", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moverJogador(jogadorX, jogadorY - 1);
            }
        });

        acoes.put("baixo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moverJogador(jogadorX, jogadorY + 1);
            }
        });

        acoes.put("esquerda", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moverJogador(jogadorX - 1, jogadorY);
            }
        });

        acoes.put("direita", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moverJogador(jogadorX + 1, jogadorY);
            }
        });
    }

    // ==============================
    // MOVIMENTAÇÃO
    // ==============================

    private void moverJogador(int x, int y) {

        if (jogoFinalizado) {
            return;
        }

        // Verificar limites do mapa
        if (x < 0 || y < 0 ||
                y >= mapa.length ||
                x >= mapa[0].length) {
            return;
        }

        // Verificar paredes
        if (mapa[y][x] == 1) {
            return;
        }

        // Verificar porta de segurança
        if (x == 8 && y == 7 && !portaAberta) {

            if (!chaveColetada ||
                    !firewallSuperado ||
                    !tokenColetado) {

                String faltando = "PORTA BLOQUEADA!\n\n";

                if (!chaveColetada) {
                    faltando += "- Chave de acesso\n";
                }

                if (!firewallSuperado) {
                    faltando += "- Firewall superado\n";
                }

                if (!tokenColetado) {
                    faltando += "- Token de segurança\n";
                }

                JOptionPane.showMessageDialog(
                        this,
                        faltando,
                        "Acesso negado",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            portaAberta = true;

            JOptionPane.showMessageDialog(
                    this,
                    "PORTA DESBLOQUEADA!\n"
                            + "Todos os requisitos foram cumpridos.\n"
                            + "Acesso ao Gateway liberado!",
                    "Segurança",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Atualizar posição
        jogadorX = x;
        jogadorY = y;

        // Consumir recursos
        bytes--;
        saltos--;

        // ==============================
        // COLETAR CHAVE
        // ==============================

        if (x == 3 && y == 1 && !chaveColetada) {

            chaveColetada = true;

            JOptionPane.showMessageDialog(
                    this,
                    "CHAVE COLETADA!\n"
                            + "Agora você pode continuar sua jornada.",
                    "Novo item",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        // ==============================
        // FIREWALL
        // ==============================

        if (x == 4 && y == 6 && !firewallSuperado) {

            firewallSuperado = true;

            bytes -= 3;
            saltos--;

            JOptionPane.showMessageDialog(
                    this,
                    "FIREWALL DETECTADO!\n"
                            + "Você conseguiu atravessar!\n"
                            + "Foram consumidos 3 bytes extras e 1 salto.",
                    "Desafio de segurança",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        // ==============================
        // COLETAR TOKEN
        // ==============================

        if (x == 6 && y == 7 && !tokenColetado) {

            tokenColetado = true;

            JOptionPane.showMessageDialog(
                    this,
                    "TOKEN DE ACESSO COLETADO!\n"
                            + "Agora você possui o token de segurança.",
                    "Token adquirido",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        atualizarInterface();
        painelJogo.repaint();

        // ==============================
        // VERIFICAR RECURSOS
        // ==============================

        if (bytes <= 0 || saltos <= 0) {

            finalizarJogo(false);
            return;
        }

        // ==============================
        // VERIFICAR GATEWAY
        // ==============================

        if (jogadorX == 8 && jogadorY == 8) {

            if (chaveColetada &&
                    firewallSuperado &&
                    tokenColetado &&
                    portaAberta) {

                finalizarJogo(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "GATEWAY BLOQUEADO!\n"
                                + "Você precisa cumprir todos os desafios.",
                        "Acesso negado",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    // ==============================
    // ATUALIZAR INTERFACE
    // ==============================

    private void atualizarInterface() {

        status.setText(
                " BYTES: " + bytes +
                        "       SALTOS: " + saltos
        );

        String chave = chaveColetada ? "SIM" : "NÃO";
        String token = tokenColetado ? "SIM" : "NÃO";

        inventario.setText(
                " CHAVE: " + chave +
                        "       TOKEN: " + token
        );
    }

    // ==============================
    // FINALIZAR JOGO
    // ==============================

    private void finalizarJogo(boolean venceu) {

        jogoFinalizado = true;

        String mensagem;

        if (venceu) {

            mensagem = "PARABÉNS!\n\n"
                    + "Você atravessou o labirinto!\n"
                    + "O Gateway foi desbloqueado!\n\n"
                    + "Bytes restantes: " + bytes;

        } else {

            mensagem = "FIM DE JOGO!\n\n"
                    + "Seus recursos acabaram.\n"
                    + "Tente novamente!";
        }

        int opcao = JOptionPane.showConfirmDialog(
                this,
                mensagem + "\n\nDeseja jogar novamente?",
                "Labirinto das Redes",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {

            reiniciarJogo();

        } else {

            dispose();
        }
    }

    // ==============================
    // REINICIAR PARTIDA
    // ==============================

    private void reiniciarJogo() {

        jogadorX = 1;
        jogadorY = 1;

        bytes = 50;
        saltos = 20;

        chaveColetada = false;
        tokenColetado = false;

        firewallSuperado = false;
        portaAberta = false;

        jogoFinalizado = false;

        atualizarInterface();
        painelJogo.repaint();
    }

    // ==============================
    // INICIAR JOGO
    // ==============================

    public static void play() {

        SwingUtilities.invokeLater(() -> {

            Game jogo = new Game();
            jogo.setVisible(true);

        });
    }
}
