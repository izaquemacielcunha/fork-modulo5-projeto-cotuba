package br.com.unipds.ebook;

import java.nio.file.Path;
import java.util.List;

public class Ebook {
    private String title;
    private String author;
    private List<Chapter> chapters;
    private EbookType type;
    private Path outputFilePath;

    public Ebook(String title, String author, List<Chapter> chapters, EbookType type, Path outputFilePath) {
        this.title = title;
        this.author = author;
        this.chapters = chapters;
        this.type = type;
        this.outputFilePath = outputFilePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public EbookType getType() {
        return type;
    }

    public void setType(EbookType type) {
        this.type = type;
    }

    public Path getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(Path outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
