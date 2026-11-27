
public abstract class ConsumableItem extends Item {

    public ConsumableItem(String name, String description, int weight) {
        super(name, description, weight);
    }

    public abstract void use();
}
