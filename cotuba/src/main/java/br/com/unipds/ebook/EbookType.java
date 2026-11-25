package br.com.unipds.ebook;

public enum EbookType {
    PDF,
    EPUB;

    public static EbookType findByName(String name) {
        for (EbookType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Formato do ebook inválido: " + name);
    }
}