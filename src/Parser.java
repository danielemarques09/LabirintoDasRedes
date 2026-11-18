
package parser;

import java.util.Scanner;
import command.Command;
import commandwords.CommandWords;

public class Parser {

    private CommandWords commands;
    private Scanner reader;

    public Parser() {

        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    public Command getCommand() {

        System.out.print("\n> ");

        String input = reader.nextLine().trim().toLowerCase();

        if (input.isEmpty()) {
            return new Command(null, null);
        }

        String[] words = input.split("\\s+", 2);

        String word1 = words[0];

        String word2 = null;

        if (words.length > 1) {
            word2 = words[1];
        }

        if (!commands.isCommand(word1)) {

            return new Command(null, word2);
        }

        return new Command(word1, word2);
    }

    public void showCommands() {
        commands.showAll();
    }
}
