public class PingItem extends ConsumableItem {
    public PingItem() {
        super("ping", "Ferramenta de diagnóstico de conectividade", 5);
    }

    @Override
    public String use() {
        return "PING 192.168.1.1: resposta recebida. Conectividade identificada.";
    }
}
