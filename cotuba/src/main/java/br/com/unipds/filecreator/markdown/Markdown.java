package br.com.unipds.filecreator.markdown;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class Markdown {
    private final Path path;
    private final Parser parser;

    public Markdown(Path path, Parser parser) {
        this.path = path;
        this.parser = parser;
    }

    public List<String> renderToHtml() {

        return readMarkdownFiles().stream().map(arquivoMD -> {
            Node document = null;
            try {
                document = parser.parseReader(Files.newBufferedReader(arquivoMD));
                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            // capítulo
                            String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                            // TODO: usar título do capítulo
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }
                });
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivoMD, ex);
            }

            try {
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                return renderer.render(document);
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
            }
        }).toList();
    }

    private List<Path> readMarkdownFiles() {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
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

}// enf of class