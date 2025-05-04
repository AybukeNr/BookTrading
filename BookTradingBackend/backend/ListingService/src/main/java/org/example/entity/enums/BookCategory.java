package org.example.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum BookCategory {
    Fiction("Kurgu"),
    Science ("Bilim"),
    History("Tarih"),
    Biography("Biyografi"),
    Social_Science("Toplum Bilimi"),
    Comics("Çizgi Roman"),
    Business("İş"),
    Sports("Spor"),
    Religion("Din"),
    Poetry("Şiir"),
    Drama("Dram"),
    Psychology("Psikoloji"),
    Philosophy("Felsefe"),
    Self_Help("Kişisel Gelişim"),
    Computers("Bilgisayar Bilimi"),
    Cooking("Yemek"),
    Education("Eğitim"),
    Juvenile_Fiction("Gençlik Romanları");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static BookCategory fromDisplayName(String value) {
        return Arrays.stream(BookCategory.values())
                .filter(cat -> cat.getDisplayName().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + value));
    }
}
