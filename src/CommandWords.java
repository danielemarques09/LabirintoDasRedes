import java.util.Set;

public class CommandWords {
    private final Set<String> commands = Set.of(
            "ir", "sair", "ajuda", "pegar", "solta",
            "inventario", "inventário", "usar", "olhar"
    );

    public boolean isCommand(String word) {
        return word != null && commands.contains(word.toLowerCase());
    }

    public String showAll() {
        return String.join(", ", commands);
    }
}
