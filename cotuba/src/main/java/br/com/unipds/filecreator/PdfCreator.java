package br.com.unipds.filecreator;

import br.com.unipds.ebook.Ebook;
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

import java.nio.file.Files;
import java.util.List;

public class PdfCreator {
    private final Ebook ebook;

    public PdfCreator(Ebook ebook) {
        this.ebook = ebook;
    }

    public void create() {
        try (var writer = new PdfWriter(Files.newOutputStream(ebook.getOutputFilePath()));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            //TODO: definir título e autor para o livro
            pdf.getDocumentInfo().setTitle(ebook.getTitle());
            pdf.getDocumentInfo().setAuthor(ebook.getAuthor());


            ebook.getChapters().forEach(chapter -> {
                try {
                    List<IElement> convertToElements = HtmlConverter.convertToElements(chapter.getHtmlContent());

                    if (pdf.getNumberOfPages() == 0) {
                        pdf.addNewPage();
                    }
                    PdfOutline rootOutline = pdf.getOutlines(false);
                    if (rootOutline == null) {
                        pdf.initializeOutlines();
                        rootOutline = pdf.getOutlines(false);
                    }

                    PdfOutline chapterOutline = rootOutline.addOutline(chapter.getTitle());
                    chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                    for (IElement element : convertToElements) {
                        pdfDocument.add((IBlockElement) element);
                    }
                    // TODO: não adicionar página depois do último capítulo
                    pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + chapter, ex);
                }
            });

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar PDF: " + ebook.getOutputFilePath().toAbsolutePath(), ex);
        }
    }

}
