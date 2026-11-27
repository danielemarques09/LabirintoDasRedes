
import java.util.ArrayList;
import java.util.List;

public class Player {

    private Room currentRoom;

    private final List<Item> inventory;

    private final int maxWeight;
    private int currentWeight;

    public Player(Room startingRoom) {
        currentRoom = startingRoom;
        inventory = new ArrayList<>();
        maxWeight = 50;
        currentWeight = 0;
    }

    public boolean goRoom(String direction) {

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            return false;
        }

        if (!nextRoom.canEnter(this)) {
            return false;
        }

        currentRoom = nextRoom;
        return true;
    }

    public void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public boolean takeItem(String name) {

        Item item = currentRoom.getItem(name);

        if (item == null) {
            return false;
        }

        if (currentWeight + item.getWeight() > maxWeight) {
            return false;
        }

        inventory.add(item);
        currentWeight += item.getWeight();

        currentRoom.removeItem(name);

        return true;
    }

    public boolean dropItem(String name) {

        Item found = getItem(name);

        if (found == null) {
            return false;
        }

        inventory.remove(found);
        currentWeight -= found.getWeight();

        currentRoom.addItem(found);

        return true;
    }

    public boolean hasItem(String name) {
        return getItem(name) != null;
    }

    public Item getItem(String name) {

        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }

        return null;
    }

    public boolean consumeItem(String name) {

        Item item = getItem(name);

        if (item == null) {
            return false;
        }

        inventory.remove(item);
        currentWeight -= item.getWeight();

        return true;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public String getInventoryString() {

        if (inventory.isEmpty()) {
            return "Seu inventário está vazio.";
        }

        StringBuilder result = new StringBuilder();

        for (Item item : inventory) {
            result.append(item.toString()).append("\n");
        }

        result.append("\nPeso: ")
                .append(currentWeight)
                .append("/")
                .append(maxWeight);

        return result.toString();
    }
}
