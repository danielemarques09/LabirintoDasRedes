
package room;

import java.util.HashMap;
import java.util.Map;

import item.Item;

public class Room {

    private String description;

    private Map<String, Room> exits;
    private Map<String, Item> items;

    public Room(String description) {

        this.description = description;

        exits = new HashMap<>();
        items = new HashMap<>();
    }

    public void setExit(String direction, Room room) {

        exits.put(direction.toLowerCase(), room);
    }

    public Room getExit(String direction) {

        return exits.get(direction.toLowerCase());
    }

    public String getExitString() {

        StringBuilder result = new StringBuilder("Saídas:");

        for (String direction : exits.keySet()) {

            result.append(" ").append(direction);
        }

        return result.toString();
    }

    public void addItem(Item item) {

        items.put(item.getName().toLowerCase(), item);
    }

    public Item removeItem(String itemName) {

        return items.remove(itemName.toLowerCase());
    }

    public Item getItem(String itemName) {

        return items.get(itemName.toLowerCase());
    }

    public String getItemString() {

        if (items.isEmpty()) {

            return "Não existem itens nesta sala.";
        }

        StringBuilder result =
                new StringBuilder("Itens encontrados:");

        for (Item item : items.values()) {

            result.append("\n- ").append(item);
        }

        return result.toString();
    }

    public String getLongDescription() {

        return "\nVocê está em: " + description +
                "\n" + getExitString() +
                "\n" + getItemString();
    }

    public String getDescription() {

        return description;
    }
}
