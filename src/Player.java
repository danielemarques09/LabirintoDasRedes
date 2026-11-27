import java.util.ArrayList;
import java.util.List;

public class Player {
    private Room currentRoom;
    private final List<Item> inventory = new ArrayList<>();
    private final int mtu;
    private int currentWeight;

    public Player(Room startingRoom, int mtu) {
        this.currentRoom = startingRoom;
        this.mtu = mtu;
        this.currentWeight = 0;
    }

    public Room getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(Room room) { currentRoom = room; }

    public boolean takeItem(String name) {
        Item item = currentRoom.getItem(name);
        if (item == null || currentWeight + item.getWeight() > mtu) {
            return false;
        }
        inventory.add(item);
        currentWeight += item.getWeight();
        currentRoom.removeItem(name);
        return true;
    }

    public boolean dropItem(String name) {
        Item item = getItem(name);
        if (item == null) return false;
        inventory.remove(item);
        currentWeight -= item.getWeight();
        currentRoom.addItem(item);
        return true;
    }

    public Item getItem(String name) {
        if (name == null) return null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name.trim())) return item;
        }
        return null;
    }

    public boolean hasItem(String name) { return getItem(name) != null; }

    public boolean consumeItem(String name) {
        Item item = getItem(name);
        if (!(item instanceof ConsumableItem)) return false;
        inventory.remove(item);
        currentWeight -= item.getWeight();
        return true;
    }

    public int getCurrentWeight() { return currentWeight; }
    public int getMtu() { return mtu; }
    public List<Item> getInventory() { return new ArrayList<>(inventory); }

    public String getInventoryString() {
        if (inventory.isEmpty()) return "Inventário vazio.\nPeso: 0/" + mtu + " bytes";
        StringBuilder text = new StringBuilder();
        for (Item item : inventory) text.append("• ").append(item).append("\n");
        text.append("\nPeso: ").append(currentWeight).append("/").append(mtu).append(" bytes");
        return text.toString();
    }
}
