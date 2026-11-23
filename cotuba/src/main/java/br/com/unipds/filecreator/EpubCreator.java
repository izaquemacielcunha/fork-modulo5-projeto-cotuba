package br.com.unipds.filecreator;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import br.com.unipds.directory.DirectoryInfos;
import br.com.unipds.filecreator.markdown.Markdown;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

public class EpubCreator {

    private final DirectoryInfos directoryInfos;
    private final Markdown markdown;

    public EpubCreator(DirectoryInfos directoryInfos, Markdown markdown) {
        this.directoryInfos = directoryInfos;
        this.markdown = markdown;
    }

    public void create() {
        try {
            var epub = new Book();

            //TODO: definir título e autor para o livro
            epub.getMetadata().addTitle("Livro");
            epub.getMetadata().addAuthor(new Author("Autor"));

            boolean[] ehPrimeiroCapitulo = {true};

            List<String> htmls = markdown.renderToHtml();

            htmls.forEach(html -> {
                String epubHtml = """
                          <html xmlns="http://www.w3.org/1999/xhtml">
                            <head>
                              <title>Capítulo</title>
                            </head>
                            <body>
                              %s
                            </body>
                          </html>
                        """.formatted(html);
                var chapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
                epub.addSection("Capítulo", chapter);

                if (ehPrimeiroCapitulo[0]) {
                    epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                    ehPrimeiroCapitulo[0] = false;
                }
            });

            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(directoryInfos.arquivoDeSaida()));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + directoryInfos.arquivoDeSaida().toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + directoryInfos.arquivoDeSaida().toAbsolutePath(), ex);
        }
    }
}
