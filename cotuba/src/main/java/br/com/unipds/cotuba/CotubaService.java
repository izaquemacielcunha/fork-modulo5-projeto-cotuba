package br.com.unipds.cotuba;

import java.util.List;

import br.com.unipds.ebook.Chapter;
import br.com.unipds.ebook.Ebook;
import br.com.unipds.ebook.EbookType;
import br.com.unipds.filecreator.EpubCreator;
import br.com.unipds.filecreator.PdfCreator;
import br.com.unipds.properties.PropertiesValue;

public class CotubaService {

    private final CotubaParams cotubaParams;

    public CotubaService(CotubaParams cotubaParams) {
        this.cotubaParams = cotubaParams;
    }

    public void generateEbook() {
        List<Chapter> chapters = cotubaParams.markdownRender().render();

        PropertiesValue propertiesValue = cotubaParams.propertiesReader().read();

        Ebook ebook = new Ebook(propertiesValue.title(), propertiesValue.author(),
                chapters, cotubaParams.directoryInfos().formato(), cotubaParams.directoryInfos().arquivoDeSaida());

        if (EbookType.PDF.equals(cotubaParams.directoryInfos().formato())) {
            PdfCreator pdfWiter = new PdfCreator(ebook);
            pdfWiter.create();
        } else if (EbookType.EPUB.equals(cotubaParams.directoryInfos().formato())) {
            EpubCreator epubCreator = new EpubCreator(ebook);
            epubCreator.create();
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + cotubaParams.directoryInfos().formato());
        }
    }
}
