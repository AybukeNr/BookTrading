package org.example.entity.enums;

public enum BookCondition {
    NEW("Yeni"),
    LIKE_NEW("Yeni Gibi"),
    VERY_GOOD("Çok İyi"),
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
}
