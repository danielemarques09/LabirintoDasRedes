
package commandwords;

import java.util.HashSet;
import java.util.Set;

public class CommandWords {

    private Set<String> validCommands;

    public CommandWords() {

        validCommands = new HashSet<>();

        validCommands.add("ir");
        validCommands.add("sair");
        validCommands.add("ajuda");
        validCommands.add("pegar");
        validCommands.add("solta");
        validCommands.add("inventario");
        validCommands.add("usar");
        validCommands.add("olhar");
    }

    public boolean isCommand(String word) {

        return validCommands.contains(word);
    }

    public void showAll() {

        System.out.println("Comandos disponíveis:");

        for (String command : validCommands) {
            System.out.println("- " + command);
        }
    }
}
