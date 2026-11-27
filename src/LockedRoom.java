
public class LockedRoom extends Room {

    private final String requiredKeyName;

    public LockedRoom(String description, String requiredKeyName) {
        super(description);
        this.requiredKeyName = requiredKeyName;
    }

    @Override
    public boolean canEnter(Player player) {
        return player.hasItem(requiredKeyName);
    }
}
