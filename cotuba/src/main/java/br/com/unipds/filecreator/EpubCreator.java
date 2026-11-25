package br.com.unipds.filecreator;

import br.com.unipds.ebook.Ebook;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EpubCreator {

    private final Ebook ebook;

    public EpubCreator(Ebook ebook) {
        this.ebook = ebook;
    }

    public void create() {
        try {
            var epub = new Book();

            //TODO: definir título e autor para o livro
            epub.getMetadata().addTitle(ebook.getTitle());
            epub.getMetadata().addAuthor(new Author(ebook.getAuthor()));

            boolean[] ehPrimeiroCapitulo = {true};

            ebook.getChapters().forEach(chapter -> {
                String epubHtml = """
                          <html xmlns="http://www.w3.org/1999/xhtml">
                            <head>
                              <title>%s</title>
                            </head>
                            <body>
                              %s
                            </body>
                          </html>
                        """.formatted(StandardCharsets.UTF_8.encode(chapter.getTitle()), chapter.getHtmlContent());
                var finalChapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
                epub.addSection(chapter.getTitle(), finalChapter);

                if (ehPrimeiroCapitulo[0]) {
                    epub.getGuide().addReference(new GuideReference(finalChapter, "text", "Start Reading"));
                    ehPrimeiroCapitulo[0] = false;
                }
            });

            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(ebook.getOutputFilePath()));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + ebook.getOutputFilePath().toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + ebook.getOutputFilePath().toAbsolutePath(), ex);
        }
    }
}
