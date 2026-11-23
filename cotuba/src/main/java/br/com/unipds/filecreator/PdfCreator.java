package br.com.unipds.filecreator;

import java.nio.file.Files;
import java.util.List;

import br.com.unipds.directory.DirectoryInfos;
import br.com.unipds.filecreator.markdown.Markdown;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.properties.AreaBreakType;

public class PdfCreator {
    private final DirectoryInfos directoryInfos;
    private final Markdown markdown;

    public PdfCreator(DirectoryInfos directoryInfos, Markdown markdown) {
        this.directoryInfos = directoryInfos;
        this.markdown = markdown;
    }

    public void create() {
        try (var writer = new PdfWriter(Files.newOutputStream(directoryInfos.arquivoDeSaida()));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            //TODO: definir título e autor para o livro
            pdf.getDocumentInfo().setTitle("Livro");
            pdf.getDocumentInfo().setAuthor("Autor");

            List<String> htmls = markdown.renderToHtml();

            htmls.forEach(html -> {
                try {
                    List<IElement> convertToElements = HtmlConverter.convertToElements(html);

                    if (pdf.getNumberOfPages() == 0) {
                        pdf.addNewPage();
                    }
                    PdfOutline rootOutline = pdf.getOutlines(false);
                    if (rootOutline == null) {
                        pdf.initializeOutlines();
                        rootOutline = pdf.getOutlines(false);
                    }

                    // TODO: usar título do capítulo
                    PdfOutline chapterOutline = rootOutline.addOutline("Capítulo");
                    chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                    for (IElement element : convertToElements) {
                        pdfDocument.add((IBlockElement) element);
                    }
                    // TODO: não adicionar página depois do último capítulo
                    pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + html, ex);
                }
            });

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar PDF: " + directoryInfos.arquivoDeSaida().toAbsolutePath(), ex);
        }
    }

}
