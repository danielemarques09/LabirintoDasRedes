
import java.util.HashMap;
import java.util.Map;

public class Room {

    private final String description;

    private final Map<String, Room> exits;
    private final Map<String, Item> items;

    private NetworkChallenge challenge;

    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    public void setExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public boolean hasExitTo(Room room) {
        return exits.containsValue(room);
    }

    public void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item);
    }

    public Item removeItem(String name) {
        return items.remove(name.toLowerCase());
    }

    public Item getItem(String name) {
        return items.get(name.toLowerCase());
    }

    public String getItemString() {

        if (items.isEmpty()) {
            return "Nenhum item disponível.";
        }

        return String.join(", ", items.keySet());
    }

    public void setChallenge(NetworkChallenge challenge) {
        this.challenge = challenge;
    }

    public NetworkChallenge getChallenge() {
        return challenge;
    }

    public boolean canEnter(Player player) {
        return true;
    }

    public String getDescription() {
        return description;
    }

    public String getLongDescription() {
        return description + "\nSaídas: " + getExitString()
                + "\nItens: " + getItemString();
    }

    public String getExitString() {
        return String.join(", ", exits.keySet());
    }
}
