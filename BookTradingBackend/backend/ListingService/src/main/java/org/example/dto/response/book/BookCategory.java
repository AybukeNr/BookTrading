package org.example.dto.response.book;

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

    private final String description;

    BookCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

