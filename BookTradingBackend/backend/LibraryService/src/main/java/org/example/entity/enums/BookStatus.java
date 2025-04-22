package org.example.entity.enums;
public enum BookStatus {
    ENABLED(0),
    DISABLED(1);

    private final int value;

    BookStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static BookStatus fromValue(int value) {
        for (BookStatus status : BookStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}