
package player;

import java.util.ArrayList;
import java.util.List;

import item.Item;
import room.Room;
import lockedroom.LockedRoom;

public class Player {

    private Room currentRoom;

    private List<Item> inventory;

    private int maxWeight;
    private int currentWeight;

    public Player(Room startingRoom, int maxWeight) {

        this.currentRoom = startingRoom;
        this.maxWeight = maxWeight;
        this.currentWeight = 0;

        inventory = new ArrayList<>();
    }

    public boolean goRoom(String direction) {

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {

            System.out.println("Não existe saída nessa direção.");

            return false;
        }

        if (nextRoom instanceof LockedRoom) {

            LockedRoom lockedRoom = (LockedRoom) nextRoom;

            if (!lockedRoom.unlock(this)) {
                return false;
            }
        }

        currentRoom = nextRoom;

        System.out.println(currentRoom.getLongDescription());

        return true;
    }

    public boolean takeItem(String itemName) {

        Item item = currentRoom.getItem(itemName);

        if (item == null) {

            System.out.println("Esse item não existe nesta sala.");

            return false;
        }

        if (currentWeight + item.getWeight() > maxWeight) {

            System.out.println("Limite de bytes excedido!");

            System.out.println(
                    "Peso atual: " + currentWeight +
                            "/" + maxWeight + " bytes."
            );

            return false;
        }

        currentRoom.removeItem(itemName);

        inventory.add(item);

        currentWeight += item.getWeight();

        System.out.println("Você pegou: " + item.getName());

        System.out.println(
                "Peso atual: " + currentWeight +
                        "/" + maxWeight + " bytes."
        );

        return true;
    }

    public boolean dropItem(String itemName) {

        for (int i = 0; i < inventory.size(); i++) {

            Item item = inventory.get(i);

            if (item.getName().equalsIgnoreCase(itemName)) {

                inventory.remove(i);

                currentWeight -= item.getWeight();

                currentRoom.addItem(item);

                System.out.println("Você soltou: " + item.getName());

                return true;
            }
        }

        System.out.println("Você não possui esse item.");

        return false;
    }

    public boolean hasItem(String itemName) {

        for (Item item : inventory) {

            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }

        return false;
    }

    public String getInventoryString() {

        if (inventory.isEmpty()) {
            return "Seu inventário está vazio.";
        }

        StringBuilder result = new StringBuilder();

        result.append("\nINVENTÁRIO:");

        for (Item item : inventory) {

            result.append("\n- ")
                    .append(item.getName())
                    .append(" (")
                    .append(item.getWeight())
                    .append(" bytes)");
        }

        result.append("\nPeso total: ")
                .append(currentWeight)
                .append("/")
                .append(maxWeight)
                .append(" bytes.");

        return result.toString();
    }

    public boolean useItem(String itemName) {

        if (!hasItem(itemName)) {

            System.out.println("Você não possui esse item.");

            return false;
        }

        for (String direction :
                new String[]{"norte", "sul", "leste", "oeste"}) {

            Room nextRoom = currentRoom.getExit(direction);

            if (nextRoom instanceof LockedRoom) {

                LockedRoom lockedRoom = (LockedRoom) nextRoom;

                if (lockedRoom.getRequiredKeyName()
                        .equalsIgnoreCase(itemName)) {

                    return lockedRoom.unlock(this);
                }
            }
        }

        System.out.println("Não é possível utilizar esse item aqui.");

        return false;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }
}
