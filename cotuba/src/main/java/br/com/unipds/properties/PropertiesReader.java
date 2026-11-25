package br.com.unipds.properties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

public class PropertiesReader {

    private final Path path;
    private final Properties properties;

    public PropertiesReader(Path path, Properties properties) {
        this.path = path;
        this.properties = properties;
    }

    public PropertiesValue read() {

        Path file = path.resolve("ebook.properties");

        if (!Files.exists(file)) {
            throw new RuntimeException("Arquivo ebook.properties não encontrado no diretório: " + path);
        }

        try (InputStream inputStream = new FileInputStream(file.toFile())) {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo ebook.properties: " + e.getMessage(), e);
        }

        String title = properties.getProperty("cotuba.ebook.titulo");
        String author = properties.getProperty("cotuba.ebook.autor");

        if (Objects.isNull(title) || title.isBlank()) {
            throw new RuntimeException("Propriedade 'cotuba.ebook.titulo' não encontrada no arquivo ebook.properties");
        }

        if (Objects.isNull(author) || author.isBlank()) {
            throw new RuntimeException("Propriedade 'cotuba.ebook.autor' não encontrada no arquivo ebook.properties");
        }

        return new PropertiesValue(title, author);
    }
}
