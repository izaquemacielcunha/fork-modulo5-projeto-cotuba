package br.com.unipds.markdown;

import br.com.unipds.ebook.Chapter;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public class MarkdownRender {
    private final Parser parser;
    private final HtmlRenderer renderer;
    private final MarkdownRepository markdownRepository;

    public MarkdownRender(Parser parser, HtmlRenderer renderer, MarkdownRepository markdownRepository) {
        this.parser = parser;
        this.renderer = renderer;
        this.markdownRepository = markdownRepository;
    }

    public List<Chapter> render() {

        return markdownRepository.getChapters().stream().map(chapter -> {
            Node document = null;
            try {
                document = parser.parse(chapter.getMarkdownContent());
                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {

                        if (heading.getLevel() == 1) {
                            chapter.setTitle(((Text) heading.getFirstChild()).getLiteral());
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }
                });
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao fazer parse do arquivo " + chapter.getMarkdownFilePath().getFileName(), ex);
            }

            try {
                String html = renderer.render(document);
                chapter.setHtmlContent(html);
                return chapter;
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + chapter.getMarkdownFilePath().getFileName(), ex);
            }
        }).toList();
    }

}
