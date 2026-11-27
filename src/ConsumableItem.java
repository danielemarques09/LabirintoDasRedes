public abstract class ConsumableItem extends Item {
    protected ConsumableItem(String name, String description, int weight) {
        super(name, description, weight);
    }

    public abstract String use();
}
