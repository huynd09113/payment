@FunctionalInterface
public interface CommandHandler {
    void handle(Customer customer, String[] args);
}
