public class LockedRoom extends Room {
    private final String requiredItem;

    public LockedRoom(String name, String description, String requiredItem) {
        super(name, description);
        this.requiredItem = requiredItem;
    }

    @Override
    public boolean canEnter(Player player) {
        return player.hasItem(requiredItem);
    }

    public String getRequiredItem() { return requiredItem; }
}
