package br.com.unipds.ebook;

import java.nio.file.Path;

public class Chapter {
    private String title;
    private String markdownContent;
    private String htmlContent;
    private Path markdownFilePath;

    public Chapter(String title, String markdownContent, String htmlContent, Path markdownFilePath) {
        this.title = title;
        this.markdownContent = markdownContent;
        this.htmlContent = htmlContent;
        this.markdownFilePath = markdownFilePath;
    }

    public Chapter() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Path getMarkdownFilePath() {
        return markdownFilePath;
    }

    public void setMarkdownFilePath(Path markdownFilePath) {
        this.markdownFilePath = markdownFilePath;
    }
}