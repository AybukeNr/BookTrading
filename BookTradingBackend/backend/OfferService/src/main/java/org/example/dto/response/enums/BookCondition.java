package org.example.dto.response.enums;

public enum BookCondition {
    NEW("Yeni"),
    LIKE_NEW("Yeni Gibi"),
    VERY_GOOD("Çok İyi"),
    GOOD("İyi"),
    ACCEPTABLE("Kabul Edilebilir"),
    POOR("Kötü");

    private final String label;

    BookCondition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
