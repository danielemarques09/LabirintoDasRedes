
import java.util.ArrayList;
import java.util.List;

public class Game {

    private Player player;
    private List<Room> rooms;

    private int currentSteps;
    private final int maxSteps = 100;

    private final long startTime;
    private final long timeLimit = 180000;

    private boolean finished;
    private String status;

    public Game() {

        currentSteps = 0;
        finished = false;
        status = "Encontre os itens e chegue ao Gateway.";

        startTime = System.currentTimeMillis();

        createRooms();
    }

    private void createRooms() {

        rooms = new ArrayList<>();

        Room start = new Room("Sala inicial - Estação de trabalho");
        Room corridor = new Room("Corredor principal da rede");
        Room switchRoom = new Room("Sala do Switch");
        LockedRoom firewall = new LockedRoom(
                "Sala protegida pelo Firewall",
                "token"
        );
        Room server = new Room("Sala dos Servidores");
        LockedRoom gateway = new LockedRoom(
                "Gateway principal",
                "chave"
        );

        start.setExit("leste", corridor);

        corridor.setExit("oeste", start);
        corridor.setExit("leste", switchRoom);

        switchRoom.setExit("oeste", corridor);
        switchRoom.setExit("sul", firewall);

        firewall.setExit("norte", switchRoom);
        firewall.setExit("leste", server);

        server.setExit("oeste", firewall);
        server.setExit("leste", gateway);

        gateway.setExit("oeste", server);

        start.addItem(new PingItem());
        corridor.addItem(new TokenItem());
        switchRoom.addItem(new CorruptedLogItem());
        server.addItem(new SecurityKey());

        switchRoom.setChallenge(
                new NetworkChallenge(
                        "Qual é o endereço IP do Gateway?",
                        "192.168.1.1"
                )
        );

        rooms.add(start);
        rooms.add(corridor);
        rooms.add(switchRoom);
        rooms.add(firewall);
        rooms.add(server);
        rooms.add(gateway);

        player = new Player(start);
    }

    public boolean enterMazeRoom(int destinationIndex) {

        if (finished) {
            return false;
        }

        if (destinationIndex < 0 || destinationIndex >= rooms.size()) {
            return false;
        }

        Room current = player.getCurrentRoom();
        Room destination = rooms.get(destinationIndex);

        if (current == destination) {
            return true;
        }

        if (!current.hasExitTo(destination)) {
            status = "Esse caminho não está disponível.";
            return false;
        }

        NetworkChallenge challenge = current.getChallenge();

        if (challenge != null && !challenge.isSolved()) {
            status = "Resolva o desafio da sala antes de sair.";
            return false;
        }

        if (!destination.canEnter(player)) {
            status = "Acesso bloqueado! Você precisa do item necessário.";
            return false;
        }

        player.setCurrentRoom(destination);
        currentSteps++;

        status = "Você entrou em: " + destination.getDescription();

        if (destination == rooms.get(5)) {
            victory();
        }

        checkLimits();

        return !finished;
    }

    public void countMovement() {

        if (finished) {
            return;
        }

        currentSteps++;
        checkLimits();
    }

    public boolean takeItem(String name) {

        if (finished) {
            return false;
        }

        boolean result = player.takeItem(name);

        if (result) {
            status = "Item coletado: " + name;
        } else {
            status = "Não foi possível pegar esse item. Confira o nome e o peso.";
        }

        return result;
    }

    public boolean dropItem(String name) {

        if (finished) {
            return false;
        }

        boolean result = player.dropItem(name);

        status = result
                ? "Item solto: " + name
                : "Você não possui esse item.";

        return result;
    }

    public void usePing() {

        if (finished) {
            return;
        }

        if (!player.hasItem("ping")) {
            status = "Você não possui o item Ping.";
            return;
        }

        status = "Ping realizado! Verifique a conexão com o Gateway: 192.168.1.1.";
    }

    public boolean solveChallenge(String answer) {

        if (finished) {
            return false;
        }

        NetworkChallenge challenge =
                player.getCurrentRoom().getChallenge();

        if (challenge == null) {
            status = "Não existe desafio nesta sala.";
            return false;
        }

        if (challenge.checkAnswer(answer)) {
            status = "Resposta correta! O caminho está liberado.";
            return true;
        }

        status = "Resposta incorreta. Tente novamente.";
        return false;
    }

    private void checkLimits() {

        if (currentSteps >= maxSteps) {
            gameOver("Você ultrapassou o limite de movimentos.");
            return;
        }

        if (System.currentTimeMillis() - startTime >= timeLimit) {
            gameOver("O tempo acabou! A rede foi comprometida.");
        }
    }

    private void victory() {

        finished = true;
        status = "PARABÉNS! Você chegou ao Gateway e concluiu a missão!";
    }

    private void gameOver(String message) {
        finished = true;
        status = message;
    }

    public int getRemainingSeconds() {

        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = Math.max(0, timeLimit - elapsed);

        if (remaining == 0 && !finished) {
            gameOver("O tempo acabou! A rede foi comprometida.");
        }

        return (int) (remaining / 1000);
    }

    public int getCurrentRoomIndex() {
        return rooms.indexOf(player.getCurrentRoom());
    }

    public int getCurrentSteps() {
        return currentSteps;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public int getInventoryWeight() {
        return player.getCurrentWeight();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public String getStatus() {
        return status;
    }

    public boolean isFinished() {
        return finished;
    }
}
