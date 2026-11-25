package br.com.unipds.markdown;

import br.com.unipds.ebook.Chapter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

public class MarkdownRepository {
    public static final String MARKDOWN_PATH_MATCHER = "glob:**/*.md";
    private final Path path;

    public MarkdownRepository(Path path) {
        this.path = path;
    }

    public List<Chapter> getChapters() {
        List<Path> markdownFiles = readMarkdownFiles();

        return markdownFiles.stream().map(markdownFile -> {
            try {
                Chapter chapter = new Chapter();
                chapter.setMarkdownContent(Files.readString(markdownFile));
                chapter.setMarkdownFilePath(markdownFile);
                return chapter;
            } catch (IOException ex) {
                throw new RuntimeException("Erro ao ler arquivo: " + markdownFile.toAbsolutePath(), ex);
            }
        }).toList();
    }

    private List<Path> readMarkdownFiles() {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(MARKDOWN_PATH_MATCHER);
        try (Stream<Path> streamMDs = Files.list(path)) {
            List<Path> arquivosMD = streamMDs
                    .filter(matcher::matches)
                    .sorted()
                    .toList();

            if (arquivosMD.isEmpty()) {
                throw new IllegalStateException("Não foram encontrados capítulos (arquivos .md) no diretório: " + path.toAbsolutePath());
            }

            return arquivosMD;
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao fazer leitura de arquivos " + path.toAbsolutePath(), ex);
        }
    }

}
