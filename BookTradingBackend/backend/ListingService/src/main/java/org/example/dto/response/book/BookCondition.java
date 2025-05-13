package org.example.dto.response.book;

public enum BookCondition {
    NEW("Yeni"),
    GOOD("İyi"),
    ACCEPTABLE("Kabul Edilebilir"),
    POOR("Kötü");

    private final String description;

    BookCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static BookCondition fromDescription(String description) {
        for (BookCondition condition : values()) {
            if (condition.getDescription().equalsIgnoreCase(description)) {
                return condition;
            }
        }
        throw new IllegalArgumentException("Invalid book condition: " + description);
    }


}
