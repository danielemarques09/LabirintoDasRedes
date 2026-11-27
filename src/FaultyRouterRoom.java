public class FaultyRouterRoom extends Room {
    public FaultyRouterRoom(String name, String description) {
        super(name, description);
    }

    @Override
    public int getEntryPenalty() {
        return 2;
    }
}
