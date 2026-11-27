
import java.util.Scanner;

public class Parser {

    private final CommandWords commands;
    private final Scanner scanner;

    public Parser() {
        commands = new CommandWords();
        scanner = new Scanner(System.in);
    }

    public Command getCommand() {

        System.out.print("> ");

        String input = scanner.nextLine().trim().toLowerCase();

        if (input.isEmpty()) {
            return new Command(null, null);
        }

        String[] words = input.split("\\s+", 2);

        String commandWord = words[0];
        String secondWord = words.length > 1 ? words[1] : null;

        if (!commands.isCommand(commandWord)) {
            return new Command(null, secondWord);
        }

        return new Command(commandWord, secondWord);
    }

    public String showCommands() {
        return commands.showAll();
    }
}
