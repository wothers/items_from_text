package wothers.ift;

public class ItemLoadException extends RuntimeException {
    public ItemLoadException(String namespaceName, String itemName) {
        super(getErrorMessage(namespaceName, itemName, null));
    }

    public ItemLoadException(String namespaceName, String itemName, String reason) {
        super(getErrorMessage(namespaceName, itemName, reason));
    }

    public static String getErrorMessage(String namespaceName, String itemName, String reason) {
        if (reason == null) return String.format("Failed to load item: %s:%s", namespaceName, itemName);
        return String.format("Failed to load item: %s:%s - %s", namespaceName, itemName, reason);
    }
}
