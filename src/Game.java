import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private final List<Room> rooms = new ArrayList<>();
    private final MazeMap mazeMap = new MazeMap();
    private final Parser parser = new Parser();

    private int playerX = 2;
    private int playerY = 2;
    private int hops = 0;
    private final int maxHops = 85;
    private final long startTime = System.currentTimeMillis();
    private final long timeLimitMillis = 180_000;

    private boolean finished;
    private boolean victory;
    private String status = "Encontre o Ping, o token, o log e a chave. Chegue ao Gateway!";

    public Game() { createRooms(); }

    private void createRooms() {
        Room start = new Room("Estação de Trabalho",
                "Você iniciou na estação. Colete o Ping para testar a conectividade.");
        Room corridor = new Room("Corredor Principal",
                "O corredor conecta os equipamentos. Há um token de autenticação.");
        Room switchRoom = new Room("Sala do Switch",
                "O switch controla o tráfego. Resolva o desafio de endereço IP.");
        LockedRoom firewall = new LockedRoom("Firewall",
                "Área protegida. É necessário possuir o token.", "token");
        FaultyRouterRoom deadEnd = new FaultyRouterRoom("Beco sem saída — Roteador defeituoso",
                "O roteador está instável. A entrada custa dois saltos extras. Há um log corrompido.");
        Room servers = new Room("Sala dos Servidores",
                "Os servidores guardam a chave de segurança do Gateway.");
        LockedRoom gateway = new LockedRoom("Gateway",
                "Saída da rede. A chave de segurança é obrigatória.", "chave");

        start.setExit("leste", corridor);
        corridor.setExit("oeste", start);
        corridor.setExit("leste", switchRoom);
        corridor.setExit("sul", deadEnd);
        switchRoom.setExit("oeste", corridor);
        switchRoom.setExit("sul", firewall);
        firewall.setExit("norte", switchRoom);
        firewall.setExit("leste", servers);
        servers.setExit("oeste", firewall);
        servers.setExit("leste", gateway);
        gateway.setExit("oeste", servers);
        deadEnd.setExit("norte", corridor);

        start.addItem(new PingItem());
        corridor.addItem(new TokenItem());
        deadEnd.addItem(new CorruptedLogItem());
        servers.addItem(new SecurityKey());

        switchRoom.setChallenge(new NetworkChallenge(
                "Qual é o endereço IPv4 do Gateway indicado no log?",
                "192.168.1.1"
        ));

        rooms.add(start);
        rooms.add(corridor);
        rooms.add(switchRoom);
        rooms.add(firewall);
        rooms.add(deadEnd);
        rooms.add(servers);
        rooms.add(gateway);

        player = new Player(start, 50);
    }

    public boolean movePlayer(int dx, int dy) {
        if (finished) return false;
        checkLimits();
        if (finished) return false;

        int nx = playerX + dx;
        int ny = playerY + dy;
        if (!mazeMap.isWalkable(nx, ny)) {
            status = "Parede! Esse caminho está bloqueado.";
            return false;
        }

        int currentRoomIndex = rooms.indexOf(player.getCurrentRoom());
        int targetRoomIndex = mazeMap.getRoomIndex(nx, ny);
        Room current = player.getCurrentRoom();
        Room target = rooms.get(targetRoomIndex);

        if (targetRoomIndex != currentRoomIndex) {
            if (!current.hasExitTo(target)) {
                status = "Não há passagem direta para essa sala.";
                return false;
            }

            NetworkChallenge challenge = current.getChallenge();
            if (challenge != null && !challenge.isSolved()) {
                status = "Resolva o desafio do Switch antes de sair.";
                return false;
            }

            if (!target.canEnter(player)) {
                status = "Acesso negado: falta o item de autorização exigido.";
                return false;
            }

            player.setCurrentRoom(target);
            status = "Você entrou em: " + target.getName() + ".";
            hops += target.getEntryPenalty();
            if (target.getEntryPenalty() > 0) {
                status += " Roteador defeituoso: +" + target.getEntryPenalty() + " saltos.";
            }
        }

        playerX = nx;
        playerY = ny;
        hops++;
        checkLimits();

        if (targetRoomIndex == 6 && !finished) {
            victory = true;
            finished = true;
            status = "MISSÃO CONCLUÍDA! Você chegou ao Gateway com a chave de segurança.";
        }
        return true;
    }

    public void executeCommand(String input) {
        if (finished) { status = "A missão já terminou."; return; }
        checkLimits();
        if (finished) return;

        Command command = parser.parse(input);
        if (command.isUnknown()) { status = "Comando desconhecido. Digite ajuda."; return; }

        String word = command.getCommandWord();
        String arg = command.getSecondWord();

        switch (word) {
            case "ir":
                moveByDirection(arg);
                break;
            case "pegar":
                status = arg == null ? "Informe o nome do item." :
                        (takeItem(arg) ? "Item coletado: " + arg : "Não foi possível pegar. Verifique o item e o limite de bytes.");
                break;
            case "solta":
                status = arg == null ? "Informe o nome do item." :
                        (dropItem(arg) ? "Item solto: " + arg : "Você não possui esse item.");
                break;
            case "inventario":
            case "inventário":
                status = player.getInventoryString();
                break;
            case "usar":
                useItem(arg);
                break;
            case "olhar":
                status = player.getCurrentRoom().getLongDescription();
                break;
            case "ajuda":
                status = "Comandos: " + parser.getCommands() +
                        ". Movimento: setas ou W/A/S/D. Objetivo: obter a chave e chegar ao Gateway.";
                break;
            case "sair":
                finished = true;
                victory = false;
                status = "Você encerrou a missão.";
                break;
            default:
                status = "Comando inválido.";
        }
    }

    private void moveByDirection(String direction) {
        if (direction == null) { status = "Informe norte, sul, leste ou oeste."; return; }
        switch (direction) {
            case "norte": movePlayer(0, -1); break;
            case "sul": movePlayer(0, 1); break;
            case "leste": movePlayer(1, 0); break;
            case "oeste": movePlayer(-1, 0); break;
            default: status = "Direção inválida.";
        }
    }

    public boolean takeItem(String name) {
        if (finished) return false;
        return player.takeItem(name);
    }

    public boolean dropItem(String name) {
        if (finished) return false;
        return player.dropItem(name);
    }

    private void useItem(String name) {
        if (name == null) { status = "Informe o item ou o endereço IP para usar."; return; }

        if (name.equalsIgnoreCase("ping")) {
            Item item = player.getItem("ping");
            if (item instanceof PingItem) {
                status = ((PingItem) item).use();
                player.consumeItem("ping");
            } else {
                status = "Você não possui o Ping.";
            }
        } else if (name.equalsIgnoreCase("log")) {
            if (player.hasItem("log")) {
                status = "Trecho recuperado do log: Gateway IPv4 = 192.168.1.1. Use esse endereço no desafio do Switch.";
            } else {
                status = "Você precisa encontrar o log corrompido no beco sem saída.";
            }
        } else if (name.equals("192.168.1.1")) {
            solveChallenge(name);
        } else {
            status = "Esse item não pode ser usado agora.";
        }
    }

    public boolean solveChallenge(String answer) {
        if (finished) return false;
        if (!player.hasItem("log")) {
            status = "Você precisa coletar o log corrompido para ter a pista.";
            return false;
        }
        NetworkChallenge challenge = player.getCurrentRoom().getChallenge();
        if (challenge == null) {
            status = "Você precisa estar na Sala do Switch para resolver o desafio.";
            return false;
        }
        if (challenge.checkAnswer(answer)) {
            status = "Resposta correta! O desafio do Switch foi resolvido.";
            return true;
        }
        status = "Endereço incorreto. Consulte o log e tente novamente.";
        return false;
    }

    private void checkLimits() {
        if (finished) return;
        if (hops >= maxHops) {
            gameOver("LIMITE DE SALTOS EXCEDIDO! O ransomware criptografou os dados.");
            return;
        }
        if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
            gameOver("TEMPO ESGOTADO! O ransomware criptografou os dados.");
        }
    }

    private void gameOver(String message) {
        finished = true;
        victory = false;
        status = message;
    }

    public int getRemainingSeconds() {
        long remaining = Math.max(0, timeLimitMillis - (System.currentTimeMillis() - startTime));
        if (remaining == 0 && !finished) gameOver("TEMPO ESGOTADO! O ransomware criptografou os dados.");
        return (int) (remaining / 1000);
    }

    public Player getPlayer() { return player; }
    public MazeMap getMazeMap() { return mazeMap; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int getHops() { return hops; }
    public int getMaxHops() { return maxHops; }
    public boolean isFinished() { return finished; }
    public boolean isVictory() { return victory; }
    public String getStatus() { return status; }
    public int getCurrentRoomIndex() { return rooms.indexOf(player.getCurrentRoom()); }
}
