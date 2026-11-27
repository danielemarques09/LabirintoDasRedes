
import java.util.Set;

public class CommandWords {

    private final Set<String> validCommands = Set.of(
            "ir", "sair", "ajuda", "pegar",
            "solta", "inventario", "usar", "olhar"
    );

    public boolean isCommand(String word) {
        return validCommands.contains(word);
    }

    public String showAll() {
        return String.join(", ", validCommands);
    }
}
