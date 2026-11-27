public class Parser {
    private final CommandWords commandWords = new CommandWords();

    public Command parse(String input) {
        if (input == null || input.isBlank()) {
            return new Command(null, null);
        }

        String[] words = input.trim().toLowerCase().split("\\s+", 2);
        String first = words[0];
        String second = words.length > 1 ? words[1] : null;

        if (!commandWords.isCommand(first)) {
            return new Command(null, second);
        }
        return new Command(first, second);
    }

    public String getCommands() {
        return commandWords.showAll();
    }
}
