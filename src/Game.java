
package game;

import parser.Parser;
import command.Command;
import player.Player;
import item.Item;
import room.Room;
import lockedroom.LockedRoom;

public class Game {

    private Parser parser;
    private Player player;

    private int maxSteps;
    private int currentSteps;

    private boolean finished;

    public Game() {

        parser = new Parser();

        maxSteps = 20;
        currentSteps = 0;
        finished = false;

        createRooms();
    }

    private void createRooms() {

        Room inicio = new Room("Ponto de Origem");
        Room switches = new Room("Sala dos Switches");
        Room servidores = new Room("Central de Servidores");

        LockedRoom firewall =
                new LockedRoom("Firewall", "token");

        Room gateway =
                new Room("Gateway de Saída para a Nuvem");

        inicio.setExit("leste", switches);

        switches.setExit("oeste", inicio);
        switches.setExit("leste", servidores);

        servidores.setExit("oeste", switches);
        servidores.setExit("norte", firewall);

        firewall.setExit("sul", servidores);
        firewall.setExit("leste", gateway);

        gateway.setExit("oeste", firewall);

        switches.addItem(new Item(
                "token",
                "Token de autenticação",
                10
        ));

        switches.addItem(new Item(
                "ping",
                "Ping de diagnóstico",
                5
        ));

        servidores.addItem(new Item(
                "chave",
                "Chave de segurança",
                20
        ));

        servidores.addItem(new Item(
                "log",
                "Log corrompido",
                15
        ));

        player = new Player(inicio, 50);
    }

    public void play() {

        printWelcome();

        while (!finished) {

            Command command = parser.getCommand();

            processCommand(command);
        }
    }

    private void processCommand(Command command) {

        if (command.isUnknown()) {
            System.out.println("Comando desconhecido.");
            return;
        }

        String action = command.getCommandWord();

        switch (action) {

            case "ajuda":
                printHelp();
                break;

            case "ir":
                goRoom(command);
                break;

            case "pegar":

                if (command.hasSecondWord()) {
                    player.takeItem(command.getSecondWord());
                } else {
                    System.out.println("Pegar o quê?");
                }

                break;

            case "solta":

                if (command.hasSecondWord()) {
                    player.dropItem(command.getSecondWord());
                } else {
                    System.out.println("Soltar o quê?");
                }

                break;

            case "inventario":
                System.out.println(player.getInventoryString());
                break;

            case "usar":

                if (command.hasSecondWord()) {
                    player.useItem(command.getSecondWord());
                } else {
                    System.out.println("Usar o quê?");
                }

                break;

            case "olhar":
                System.out.println(
                        player.getCurrentRoom().getLongDescription()
                );
                break;

            case "sair":
                finished = true;
                System.out.println("Você saiu do jogo.");
                break;

            default:
                System.out.println("Comando inválido.");
        }
    }

    private void goRoom(Command command) {

        if (!command.hasSecondWord()) {
            System.out.println("Ir para onde?");
            return;
        }

        boolean moved =
                player.goRoom(command.getSecondWord());

        if (moved) {

            currentSteps++;

            System.out.println(
                    "Saltos: " + currentSteps + "/" + maxSteps
            );

            if (player.getCurrentRoom().getDescription()
                    .equals("Gateway de Saída para a Nuvem")
                    && player.hasItem("chave")) {

                winGame();
                return;
            }

            if (currentSteps >= maxSteps) {
                gameOver();
            }
        }
    }

    private void printWelcome() {

        System.out.println("================================");
        System.out.println("       LABIRINTO DAS REDES");
        System.out.println("================================");

        System.out.println(
                "Um ataque de Ransomware foi detectado!"
        );

        System.out.println(
                "Encontre a chave e alcance o Gateway!"
        );

        System.out.println(
                player.getCurrentRoom().getLongDescription()
        );

        printHelp();
    }

    private void printHelp() {

        System.out.println("\nComandos disponíveis:");

        parser.showCommands();

        System.out.println("\nExemplo: ir leste");
    }

    private void winGame() {

        finished = true;

        System.out.println("\nPARABÉNS! VOCÊ VENCEU!");
        System.out.println("A chave foi encontrada.");
        System.out.println("Você alcançou o Gateway!");
    }

    private void gameOver() {

        finished = true;

        System.out.println("\nGAME OVER!");
        System.out.println("O limite de saltos foi atingido.");
        System.out.println("O sistema foi criptografado.");
    }
}
