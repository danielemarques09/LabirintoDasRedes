import java.util.LinkedHashMap;
import java.util.Map;

public class Room {
    private final String name;
    private final String description;
    private final Map<String, Room> exits = new LinkedHashMap<>();
    private final Map<String, Item> items = new LinkedHashMap<>();
    private NetworkChallenge challenge;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setExit(String direction, Room room) {
        exits.put(direction.toLowerCase(), room);
    }

    public Room getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    public boolean hasExitTo(Room room) {
        return exits.containsValue(room);
    }

    public void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item);
    }

    public Item getItem(String name) {
        return name == null ? null : items.get(name.toLowerCase());
    }

    public Item removeItem(String name) {
        return name == null ? null : items.remove(name.toLowerCase());
    }

    public String getItemString() {
        return items.isEmpty() ? "Nenhum item disponível." : String.join(", ", items.keySet());
    }

    public void setChallenge(NetworkChallenge challenge) {
        this.challenge = challenge;
    }

    public NetworkChallenge getChallenge() { return challenge; }

    public boolean canEnter(Player player) { return true; }

    public int getEntryPenalty() { return 0; }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public String getLongDescription() {
        return name + "\n" + description
                + "\nSaídas: " + String.join(", ", exits.keySet())
                + "\nItens: " + getItemString();
    }
}
