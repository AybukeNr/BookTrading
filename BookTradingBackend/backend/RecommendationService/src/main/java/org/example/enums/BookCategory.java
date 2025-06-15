package org.example.enums;

import java.util.Arrays;

public enum BookCategory {
    Biography("Biyografi"),
    Business("İş"),
    Comics("Çizgi Roman"),
    Computers("Bilgisayar Bilimi"),
    Cooking("Yemek"),
    Drama("Dram"),
    Education("Eğitim"),
    Family("Aile"),
    Fiction("Kurgu"),
    History("Tarih"),
    Juvenile_Fiction("Gençlik Romanları"),
    Philosophy("Felsefe"),
    Poetry("Şiir"),
    Political_Science("Siyaset Bilimi"),
    Psychology("Psikoloji"),
    Religion("Din"),
    Science("Bilim"),
    Self_Help("Kişisel Gelişim"),
    Social_Science("Toplum Bilimi"),
    Sports("Spor");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BookCategory fromDisplayName(String value) {
        return Arrays.stream(BookCategory.values())
                .filter(cat -> cat.displayName.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + value));
    }

    public static String toAprioriName(String displayName) {
        return fromDisplayName(displayName).name().replace("_", " ");
    }

    public static String displayNameFromString(String categoryName) {
        // Enum sabit adı gibi çalışacak şekilde dönüştür: boşlukları "_" yap
        String formatted = categoryName.replace(" ", "_");
        return Arrays.stream(BookCategory.values())
                .filter(cat -> cat.name().equalsIgnoreCase(formatted))
                .findFirst()
                .map(BookCategory::getDisplayName)
                .orElseThrow(() -> new IllegalArgumentException("Kategori eşleşmedi: " + categoryName));
    }

}