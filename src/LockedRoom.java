public class LockedRoom extends Room {

    private String requiredKeyName;
    private boolean isLocked;

    public LockedRoom(String description, String requiredKeyName) {

        super(description);

        this.requiredKeyName = requiredKeyName.toLowerCase();

        this.isLocked = true;
    }

    public boolean unlock(Player player) {

        if (!isLocked) {
            return true;
        }

        if (player.hasItem(requiredKeyName)) {

            isLocked = false;

            System.out.println("\nFirewall desbloqueado!");

            System.out.println("Acesso autorizado.");

            return true;
        }

        System.out.println("\nACESSO NEGADO!");

        System.out.println(
                "Você precisa do item: " + requiredKeyName
        );

        return false;
    }

    public boolean isLocked() {

        return isLocked;
    }

    public String getRequiredKeyName() {

        return requiredKeyName;
    }
}
